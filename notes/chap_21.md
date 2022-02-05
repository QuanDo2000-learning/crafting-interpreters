# Global Variables

## Challenges

1. The compiler adds a global variable’s name to the constant table as a string every time an identifier is encountered. It creates a new constant each time, even if that variable name is already in a previous slot in the constant table. That’s wasteful in cases where the same variable is referenced multiple times by the same function. That, in turn, increases the odds of filling up the constant table and running out of slots since we allow only 256 constants in a single chunk.

   Optimize this. How does your optimization affect the performance of the compiler compared to the runtime? Is this the right trade-off?

2. Looking up a global variable by name in a hash table each time it is used is pretty slow, even with a good hash table. Can you come up with a more efficient way to store and access global variables without changing the semantics?

3. When running in the REPL, a user might write a function that references an unknown global variable. Then, in the next line, they declare the variable. Lox should handle this gracefully by not reporting an “unknown variable” compile error when the function is first defined.

   But when a user runs a Lox _script_, the compiler has access to the full text of the entire program before any code is run. Consider this program:

   ```lox
   fun useVar() {
     print oops;
   }

   var ooops = "too many o's!";
   ```

   Here, we can tell statically that `oops` will not be defined because there is _no_ declaration of that global anywhere in the program. Note that `useVar()` is never called either, so even though the variable isn’t defined, no runtime error will occur because it’s never used either.

   We could report mistakes like this as compile errors, at least when running from a script. Do you think we should? Justify your answer. What do other scripting languages you know do?
