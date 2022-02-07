// compiler.h
// Declaration classes and methods required by the compiler.

#ifndef clox_compiler_h
#define clox_compiler_h

#include "object.h"
#include "vm.h"

// Compile the given source code into bytecode.
// Returns a function.
ObjFunction* compile(const char* source);
void markCompilerRoots();

#endif