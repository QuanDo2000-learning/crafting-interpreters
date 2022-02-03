// main.h
// Main function.

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "chunk.h"
#include "common.h"
#include "debug.h"
#include "vm.h"

// Run the interpreter in REPL mode.
static void repl() {
  char line[1024];
  for (;;) {
    printf("> ");

    if (!fgets(line, sizeof(line), stdin)) {
      printf("\n");
      break;
    }

    interpret(line);
  }
}

// Read the specified file and load them into the buffer.
static char* readFile(const char* path) {
  FILE* file = fopen(path, "rb");
  // Report an error and exit in case the file can't be read.
  if (file == NULL) {
    fprintf(stderr, "Could not open file \"%s\".\n", path);
    exit(74);
  }

  // Seek to the end of the file to get the fileSize.
  fseek(file, 0L, SEEK_END);
  size_t fileSize = ftell(file);
  // Set the file pointer back at the beginning to load the file into memory.
  rewind(file);

  // Allocates the buffer with the size obtained previously.
  char* buffer = (char*)malloc(fileSize + 1);
  // Report an error in case we don't have enough memory to load the file.
  if (buffer == NULL) {
    fprintf(stderr, "Not enough memory to read \"%s\".\n", path);
    exit(74);
  }

  // Load the contents of the file into the buffer.
  size_t bytesRead = fread(buffer, sizeof(char), fileSize, file);
  // Report an error when the file was not read fully.
  if (bytesRead < fileSize) {
    fprintf(stderr, "Could not read file \"%s\".\n", path);
    exit(74);
  }

  // Added a null character at the end of the buffer.
  buffer[bytesRead] = '\0';

  fclose(file);
  return buffer;
}

// Parse the input file and execute the code.
static void runFile(const char* path) {
  char* source = readFile(path);
  InterpretResult result = interpret(source);
  free(source);

  if (result == INTERPRET_COMPILE_ERROR) exit(65);
  if (result == INTERPRET_RUNTIME_ERROR) exit(70);
}

int main(int argc, const char* argv[]) {
  initVM();

  if (argc == 1) {
    repl();
  } else if (argc == 2) {
    runFile(argv[1]);
  } else {
    fprintf(stderr, "Usage: clox [path]\n");
    exit(64);
  }

  freeVM();
  return 0;
}