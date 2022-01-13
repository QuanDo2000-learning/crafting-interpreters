# Evaluating Expressions

For our first interpreter, we will take the simplest and shortest part and execute the syntax tree.
Right now, our interpreter only supports expressions. So, to "execute" code, we need to evaluate the expression and produce a value.

1. What kinds of values do we produce?
2. How do we organize those chunks of code?

## Representing Values

In Lox, a variable con store different types at different points in time. To connect that with Java's static typing, we will use java.lang.Object.
One reason we are using Java is due to its object representation and a nice garbage collector.

## Evaluating Expressions

There are many design patterns we can use to code our interpreter portion of the code.
One of them is Gang of Four's [Interpreter design patter](https://en.wikipedia.org/wiki/Interpreter_pattern) where we can tell each syntax tree node to interpret itself. This pattern is nice but it will get messy if we stuff all that code into the tree classes.
Another one, which we will use, is our previous Visitor pattern.
