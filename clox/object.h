// object.h
// Declare structs and functions to represent an Object in Lox.

#ifndef clox_object_h
#define clox_object_h

#include "common.h"
#include "value.h"

// Get the type of the object given.
#define OBJ_TYPE(value) (AS_OBJ(value)->type)

// Check if the given object is a string.
#define IS_STRING(value) isObjType(value, OBJ_STRING)

// Convert the value into the string object pointer.
#define AS_STRING(value) ((ObjString*)AS_OBJ(value))
#define AS_CSTRING(value) (((ObjString*)AS_OBJ(value))->chars)

// Enumerator for the different types of object.
typedef enum {
  OBJ_STRING,
} ObjType;

struct Obj {
  ObjType type;
  struct Obj* next;
};

struct ObjString {
  Obj obj;
  int length;
  char* chars;
  uint32_t hash;
};

ObjString* takeString(char* chars, int length);
ObjString* copyString(const char* chars, int length);
void printObject(Value value);

static inline bool isObjType(Value value, ObjType type) {
  return IS_OBJ(value) && AS_OBJ(value)->type == type;
}

#endif