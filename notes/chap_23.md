# Jumping Back and Forth

## Challenges

1. In addition to `if` statements, most C-family languages have a multi-way `switch` statement. Add one to clox. The grammar is:

   ```BNF
   switchStmt     → "switch" "(" expression ")"
                   "{" switchCase* defaultCase? "}" ;
   switchCase     → "case" expression ":" statement* ;
   defaultCase    → "default" ":" statement* ;
   ```

   To execute a `switch` statement, first evaluate the parenthesized switch value expression. Then walk the cases. For each case, evaluate its value expression. If the case value is equal to the switch value, execute the statements under the case and then exit the `switch` statement. Otherwise, try the next case. If no case matches and there is a `default` clause, execute its statements.

   To keep things simpler, we’re omitting fallthrough and `break` statements. Each case automatically jumps to the end of the switch statement after its statements are done.

2. In jlox, we had a challenge to add support for `break` statements. This time, let’s do `continue`:

   ```BNF
   continueStmt → "continue" ";" ;
   ```

   A `continue` statement jumps directly to the top of the nearest enclosing loop, skipping the rest of the loop body. Inside a `for` loop, a `continue` jumps to the increment clause, if there is one. It’s a compile-time error to have a `continue` statement not enclosed in a loop.

   Make sure to think about scope. What should happen to local variables declared inside the body of the loop or in blocks nested inside the loop when a `continue` is executed?

3. Control flow constructs have been mostly unchanged since Algol 68. Language evolution since then has focused on making code more declarative and high level, so imperative control flow hasn’t gotten much attention.

   For fun, try to invent a useful novel control flow feature for Lox. It can be a refinement of an existing form or something entirely new. In practice, it’s hard to come up with something useful enough at this low expressiveness level to outweigh the cost of forcing a user to learn an unfamiliar notation and behavior, but it’s a good chance to practice your design skills.
