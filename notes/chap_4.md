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

The core of a scanner is a loop. Starting at the first character, the scanner finds out what lexeme the character belongs to then comsumes any following characters that are part of that lexeme. When the scanner reaches the end, a token is emitted. The scanner then loops back and starts again.
The part where we look at characters to determine what kind of lexeme is similar to regular expressions.
The rules that determine how a particular language groups characters into lexemes are called its **lexical grammar**. Lox, and most other programming languages, have rules simple enough to be classified as [regular language](https://en.wikipedia.org/wiki/Regular_language).

## The Scanner Class

Loops through the input source code and produce a list of tokens.

## Recognizing Lexemes

First, we will start with recognizing lexemes with single characters.

### Lexical errors

During scanning, the source code might contain characters unknown to Lox, like `@#^`. We need to report an error.
Note that the characters are still consumed by `advance()`.
Note that the scanner continues to detect other errors later in the program.

### Operators

The scanner now needs to determine the difference between `!` and `!=`.

## Longer Lexemes

The first form of long lexemes we deal with is comment. Here we will also deal with the `/` operator.
We will deal with longer lexemes by moving to lexeme-specific code after detecting the beginning of one lexeme.

### String literals

Next, we detect string tokens starting from `"` and ending with another `"`.

### Number literals

All numbers in Lox are floating point at runtime, but both integer and decimal literals are supported.
Leading or trailing decimal points are not supported.

## Reserved Words and Identifiers

Here, to parse reserved words, we use an important principle called **maximal munch**. This is when we pick the grammar rule that matches the most characters from a chunk of code.
The term **reserved word** refers to an identifier that has been claimed by the language for its own use.

## Challenges

1. The lexical grammars of Python and Haskell are not _regular_. What does that mean, and why aren't they?

   > One reason why Python lexical grammar is not _regular_ is because it uses indentation as tokens and that can't be done using regular grammar.  
   > The lexical grammar not _regular_ means that some tokens can't be recognized just by grammar rules but it has to depend on other factors.  
   > One reason why Haskell's lexical grammar is not _regular_ is because block comments are parsed differently when nesting.

2. Aside from separating tokens - distinguishing `print foo` from `printfoo` - spaces aren't used for much in most languages. However, in a couple of dark corners, a space _does_ affect how code is parsed in CoffeeScript, Ruby, and the C preprocessor. Where and what effect does it have in each of those languages?

   > In [this](https://stackoverflow.com/questions/16314251/coffeescript-issue-with-space) example of CoffeeScript, the space will affect how the code is parsed and the resulting code will be different.  
   > Similarly, Ruby also has [this](https://www.quora.com/Does-whitespace-matter-in-the-Ruby-language) issue where whitespace will matter in some specific circumstances.  
   > For C, [here](https://stackoverflow.com/questions/14763892/whitespace-in-c11-more-than-preprocessing-token-separation) are some cases when whitespace will have an impact on the resulting tokens.

3. Our scanner here, like most, discards comments and whitespace since those aren't needed by the parser. Why might you want to write a scanner that does _not_ discard those? What would it be useful for?

   > Some examples of a scanner that does not discard whitespace or comments are the formatters and linters used in VSCode for example. We want to scan the source code to format our code but do not want to modify the comments or whitespace originally in the code.

4. Add support to Lox's scanner for C-style `/* ... */` block comments. Make sure to handle newlines in them. Consider allowing them to nest. Is adding support for nesting more work than you expected? Why?

   > I have added block comments support but have yet to add support for nesting.  
   > Adding support for nesting would require the use of a stack to store the occurences of the block comments to determine which level of the comment we are at. Either that, or another implementation might be a recursive function to consume the block comments.  
   > I have opted to use a recursive function to consume the block comments and deal with nesting.
