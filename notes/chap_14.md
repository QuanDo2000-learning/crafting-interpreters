# Chunks of Bytecode

## Challenges

1. Our encoding of line information is hilariously wasteful of memory. Given that a series of instructions often correspond to the same source line, a natural solution is something akin to [run-length encoding](https://en.wikipedia.org/wiki/Run-length_encoding) of the line numbers.

   Devise an encoding that compresses the line information for a series of instructions on the same line. Change `writeChunk()` to write this compressed form, and implement a `getLine()` function that, given the index of an instruction, determines the line where the instruction occurs.

   _Hint: It’s not necessary for `getLine()` to be particularly efficient. Since it is called only when a runtime error occurs, it is well off the critical path where performance matters._

2. Because `OP_CONSTANT` uses only a single byte for its operand, a chunk may only contain up to 256 different constants. That’s small enough that people writing real-world code will hit that limit. We could use two or more bytes to store the operand, but that makes _every_ constant instruction take up more space. Most chunks won’t need that many unique constants, so that wastes space and sacrifices some locality in the common case to support the rare case.

   To balance those two competing aims, many instruction sets feature multiple instructions that perform the same operation but with operands of different sizes. Leave our existing one-byte `OP_CONSTANT` instruction alone, and define a second `OP_CONSTANT_LONG` instruction. It stores the operand as a 24-bit number, which should be plenty.

   Implement this function:

   ```c++
   void writeConstant(Chunk* chunk, Value value, int line) {
     // Implement me...
   }
   ```

   It adds `value` to `chunk`’s constant array and then writes an appropriate instruction to load the constant. Also add support to the disassembler for `OP_CONSTANT_LONG` instructions.

   Defining two instructions seems to be the best of both worlds. What sacrifices, if any, does it force on us?

3. Our `reallocate()` function relies on the C standard library for dynamic memory allocation and freeing. `malloc()` and `free()` aren’t magic. Find a couple of open source implementations of them and explain how they work. How do they keep track of which bytes are allocated and which are free? What is required to allocate a block of memory? Free it? How do they make that efficient? What do they do about fragmentation?

   Hardcore mode: Implement `reallocate()` without calling `realloc(),` `malloc(),` or `free().` You are allowed to call `malloc()` _once_, at the beginning of the interpreter’s execution, to allocate a single big block of memory, which your `reallocate()` function has access to. It parcels out blobs of memory from that single region, your own personal heap. It’s your job to define how it does that.
