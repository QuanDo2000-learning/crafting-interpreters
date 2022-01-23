# Classes

This chapter and the next will help us understand more about how to design and build object systems.

## OOP and Classes

There are three paths to object-oriented programming.

- Classes
- [Prototypes](http://gameprogrammingpatterns.com/prototype.html)
- [Multimethods](https://en.wikipedia.org/wiki/Multiple_dispatch)

Classes are the classic approach and the most popular style.
Prototypes are second in popularity and it can be seen in JavaScript (and to some extent [Lua](https://www.lua.org/pil/13.4.1.html)).
Multimethods are the least known method and they can be seen in [CLOS](https://en.wikipedia.org/wiki/Common_Lisp_Object_System), [Dylan](https://opendylan.org/), [Julia](https://julialang.org/), or [Raku](https://docs.raku.org/language/functions#Multi-dispatch).

The main goal of classes is to bundle data along with the code that operates on it. The _class_ will contain several elements.

1. A _constructor_ to create and initialize new _instances_ of the class.
2. A way to store and access _fields_ on instances.
3. _Methods_ shared by all instances that operate on each instances' state.

## Class Declarations

First of, we will start with syntax, and that means adding a new `class` statement to our grammar rule.

```BNF
declaration    -> classDecl
               | funDecl
               | varDecl
               | statement ;

classDecl      -> "class" IDENTIFIER "{" function* "}" ;
```

A class declaration will consists of the `class` keyword, followed by its name, then a curly-braced body containing method declarations.
The methods declared inside a class don't have a leading `fun` keyword.
Fields won't be a part of class declaration, similar to other dynamically typed languages. Fields can be freely added as needed using normal imperative code.
A new Java class called LoxClass will be created for the _runtime_ representation of a class.

## Creating Instances

Now that we have classes, we need to create instances. Unlike some languages, Lox doesn't have "static" methods that we can call right on the class itself.
There isn't a standard way of creating instances across different languages. What we will do for Lox is use the call expression of class objects to create new instances.
So, when we call a class, a new instance implemented by LoxInstance class in Java will be created.

## Properties on Instances

Next on the list, we will now implement state (properties) for the instances.
Lox is similar to JavaScript and Python in how it handles state. Properties can be accesses by both inside methods and outside code using the `.` syntax.
We will have to do a little modification to one of our previous grammar rule.

```BNF
call           -> primary ( "(" arguments? ")" | "." IDENTIFIER )* ;
```

### Get expressions

We now add in code to parse our properties rule and code to resolve them.
Within our LoxInstance, a map will be used to store the properties.
When the interpreter tries to get a field, if it exists in the map, we return it. If not, we raise an error.
So we have just finished the code to read state from an instance. Now let's get to writing them.

### Set expressions

For setters, we have to add some grammar rules for left-hand side dotted identifiers.

```BNF
assignment     -> ( call "." )? IDENTIFIER "=" assignment
               | logic_or ;
```

Unlike our getters, our setters don't chain but instead we get all the expression before the last dot and only set the new value after that.
Since we have an arbitrary number of dotted expressions, it is impossible to look forward for an `=` operator.
Instead, we will parse the left-hand side like a normal expression and if we see an equal sign after, we transformed the previous expression to the correct syntax tree node.

## Methods on Classes

As we get to implementing methods on classes, there isn't much to do for the parser because most of the basics are already there.
There are a few decisions we need to make about how methods and variables interact. For Lox, we will allow class methods to be stored in variables and called later on. It is also possible to assign a function to a class property.

## This

We will now implement `this`, a way for us to refer to the "current" instance from within the methods.
There is a lot of similarities between implementing `this` and closure so we already have a lot of the machinery required.

### Invalid uses of this

Just in case the user uses this outside of a method, we need to detect it and report an error.

## Constructors and Initializers

This is the part where we make sure that each object starts in a good state.
"Contructing" an object consists of two operations.

1. The runtime _allocates_ the memory required for a fresh instance.
2. A user-provided chunk of code is called which _initializes_ the unformed object.

### Invoking init() directly

Just in case the user decided to call `init()` directly, we will make Lox return `this` by default.

### Returning from init()

Here, we will make it an error for the user to return a value from an `init()` method. An empty `return` is still possible.

## Challenges

1. We have methods on instances, but there is no way to define “static” methods that can be called directly on the class object itself. Add support for them. Use a `class` keyword preceding the method to indicate a static method that hangs off the class object.

   ```lox
   class Math {
     class square(n) {
       return n * n;
     }
   }

   print Math.square(3); // Prints "9".
   ```

   You can solve this however you like, but the “[metaclasses](https://en.wikipedia.org/wiki/Metaclass)” used by Smalltalk and Ruby are a particularly elegant approach. _Hint: Make LoxClass extend LoxInstance and go from there._

2. Most modern languages support “getters” and “setters”—members on a class that look like field reads and writes but that actually execute user-defined code. Extend Lox to support getter methods. These are declared without a parameter list. The body of the getter is executed when a property with that name is accessed.

   ```lox
   class Circle {
     init(radius) {
       this.radius = radius;
     }

     area {
       return 3.141592653 * this.radius * this.radius;
     }
   }

   var circle = Circle(4);
   print circle.area; // Prints roughly "50.2655".
   ```

3. Python and JavaScript allow you to freely access an object’s fields from outside of its own methods. Ruby and Smalltalk encapsulate instance state. Only methods on the class can access the raw fields, and it is up to the class to decide which state is exposed. Most statically typed languages offer modifiers like `private` and `public` to control which parts of a class are externally accessible on a per-member basis.

   What are the trade-offs between these approaches and why might a language prefer one or the other?
