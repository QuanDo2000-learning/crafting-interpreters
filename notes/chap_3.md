# The Lox Language

The author's [Lox interpreter](https://github.com/munificent/craftinginterpreters).

## Hello, Lox

Lox has a similar syntax to C.

```lox
// Your first Lox program!
print "Hello, world!";
```

## A High-Level Language

Lox is picked because it is compact and a high-level language.

### Dynamic typing

Lox is dynamically typed. Variables can store values of any type, and a single variable can store values of different types at different times.
Performing an operation on values of the wrong type will generate an error and reported at runtime.
A static type system is not picked because it is complicated to learn and implement. Using a dynamic type language is simpler and easier to implement.

### Automatic memory management

We will implement methods to manage memory automatically similar to many high-level languages.
Two main techniques are **reference counting** and **tracing garbage collection** (also called **garbage collection** or **GC**). Ref counters are simpler to implement but all languages eventually add in a full tracing GC.

## Data Types

- **Booleans**. Logic.

```lox
true;  // Not false.
false; // Not *not* false.
```

- **Numbers**. Double-precision floating point.

```lox
1234;  // An integer
12.34; // A decimal number
```

- **Strings**. Enclosed in double quotes.

```lox
"I am a string";
"";    // The empty string
"123"; // This is a string, not a number.
```

- **Nil**. "No value" or NULL.

```lox
nil;
```

## Expressions

The combination of built-in data types and their literals.

### Arithmetic

Lox has similar arithmetic operators from C.

```lox
add + me;
subtract - me;
multipy * me;
divide / me;
```

Subexpressions on either side are **operands**. These are called **binary** operators because there are _two_ operands. These are also **infix** operators because the operator is fixed _in_ the middle (compared to **prefix** or **postfix**).

`-` can be used as both an infix and prefix.

```lox
-negateMe;
```

All the operators only work on numbers and any other types will produce an error. The exception is the `+` operator which can be applied to strings to concatenate them.

### Comparison and equality

Comparison Operators

```lox
less < than;
lessThan <= orEqual;
greater > than;
greaterThan >= orEqual;
```

Test or equality or inequality

```lox
1 == 2;         // false.
"cat" != "dog"; // true.
314 == "pi";    // false.
123 == "123";   // false.
```

### Logical operators

The not operator, a prefix `!`.

```lox
!true;  // false.
!false; // true.
```

The `and` and `or` operations **short-circuit**, meaning they will return immediately when it is not required to evaluate the remaining operands to determine the outcome.

```lox
true and false; // false.
true and true;  // true.
false or false; // false.
true or false;  // true.
```

### Precedence and grouping

All operators have the same precedence similar to C and grouping is done with `()`.

## Statements

While an expression's main job is to produce a _value_, a statement's job is to produce an _effect_.
One example is the `print` statement.

```lox
print "Hello, world!";
```

An expression followed by a semicolon (`;`) promotes the expression to statement-hood. This is called **expression statement**.

```lox
"some expression";
```

If we want to pack a serires of statements where only one is expected, a **block** can be used.

```lox
{
  print "One statement.";
  print "Two statements.";
}
```

Blocks also affect scoping.

## Variables

Variables are declared using `var` statements. If the initializer is ommited, the default value is `nil`.

```lox
var imAVariable = "here is my value";
var iAmNil;
```

Variables can be accesses and assigned using its name.

## Control Flow

`if` statements.

```lox
if (condition) {
  print "yes";
} else {
  print "no";
}
```

`while` loop.

```lox
var a = 1;
while (a < 10) {
  print a;
  a = a + 1;
}
```

`for` loop.

```lox
for (var a = 1; a < 10; a = a + 1) {
  print a;
}
```

## Functions

Function calls looks the same as it does in C.

```lox
makeBreakfast(bacon, eggs, toast);
```

Functions can be called without arguments but the parentheses are mandatory to call the function. Without parentheses, the function is only referred to but not called.
Functions are defined using `fun`.

```lox
fun printSum(a, b) {
  print a + b;
}
```

We need to make some distinctions between "parameter" and "argument".

- An **argument** is an actual value passed to a function. So, a function _call_ has an _argument_ list. It is also referred to as **actual parameter**.
- A **parameter** is a variable that holds the value of the argument inside the body of the function. So, a function _declaration_ has a _parameter_ list. They are also called **formal parameters** or **formals**.

The body of a function is always a block. A value can be returned using a `return` statement. If execution reaches the end without hitting a `return`, the function returns `nil`.

```lox
fun returnSum(a, b) {
  return a + b;
}
```

### Closures

Functions are _first class_ in Lox, which means they are real values that can be referenced to, stored in variables, and pass around.

```lox
fun addPair(a, b) {
  return a + b;
}

fun identity(a) {
  return a;
}

print identity(addPair)(1, 2); // Print "3".
```

We can declare local functions inside another function.

```lox
fun outerFunction() {
  fun localFunction() {
    print "I'm local!";
  }

  localFunction();
}
```

If we combine local functions, first-class functions, and block scope, we can have an interesting situation.

```lox
fun returnFunction() {
  var outside = "outside";

  fun inner() {
    print outside;
  }

  return inner;
}

var fn = returnFunction();
fn();
```

Here, `inner()` acceses a local variable declared outside of its body. `inner()` "hold on" to references to any surrounding variables that it uses so that they stay around even after the outer function has returned.
This concept is called **closures** but it can also refer to _any_ first-class function nowadays.

## Classes

### Why might any language want to be object oriented?

**Object-oriented programming** (**OOP**) is still popular. Using objects for a dynamically typed language is very useful and we need some way of defining compound data types to bundle stuffs together.
Another useful feature is the ability to scope methods to the object without having to name different methods differently for each type.

### Why is Lox object oriented?

It will be useful to cover OOP languages due to its popularity.

### Classes or prototypes

Class-based languages differentiate between classes and instances. Instances store the state for each object and have a reference to the instance's class. Classes contain the methods and inheritance chain.
Calling a method on an instance means looking up the instance's class and finding the method there.
Prototype-based languages merges the two concept together. Only objects exists, no classes or instances, and each object can contain state and methods. Objects can directly inherit from each other (or "delegate to" in prototypal lingo).
This means prototypal languages are easier to implement but introduces more complexity on the user's end. Lox will be implemented with classes.

### Classes in Lox

```lox
class Breakfast {
  cook() {
    print "Eggs a-fryin'!";
  }

  serve(who) {
    print "Enjoy your breakfast, " + who + ".";
  }
}
```

The body of a class contains its methods. They are like function declarations without the `fun` keyword. Similar to functions, classes are first class in Lox.

```lox
// Store it in variables
var someVariable = Breakfast;

// Pass it to functions.
someFunction(Breakfast);
```

To create instances, call the class like a function.

```lox
var breakfast = Breakfast();
print breakfast; // "Breakfast instance".
```

### Instantiation and initialization

Classes in Lox also have fields to store state, properties of objects. Assigning to a field will create one if it doesn't exist.

```lox
breakfast.meat = "sausage";
breakfast.bread = "sourdough";
```

Accessing a field or method of the current object from within a method can be done with `this`.

```lox
class Breakfast {
  serve(who) {
    print "Enjoy your " + this.meat + " and " + this.bread + ", " + who + ".";
  }

  // ...
}
```

We can define an initializer for the object using `init()`, which is called automatically when the object is constructed.

```lox
class Breakfast {
  init(meat, bread) {
    this.meat = meat;
    this.bread = bread;
  }

  // ...
}

var baconAndToast = Breakfast("bacon", "toast");
baconAndToast.serve("Dear Reader");
// "Enjoy your bacon and toast, Dear Reader."
```

### Inheritance

Inheritance can be done using `<` when declaring a class.

```lox
class Brunch < Breakfast {
  drink() {
    print "How about a Bloddy Mary?";
  }
}
```

In the example above, Brunch is the **derived class** or **subclass**, and Breakfast is the **base class** or **superclass**.
Every method defined in the superclass is also available to its subclasses.

```lox
var benedict = Brunch("ham", "English muffin");
benedict.serve("Noble Reader");
```

The `init()` method is also inherited. We can use `super` to call the `init()` of the superclass similar to Java.

```lox
class Brunch < Breakfast {
  init(meat, bread, drink) {
    super.init(meat, bread);
    this.drink = drink;
  }
}
```

## The Standard Library

The "core" or "standard" library is the set of functionality that is implemented directly in the interpreter and that all user-defined behavior is build on top of.
The only functionality for Lox covered in the book is `print` and `clock()`

## Challenges

1. Write some sample Lox programs and run them (you can use the implementations of Lox in [my repository](https://github.com/munificent/craftinginterpreters)). Try to come up with edge case behavior I didn't specify here. Does it do what you expect? Why or why not?

   > Ran into some trouble installing the author's implementation.  
   > Dart was installed successfully and added to Path but make get was not working. Changed to using mingw32-make but ran into an error.

2. This informal introduction leaves a lot unspecified. List several open questions you have about the language's syntax and semantics. What do you think the answer should be?

   > Why was infix operators chosen instead of prefix or suffix? One reason I would go for is familiarity and ease of reading.  
   > Since loop was covered, there doesn't seem to be a way to break out of loops. Why? Maybe it adds more complexity to the implementation.

3. Lox is a pretty tiny language. What features do you think it is missing that would make it annoying to use for real programs? (Aside from the standard library, of course.)

   > One feature would be bitwise operators and modulo operator. Some algorithms would benefit if we have these operators.
