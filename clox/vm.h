// vm.h
// Declare structures and methods for the virtual machine.

#ifndef clox_vm_h
#define clox_vm_h

#include "chunk.h"
#include "object.h"
#include "table.h"
#include "value.h"

#define FRAMES_MAX 64
#define STACK_MAX (FRAMES_MAX * UINT8_COUNT)

// Data structure to keep track of a function's call frame.
typedef struct {
  ObjFunction* function;
  uint8_t* ip;   // Instruction pointer of current function
  Value* slots;  // Pointer to first value slot of the function
} CallFrame;

// Data structure to keep track of the VM's state.
typedef struct {
  CallFrame frames[FRAMES_MAX];  // Stack of function calls
  int frameCount;

  Value stack[STACK_MAX];  // Value stack implemented in C-array
  Value* stackTop;         // Pointer to the top of the stack, past the last appended element.
  Table globals;           // Store all global variables.
  Table strings;           // Store all the "intern" strings.
  Obj* objects;            // Pointer to the head of the object list
} VM;

// Enumerator for all return values after interpret.
typedef enum {
  INTERPRET_OK,
  INTERPRET_COMPILE_ERROR,
  INTERPRET_RUNTIME_ERROR
} InterpretResult;

extern VM vm;

void initVM();
void freeVM();
// Interpret a given chunk of bytecode.
InterpretResult interpret(const char* source);
// Push a value onto the value stack.
void push(Value value);
// Pop a value off the value stack.
Value pop();

#endif