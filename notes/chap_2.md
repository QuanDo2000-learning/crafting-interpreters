# A Map of the Territory

## The Parts of a Language

Journey to explore the language. Start off at the bottom with raw text files at the bottom then we slowly gain meaning as we go up the mountain. At the top, we get a view of what the code is meant to do.

### Scanning

**Scanning**, or **lexing**, or lexical analysis is the process of translating the linear stream of characters and group them together into a series of "word" (tokens).
**Tokens** can be single characters like `,.` or several characters like `123`, `hi!`, or `min`.
Whitespace and comments are usually ignored by the language and is not included in the tokens.

### Parsing

**Parsing** is the process where the tokens are translated using grammar to compose larger expressions and statements.
A parser takes a flat sequence of tokens and builds a tree structure that mirrors the nested nature of the grammar. They are called **parse trees** or **abstract syntax trees** (ASTs) or sometimes just trees.
One of the usage of the parser is reporting **syntax errors**.

### Static analysis

The first two steps scanning and parsing are similar accross all implementations but each language will have their own method for analysis.
The first part of analysis that most languages do is called **binding** or **resolution**.
For each **identifier** (variable), we find where the name is definedd and connect the two together. This is where **scope** gets defined.
If the language is statically typed, this is when we type check. If the types don't support being added to each other, then we report a **type error**.
The language we'll build is dynamically typed so the checking will be done at runtime.
The results from the analysis will be stored in various places

- Sometimes it is stored back into the syntax tree as attributes.
- Sometimes a lookup table is created where the key is the identifiers - names of variables and declarations. It is called a **symbol table** and the value associated with the key tells us what the identifier refers to.
- The most powerful bookkeeping tool is to transform the tree into a new data structure. This is covered in the next section.

Up until now, we have covered the **front end** of the implementation. We will now go to the **middle end** and finally the **back end**.

### Intermediate representations

The compiler can be thought of as a pipeline where each stage's job is to organize the data to make the next stages easier to implement.
The front end is specific to the source languague the program is written in. The back end is concerned with the final architecture where the program runs.
In the middle, the code is stored in some **intermediate representation** (**IR**) that isn't tied to either the source or the destination forms. The IR acts as the interface between the two.
IR allows us to support multiple source languages on multiple platforms without having to write a separate implementation for each combination.
Some styles of IR are "control flow graph", "static single-assignment", "continuation-passing style", and "three-address code".

### Optimization

After getting an understanding of what the program is doing, we can optimize the program.
One example of optimization is **constant folding**, where expressions that always evaluate to the same value is replaced with its results at compile time.
Keywords for more information about optimization "constant propagation", "common subexpression elimination", "loop invariant code motion", "global value numbering", "strength reduction", "scalar replacement of aggregates", "dead code elimination", and "loop unrolling".

### Code generation

After applying all optimizations to the user's program, the last step is converting it to machine code, **generating code** (or **code gen**), where "code" refers to the primitive assembly-like instructions a CPU runs and not "source code" a human can read.
Here is the **back end**, where the representation of the code becomes more and more primitive, getting closer to something the machine can understand.
We have to decide whether to generate instruction for a real CPU or a virtual one.
Real machine code will be very fast at the cost of being complicated to do and not portable.
A virtual machine code, also called **p-code** by Wirth, or generally called **bytecode** will be much more portable and can be used on multiple different architectures.

### Virtual machine

If our compiler generates bytecode, we also have to think about how to run the bytecode on multiple machines, whether to create a mini-compiler for each architecture or a **virtual machine** (**VM**).
A VM is a program that emulates a hypothetical chip that supports your virtual architecture at runtime. Running bytecode in a VM will be slower than translating to native code but it is made up by simplicity and portability.
This is how we will build our second interpreter in this book.

### Runtime

This is when we run our program, either by the operating system or the VM.
There are some services that might be running during runtime: garbage collector to automatically manage memory, debugger to keep track of objects during execution, ...

## Shortcuts and Alternative Routes

All the steps above is the long path that covers all possible phase that we might implement. Some languages take this path but there are some that uses shortcuts and alternative paths.

### Single-pass compilers

These compilers combine parsing, analysis, and code generation into one step without having any intermediate data structures. This type of compiler will restrict the design of the language. Pascal and C are some languages that is designed with this restriction.
[Syntax-directed translation](https://en.wikipedia.org/wiki/Syntax-directed_translation) is a structured language for building these types of compilers. An _action_ is associated with a piece of grammar and the compiler will build up the program each chunk of syntax at a time.

### Tree-walk interpreters

These interpreters execute the code right after parsing code into AST. The interpreter traverses the syntax tree each branch and leaf at a time and evaluating each node as it goes.
This is how our first interpreter will be implemented.

### Transpilers

**Transpilers**, or source-to-source compiler, or transcompiler, are translating the language to another language and using the compiler for that language to do the back end for you.
Today, most transpilers work on higher-level languages. C compilers are available on almost all architectures so targeting C is a good choice.
Another wide spread transpilers is the web browsers and the "machine code" JavaScript. [Almost all language](https://github.com/jashkenas/coffeescript/wiki/list-of-languages-that-compile-to-js) has a compiler that targets JS to get code running in a browser.

### Just-in-time compilation

**Just-in-time compilation** (**JIT**) is the method in which we compile program to native code for the architecture when the program is loaded.
Some common JIT are HotSpot Java Virtual Machine (JVM) or Microsoft's Common Language Runtime (CLR).
More sophisticated JITs insert profiling hooks into the generated code to see which regions are performance critical and the data flowing through them. Those regions will be recompile automatically over time with more advanced optimizations.

## Compilers and Interpreters

The difference between compilers and interpreters are like comparing fruit and vegetable. Fruit is a _botanical_ term and vegetable is _culinary_. There are fruits that aren't vegetables (apples) and vegetables that aren't fruit (carrots), and plants that are both fruits and vegetables (tomatoes) or neither (peanuts).
**Compiling** is an _implementation technique_ that involves translating a source language to another form. **Compilers** translates source code into some other form but doesn't execute it.
**Interpreter**, on the other hand, takes in source code and executes it immediately.
The second interpreter we will implement will be within the overlapping region between compilers and interpreters.

## Challenges

1. Pick an open source implementation of a language you like. Download the source code and poke around in it. Try to find the code that implements the scanner and parser. Are they handwritten, or generated using tools like Lex and Yacc? (`.l` or `.y` files usually imply the latter.)

   > The open source language is [Python](https://github.com/python/cpython). It seems that parser and scanner are in [Parser](https://github.com/python/cpython/tree/main/Parser). The parser and scanner for Python is handwritten in C and not using tools.

2. Just-in-time compilation tends to be the fastest way to implement dynamically typed languages, but not all of them use it. What reasons are there to `not` JIT?

   > Despite the having the speed of compiled code and flexibility of interpretation, JIT has the overhead of an interpreter and compiling and linking. Therefore, some dynamically typed languages might not want to use JIT because of the large overhead in doing interpreting, compiling, and linking.

3. Most Lisp implementation that compile to C also contain an interpreter that lets them execute Lisp code on the fly as well. Why?

   > Since Lisp is a dynamically typed language, the program can be changed at runtime. Having an interpreter for Lisp helps debugging the Lisp code dynamically. On the other hand, after finishing with debugging, the compiler exists to compile the code and make the program faster.
