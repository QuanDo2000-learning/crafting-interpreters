# Inheritance

In this chapter, we will implement inheritance, a way to reuse similar code between classes.

## Superclasses and Subclasses

First of, we will decide the syntax to specify a superclass when declaring a class. We will follow Ruby and use less-than sign (`<`).
That will be followed by a small change in our grammar rules.

```lox
classDecl      -> "class" IDENTIFIER ( "<" IDENTIFIER )?
                  "{" function* "}" ;
```

Here, we will also detect and report an error in case the user decided to inherit from itself.

## Inheriting Methods

The requirements of inheritance is the ability to call a method of the superclass on the subclass.

## Calling Superclass Methods

When using subclass, we would usually want to run the superclass' method and then refine it. So we will need a syntax to refer to the superclass' method.
In Lox, we will use the same syntax as Java, using `super`.

### Syntax

First, let's add and modify some grammar rules to account for the `super` keyword.

```BNF
primary        -> "true" | "false" | "nil" | "this"
               | NUMBER | STRING | IDENTIFIER | "(" expression ")"
               | "super" "." IDENTIFIER ;
```

### Semantics

Now, we need to implement the `super` expression so that it works how we want it to. When we call `super` we want the code to lookup the method starting on the superclass of the class containing the `super` expression.

### Invalid uses of super

Now we have to deal with cases where the user used `super` improperly.

## Conclusion

That was the last bit of code to complete our implementation of Lox in Java.
We have implemented a lot of features.

- Tokens and lexing
- Abstract syntax trees
- Recursive descent parsing
- Prefix and infix expressions
- Runtime representation of objects
- Interpreting code using the Visitor pattern
- Lexical scope
- Environment chains for storing variables
- Control flow
- Functions with parameters
- Closures
- Static vairable resolution and error detection
- Classes
- Constructors
- Fields
- Methods
- Inheritance

## Challenges

1. Lox supports only _single inheritance_—a class may have a single superclass and that’s the only way to reuse methods across classes. Other languages have explored a variety of ways to more freely reuse and share capabilities across classes: mixins, traits, multiple inheritance, virtual inheritance, extension methods, etc.

   If you were to add some feature along these lines to Lox, which would you pick and why? If you’re feeling courageous (and you should be at this point), go ahead and add it.

2. In Lox, as in most other object-oriented languages, when looking up a method, we start at the bottom of the class hierarchy and work our way up—a subclass’s method is preferred over a superclass’s. In order to get to the superclass method from within an overriding method, you use `super`.

   The language [BETA](https://beta.cs.au.dk/) takes the [opposite approach](http://journal.stuffwithstuff.com/2012/12/19/the-impoliteness-of-overriding-methods/). When you call a method, it starts at the _top_ of the class hierarchy and works _down_. A superclass method wins over a subclass method. In order to get to the subclass method, the superclass method can call `inner`, which is sort of like the inverse of `super`. It chains to the next method down the hierarchy.

   The superclass method controls when and where the subclass is allowed to refine its behavior. If the superclass method doesn’t call `inner` at all, then the subclass has no way of overriding or modifying the superclass’s behavior.

   Take out Lox’s current overriding and `super` behavior and replace it with BETA’s semantics. In short:

   - When calling a method on a class, prefer the method _highest_ on the class’s inheritance chain.
   - Inside the body of a method, a call to `inner` looks for a method with the same name in the nearest subclass along the inheritance chain between the class containing the `inner` and the class of `this`. If there is no matching method, the `inner` call does nothing.

   For example:

   ```lox
   class Doughnut {
     cook() {
       print "Fry until golden brown.";
       inner();
       print "Place in a nice box.";
     }
   }

   class BostonCream < Doughnut {
     cook() {
       print "Pipe full of custard and coat with chocolate.";
     }
   }

   BostonCream().cook();
   ```

   This should print:

   ```lox
   Fry until golden brown.
   Pipe full of custard and coat with chocolate.
   Place in a nice box.
   ```

3. In the chapter where I introduced Lox, [I challenged you](http://www.craftinginterpreters.com/the-lox-language.html#challenges) to come up with a couple of features you think the language is missing. Now that you know how to build an interpreter, implement one of those features.
