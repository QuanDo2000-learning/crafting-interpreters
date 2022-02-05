// memory.h
// Define low-level memory operations.

#ifndef clox_memory_h
#define clox_memory_h

#include "common.h"
#include "object.h"

#define ALLOCATE(type, count) \
  (type*)reallocate(NULL, 0, sizeof(type) * (count))

#define FREE(type, pointer) reallocate(pointer, sizeof(type), 0)

// Calculates the new capacity by a factor of 2.
#define GROW_CAPACITY(capacity) \
  ((capacity) < 8 ? 8 : (capacity)*2)

// Grow an array pointed to by pointer from oldCount to newCount.
#define GROW_ARRAY(type, pointer, oldCount, newCount)   \
  (type*)reallocate(pointer, sizeof(type) * (oldCount), \
                    sizeof(type) * (newCount))

// Free the memory in the array pointed to.
#define FREE_ARRAY(type, pointer, oldCount) \
  reallocate(pointer, sizeof(type) * (oldCount), 0)

// Reallocate array pointed to by pointer from oldSize to newSize.
// Function is used for all dynamic memory management in clox.
//
// oldSize | newSize | Operation.
// 0 | Non-zero | Allocate new block.
// Non-zero | 0 | Free allocation.
// Non-zero | < oldSize | Shrink allocation.
// Non-zero | > oldSize | Grow allocation.
void* reallocate(void* pointer, size_t oldSize, size_t newSize);
// Free all objects from memory.
void freeObjects();

#endif