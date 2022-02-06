// object.h
// Declare structs and functions to represent an Object in Lox.

#ifndef clox_object_h
#define clox_object_h

#include "chunk.h"
#include "common.h"
#include "value.h"

// Get the type of the object given.
#define OBJ_TYPE(value) (AS_OBJ(value)->type)

// Check if the given value is the correct object.
#define IS_FUNCTION(value) isObjType(value, OBJ_FUNCTION)
#define IS_NATIVE(value) isObjType(value, OBJ_NATIVE)
#define IS_STRING(value) isObjType(value, OBJ_STRING)

// Convert the value into different objects.
#define AS_FUNCTION(value) ((ObjFunction*)AS_OBJ(value))
#define AS_NATIVE(value) \
  (((ObjNative*)AS_OBJ(value))->function)
#define AS_STRING(value) ((ObjString*)AS_OBJ(value))
#define AS_CSTRING(value) (((ObjString*)AS_OBJ(value))->chars)

// Enumerator for the different types of object.
typedef enum {
  OBJ_FUNCTION,
  OBJ_NATIVE,
  OBJ_STRING,
} ObjType;

struct Obj {
  ObjType type;
  struct Obj* next;
};

// Data structure for a function in Lox.
typedef struct {
  Obj obj;
  int arity;
  Chunk chunk;
  ObjString* name;
} ObjFunction;

typedef Value (*NativeFn)(int argCount, Value* args);

// Structure to represent a native function in Lox.
typedef struct {
  Obj obj;
  NativeFn function;
} ObjNative;

// Structure for a string in Lox.
struct ObjString {
  Obj obj;
  int length;
  char* chars;
  uint32_t hash;
};

ObjFunction* newFunction();
ObjNative* newNative(NativeFn function);
ObjString* takeString(char* chars, int length);
ObjString* copyString(const char* chars, int length);
void printObject(Value value);

static inline bool isObjType(Value value, ObjType type) {
  return IS_OBJ(value) && AS_OBJ(value)->type == type;
}

#endif