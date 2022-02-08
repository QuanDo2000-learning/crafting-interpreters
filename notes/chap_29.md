# Superclasses

## Challenges

1. A tenet of object-oriented programming is that a class should ensure new objects are in a valid state. In Lox, that means defining an initializer that populates the instance’s fields. Inheritance complicates invariants because the instance must be in a valid state according to all of the classes in the object’s inheritance chain.

   The easy part is remembering to call `super.init()` in each subclass’s `init()` method. The harder part is fields. There is nothing preventing two classes in the inheritance chain from accidentally claiming the same field name. When this happens, they will step on each other’s fields and possibly leave you with an instance in a broken state.

   If Lox was your language, how would you address this, if at all? If you would change the language, implement your change.

2. Our copy-down inheritance optimization is valid only because Lox does not permit you to modify a class’s methods after its declaration. This means we don’t have to worry about the copied methods in the subclass getting out of sync with later changes to the superclass.

   Other languages, like Ruby, _do_ allow classes to be modified after the fact. How do implementations of languages like that support class modification while keeping method resolution efficient?

3. In the [jlox chapter on inheritance](http://www.craftinginterpreters.com/inheritance.html), we had a challenge to implement the BETA language’s approach to method overriding. Solve the challenge again, but this time in clox. Here’s the description of the previous challenge:

   In Lox, as in most other object-oriented languages, when looking up a method, we start at the bottom of the class hierarchy and work our way up—a subclass’s method is preferred over a superclass’s. In order to get to the superclass method from within an overriding method, you use `super`.

   The language [BETA](https://beta.cs.au.dk/) takes the [opposite approach](http://journal.stuffwithstuff.com/2012/12/19/the-impoliteness-of-overriding-methods/). When you call a method, it starts at the _top_ of the class hierarchy and works _down_. A superclass method wins over a subclass method. In order to get to the subclass method, the superclass method can call `inner`, which is sort of like the inverse of `super`. It chains to the next method down the hierarchy.

   The superclass method controls when and where the subclass is allowed to refine its behavior. If the superclass method doesn’t call `inner` at all, then the subclass has no way of overriding or modifying the superclass’s behavior.

   Take out Lox’s current overriding and `super` behavior, and replace it with BETA’s semantics. In short:

   - When calling a method on a class, the method _highest_ on the class’s inheritance chain takes precedence.
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

   ```text
   Fry until golden brown.
   Pipe full of custard and coat with chocolate.
   Place in a nice box.
   ```

   Since clox is about not just implementing Lox, but doing so with good performance, this time around try to solve the challenge with an eye towards efficiency.
