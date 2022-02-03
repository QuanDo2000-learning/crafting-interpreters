// memory.c
// Define functions to manage memory.

#include "memory.h"

#include <stdlib.h>

void* reallocate(void* pointer, size_t oldSize, size_t newSize) {
  if (newSize == 0) {
    free(pointer);
    return NULL;
  }

  void* result = realloc(pointer, newSize);
  // NULL means there isn't enough memory.
  if (result == NULL) exit(1);
  return result;
}