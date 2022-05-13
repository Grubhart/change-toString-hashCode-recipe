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
package org.grubhart.recipe

import org.grubhart.recipe.ReplaceArrayToStringHashCodeRecipe
import org.junit.jupiter.api.Test
import org.openrewrite.Recipe
import org.openrewrite.java.JavaRecipeTest

class ToStringTests: JavaRecipeTest {

    override val recipe: Recipe
        get() = ReplaceArrayToStringHashCodeRecipe()

    @Test
    fun testReplaceToString() = assertChanged(
        before = """
            package org.grubhart.recipe;

            public class Program {

                public static void main(String[] args) {
                    String argStr = args.toString();
                    
                    int arrayInt[] = {2,3,4,5};
                    String arrayIntStr = arrayInt.toString();
                    
                    int[] arrayInt2 = {1,6,7,0};
                    String arrayIntStr2 = arrayInt2.toString();
                    
                    String cadena = "hello world!";
                    String copyStr = cadena.toString();
                    
                    int[] s = new int[]{1,2,3};
                    System.out.println(s.toString());
                }
            }
        """,
        after = """
            package org.grubhart.recipe;
            
            import java.util.Arrays;

            public class Program {

                public static void main(String[] args) {
                    String argStr = Arrays.toString(args);
                    
                    int arrayInt[] = {2,3,4,5};
                    String arrayIntStr = Arrays.toString(arrayInt);
                    
                    int[] arrayInt2 = {1,6,7,0};
                    String arrayIntStr2 = Arrays.toString(arrayInt2);
                    
                    String cadena = "hello world!";
                    String copyStr = cadena.toString();
                    
                    int[] s = new int[]{1,2,3};
                    System.out.println(Arrays.toString(s));
                }
            }
        """
    )

}
