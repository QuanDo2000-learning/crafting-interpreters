// chunk.h
// Module to define code representation.

#ifndef clox_chunk_h
#define clox_chunk_h

#include "common.h"
#include "value.h"

// Each opcode describes an instruction.
typedef enum {
  OP_CONSTANT,  // Produce a constant (2 bytes)
  OP_NIL,       // Push nil value onto stack (1 byte)
  OP_TRUE,      // Push true value onto stack (1 byte)
  OP_FALSE,     // Push false value onto stack (1 byte)
  OP_EQUAL,     // Top 2 == Top 1 on stack (1 byte)
  OP_GREATER,   // Top 2 > Top 1 on stack (1 byte)
  OP_LESS,      // Top 2 < Top 1 on stack (1 byte)
  OP_ADD,       // Top 2 + Top 1 on stack (1 byte)
  OP_SUBTRACT,  // Top 2 - Top 1 on stack (1 byte)
  OP_MULTIPLY,  // Top 2 * Top 1 on stack (1 byte)
  OP_DIVIDE,    // Top 2 / Top 1 on stack (1 byte)
  OP_NOT,       // Reverse the logical value (1 byte)
  OP_NEGATE,    // Negate top value on stack (1 byte)
  OP_RETURN,    // Return from current function (1 byte)
} OpCode;

// Struct to hold data stored along with the instructions.
// The structure is a dynamic array.
typedef struct {
  int count;             // Number of in use elements
  int capacity;          // Number of elements
  uint8_t* code;         // Points to the first instruction
  int* lines;            // Array of line number for each byte of instruction
  ValueArray constants;  // Dynamic array for constants
} Chunk;

// Initialize a new chunk with zero count, capacity and points to a null pointer.
void initChunk(Chunk* chunk);
// Free the memory in a chunk.
void freeChunk(Chunk* chunk);
// Append a bytecode instruction to the end of the chunk.
// If there is no capacity left, we grow the capacity and append.
// The line in source code where the bytecode is found is also provided.
void writeChunk(Chunk* chunk, uint8_t byte, int line);
// Add a constant to the dynamic array.
// Return value is the index where constant was appended.
int addConstant(Chunk* chunk, Value value);

#endif