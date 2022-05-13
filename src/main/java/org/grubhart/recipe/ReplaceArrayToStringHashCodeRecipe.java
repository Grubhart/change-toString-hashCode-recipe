/*
 * Copyright 2021 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grubhart.recipe;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.MethodMatcher;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.Collections;
import java.util.Set;

public class ReplaceArrayToStringHashCodeRecipe extends Recipe {

    private static final MethodMatcher ARRAY_TO_STRING = new MethodMatcher("java.lang.Object toString()");
    private static final MethodMatcher ARRAY_HASHCODE = new MethodMatcher("java.lang.Object hashCode()");


    @Override
    public String getDisplayName () {
        return "Replace Array toString and Hash method";
    }
    @Override
    public String getDescription () {
        return "Replace Array toString and Hash method with static version i.e. Arrays.toString(array)";
    }

    @Override
    public Set<String> getTags() { return Collections.singleton("RSPEC-2116");}

    @Override
    public JavaIsoVisitor<ExecutionContext> getVisitor(){
        return new ReplaceArrayWithStaticMethodsVisitor();
    }

    public class ReplaceArrayWithStaticMethodsVisitor extends JavaIsoVisitor<ExecutionContext> {


        private JavaTemplate toStringTemplate = JavaTemplate.builder(this::getCursor, "Arrays.toString(#{any(java.lang.Object)})")
                .imports("java.util.Arrays")
                .build();

        private JavaTemplate hashcodeTemplate = JavaTemplate.builder(this::getCursor, "Arrays.hashCode(#{any(java.lang.Object)})")
                .imports("java.util.Arrays")
                .build();

        /*
        With 'm.getSelect().getType().toString().endsWith("[]")' we catch every array possible: int[] , Object[], etc.
        <MATCHER>.matches(m) allow identify if the method is toString or hashCode
        I call first the array validation because this is a && operator if the first validation fail, we don't evaluate the second one
         */
        @Override
        public J.MethodInvocation visitMethodInvocation(J.MethodInvocation method,ExecutionContext executionContext){

            J.MethodInvocation m = super.visitMethodInvocation(method,executionContext);

            if(m.getSelect().getType().toString().endsWith("[]") && ARRAY_TO_STRING.matches(m)){
                JavaType.Method methodType = m.getMethodType();
                J.Identifier select = (J.Identifier) m.getSelect();
                m = m.withTemplate(toStringTemplate,m.getCoordinates().replace(),select);
                m = m.withMethodType(methodType);
                maybeAddImport("java.util.Arrays");
            }

            if(m.getSelect().getType().toString().endsWith("[]") && ARRAY_HASHCODE.matches(m)){
                JavaType.Method methodType = m.getMethodType();
                J.Identifier select = (J.Identifier) m.getSelect();
                m = m.withTemplate(hashcodeTemplate,m.getCoordinates().replace(),select);
                m = m.withMethodType(methodType);
                maybeAddImport("java.util.Arrays");
            }
            return m;      }
    }
}
