# Change toString and HashCode Recipe

While hashCode and toString are available on arrays, they are largely useless. hashCode returns the array’s "identity hash code", and toString returns nearly the same value. Neither method’s output actually reflects the array’s contents. Instead, you should pass the array to the relevant static Arrays method.


    String argStr = args.toString(); // Noncompliant
    int argHash = args.hashCode(); // Noncompliant
    String argStr = Arrays.toString(args);
    int argHash = Arrays.hashCode(args);

This Recipe evaluate arrays with *[] pattern and when that arrays call their toString() or hashCode() it replace that call with Arrays.toString(array) or Arrays.hashCode(array)

example:

int arrayInt[] = {2,3,4,5};
int arrayIntHash = Arrays.hashCode(arrayInt);

change to:
> int arrayInt[] = {2,3,4,5};
> String arrayIntStr = Arrays.toString(arrayInt);

The code is in org.grubhart.recipe package

and you can found the tests on org.grubhart.recipe package