// compiler.h
// Declaration classes and methods required by the compiler.

#ifndef clox_compiler_h
#define clox_compiler_h

// Compile the given source code into bytecode.
// The source code is passed through the scanner and then the compiler and finally the Virtual Machine.
void compile(const char* source);

#endif