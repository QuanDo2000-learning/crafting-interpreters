# A Virtual Machine

## Challenges

1. What bytecode instruction sequences would you generate for the following expressions:

   ```lox
   1 * 2 + 3
   1 + 2 * 3
   3 - 2 - 1
   1 + 2 * 3 - 4 / -5
   ```

   (Remember that Lox does not have a syntax for negative number literals, so the `-5` is negating the number 5.)

2. If we really wanted a minimal instruction set, we could eliminate either `OP_NEGATE` or `OP_SUBTRACT`. Show the bytecode instruction sequence you would generate for:

   ```lox
   4 - 3 * -2
   ```

   First, without using `OP_NEGATE`. Then, without using `OP_SUBTRACT`.

   Given the above, do you think it makes sense to have both instructions? Why or why not? Are there any other redundant instructions you would consider including?

3. Our VM’s stack has a fixed size, and we don’t check if pushing a value overflows it. This means the wrong series of instructions could cause our interpreter to crash or go into undefined behavior. Avoid that by dynamically growing the stack as needed.

   What are the costs and benefits of doing so?

4. To interpret `OP_NEGATE`, we pop the operand, negate the value, and then push the result. That’s a simple implementation, but it increments and decrements `stackTop` unnecessarily, since the stack ends up the same height in the end. It might be faster to simply negate the value in place on the stack and leave `stackTop` alone. Try that and see if you can measure a performance difference.

   Are there other instructions where you can do a similar optimization?
