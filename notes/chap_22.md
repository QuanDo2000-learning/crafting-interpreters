# Local Variables

## Challenges

1. Our simple local array makes it easy to calculate the stack slot of each local variable. But it means that when the compiler resolves a reference to a variable, we have to do a linear scan through the array.

   Come up with something more efficient. Do you think the additional complexity is worth it?

2. How do other languages handle code like this:

   ```lox
   var a = a;
   ```

   What would you do if it was your language? Why?

3. Many languages make a distinction between variables that can be reassigned and those that can’t. In Java, the `final` modifier prevents you from assigning to a variable. In JavaScript, a variable declared with `let` can be assigned, but one declared using `const` can’t. Swift treats `let` as single-assignment and uses `var` for assignable variables. Scala and Kotlin use `val` and `var`.

   Pick a keyword for a single-assignment variable form to add to Lox. Justify your choice, then implement it. An attempt to assign to a variable declared using your new keyword should cause a compile error.

4. Extend clox to allow more than 256 local variables to be in scope at a time.
