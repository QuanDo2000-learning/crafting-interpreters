// debug.h
// Contains the disassembler and debugging declarations.

#ifndef clox_debug_h
#define clox_debug_h

#include "chunk.h"

// Disassemble all the instructions in the given chunk.
void disassembleChunk(Chunk* chunk, const char* name);
// Disassemble a single instruction in the chunk.
int disassembleInstruction(Chunk* chunk, int offset);

#endif