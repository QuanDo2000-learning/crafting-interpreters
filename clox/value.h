// value.h
// Contains declarations for value representations.

#ifndef clox_value_h
#define clox_value_h

#include "common.h"

typedef double Value;

// A dynamic array storing values.
typedef struct {
  int capacity;
  int count;
  Value* values;
} ValueArray;

// Initialize a new empty dynamic array storing values.
void initValueArray(ValueArray* array);
// Append a value to the dynamic array.
void writeValueArray(ValueArray* array, Value value);
// Free the memory for the dynamic array.
void freeValueArray(ValueArray* array);
void printValue(Value value);

#endif