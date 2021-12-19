# Scanning

## The Interpreter Framework

Lox is a scripting language, which means it executes directly from source. Our interpreter will have two ways of running code.
jlox can be started from the command line given a file path and it executes the file.
jlox can also be run interactively by running it without arguments.
An interactive prompt is also called "REPL" for Read Evaluate Print Loop.

### Error handling

It is vital in practical programs to handle errors gracefully.
Our error reporting will at least point them to the line where it occurred.
It is good engineering practice to separate the code that _generates_ the errors from the code that _reports_ the errors.

## Lexemes and Tokens

Lexical analysis is scanning through the list of characters and group them into smallest sequences that still represent something. Each of the group of characters is called **lexeme**.
Lexeme are only raw substrings of the source code but once we add in other information during the scanning process, we get a token.

### Token type

It it necessary to define the types of tokens that the parser can recognize. A type is needed for each keyword, operator, bit of punctuation, and literal type.

### Literal value

These are lexemes for literal values produced by the scanner.

### Location information

The location where the lexeme is needs to be stored.

## Regular Languages and Expressions
