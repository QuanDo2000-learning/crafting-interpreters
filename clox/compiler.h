// compiler.h
// Declaration classes and methods required by the compiler.

#ifndef clox_compiler_h
#define clox_compiler_h

#include "object.h"
#include "vm.h"

// Compile the given source code into bytecode.
// The bytecode is placed into the provided chunk.
// Return value shows whether compilation finished successfully.
bool compile(const char* source, Chunk* chunk);

#endif