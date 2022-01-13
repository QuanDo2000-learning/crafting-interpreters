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

### Evaluating literals

Before, we converted a literal _token_ into a literal _syntax tree node_ in the parser, now we will convert the literal tree node into a runtime value.

### Evaluating parentheses

To evaluate the parentheses, which are grouping expressions, we recursively evaluate the subexpression inside the parentheses.

### Evaluating unary expressions

For unary expressions, we have to evaluate the right subexpression first before applying the unary operator.
Here, our interpreter will do a **post-order traversal**, where each node evaluates its children before doing its own work.

### Truthiness and falsiness

Here, we have to decide what happens when you use something other than `true` or `false` in a logic operation.
Most dynamically typed language partitions all values into two sets, one "truthy" and one "falsey". This is arbitrary and is different for each language.
In Lox, we follow Ruby's simple rule: `false` and `nil` are falsey and everything else is truthy.

### Evaluating binary operators

Similar to unary expressions, now we just have to evaluate the left, then the right subexpressions, then evaluate the expression based on the operator.
One thing to note is that we will be overloading the `+` operator to concatenate strings so we have to take extra care of that.

## Runtime Errors

The cast that was used earlier might fail and we will be responsible for handling that error gracefully.
Before, we dealt with _syntax_ and _static_ errors before any code is executed. Now, we will deal with **runtime errors**, any errors that occur when the program is running.

### Detecting runtime errors

Instead of using Java's exception, we will create a Lox-specific one so we can customize it.

## Hooking Up the Interpreter

Here, we add public methods so the interpreter class can interact with the rest of the program.

### Reporting runtime errors

Here, we add code to report the runtime errors.

## Challenges

**1. Allowing comparisons on types other than numbers could be useful. The operators might have a reasonable interpretation for strings. Even comparisons among mixed types, like 3 < "pancake" could be handy to enable things like ordered collections of heterogeneous types. Or it could simply lead to bugs and confusion.**
**Would you extend Lox to support comparing other types? If so, which pairs of types do you allow and how do you define their ordering? Justify your choices and compare them to other languages.**

**2. Many languages define + such that if either operand is a string, the other is converted to a string and the results are then concatenated. For example, "scone" + 4 would yield scone4. Extend the code in visitBinaryExpr() to support that.**

**3. What happens right now if you divide a number by zero? What do you think should happen? Justify your choice. How do other languages you know handle division by zero, and why do they make the choices they do?**
**Change the implementation in visitBinaryExpr() to detect and report a runtime error for this case.**
