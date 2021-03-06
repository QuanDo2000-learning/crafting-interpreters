# Introduction

## Notes

- There are 2 implementations of interpreters
- There is general-purpose language, and there are "little languages" or "domain-specific languages"
  - Exampe
    - Make
    - YAML
    - Emacs Lisp
    - cpp
    - INI
    - JSON
    - Vim Script
    - Bash
    - Batch
    - CSS
    - HTML
    - XML
    - SQL
    - Tcl
    - ASP.NET
- There are tools like [Yacc](https://en.wikipedia.org/wiki/Yacc) or Bison (compiler-compiler) to generate source files for an implementation
- First interpreter is writen in Java, the second is written in C

## Challenges

1. There are at least six domain-specific languages used in the [little system I cobbled together](https://github.com/munificent/craftinginterpreters) to write and publish this book. What are they?

   > The languages are:
   >
   > - HTML
   > - CSS
   > - SCSS
   > - Markdown
   > - XML
   > - Makefile
   > - Dart

2. Get a "Hello, world!" program written and running in Java. Set up whatever makefiles or IDE projects you need to get it working. If you have a debugger, get comfortable with it and step through your program as it runs.

   > The environment will be VSCode with Java development kit installed.  
   > This [link](https://code.visualstudio.com/docs/languages/java) can be followed to install Java in VSCode.

3. Do the same thing for C. To get some practice with pointers, define a [doubly linked list](https://en.wikipedia.org/wiki/Doubly_linked_list) of heap-allocated strings. Write functions to insert, find, and delete items from it. Test them.

   > The files for this challenge is saved in ./challenges/1-3  
   > Run by `gcc main.c -o main` then `./main.exe` (for Windows) or `./main` (for Linux)
