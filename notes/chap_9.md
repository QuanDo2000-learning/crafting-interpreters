# Control Flow

In this chapter, our interpreter takes a big step towards the programming language major leagues: _Turing-completeness_.

## Turing Machines (Briefly)

Alan Turing and Alonzo Church each crafted a tiny system with a minimum set of machinery that is powerful enough to compute any of a (very) large class of functions. These functions are called "computable functions".
Turing's system is called a **Turing machine** and Church's is the **lambda calculus**.

The language is considered **Turing-complete** is it is expressive enough to simulate a Turing machine.
All you need is some basic arithmetic, a little control flow, and the ability to allocate and use (theoretically) arbitrary amounts of memory.

## Conditional Execution

Control flow can be divided into two kinds.

- **Conditional** or **branching control flow** is used to _not_ execute some piece of code.
- **Looping control flow** executes a chunk of code more than once.

First, let's add conditional `if` statement into the grammar.

```BNF
statement      -> exprStmt
               | ifStmt
               | printStmt
               | block ;

ifStmt         -> "if" "(" expression ")" statement
               ( "else" statement )? ;
```

Here, we solve the dangling else problem by associating it with the nearest `if` that precedes it.

## Logical Operators

We implement operators `and` and `or`. These operators aren't like the other binary operators because they **short-circuit**.
This means that if the left operand gives us the result for the logical expression, the right operand is not evaluated.
We first add them to our grammar.

```BNF
expression     -> assignment ;
assignment     -> IDENTIFIER "=" assignment
               | logic_or ;
logic_or       -> logic_and ( "or" logic_and )* ;
logic_and      -> equality ( "and" equality )* ;
```

## While Loops

There will be two looping control flow statements `while` and `for`.

```BNF
statement      -> exprStmt
               | ifStmt
               | printStmt
               | whileStmt
               | block ;

whileStmt      -> "while" "(" expression ")" statement ;
```

## For Loops

Grammar for the syntax.

```BNF
statement      -> exprStmt
               | forStmt
               | ifStmt
               | printStmt
               | whileStmt
               | block ;

forStmt        -> "for" "(" ( varDecl | exprStmt | ";" )
                 expression? ";"
                 expression? ")" statement ;
```

### Desugaring

We can argue that Lox doesn't need `for` loops and that would be true. But adding in a `for` loop will make some common code patterns easier to write.
These features are called **syntactic sugar**.
In this context, **desugaring** means taking the code using syntax sugar and translates it to a more primitive form.

## Challenges

**1. A few chapters from now, when Lox supports first-class functions and dynamic dispatch, we technically won’t _need_ branching statements built into the language. Show how conditional execution can be implemented in terms of those. Name a language that uses this technique for its control flow.**

**2. Likewise, looping can be implemented using those same tools, provided our interpreter supports an important optimization. What is it, and why is it necessary? Name a language that uses this technique for iteration.**

**3. Unlike Lox, most other C-style languages also support `break` and `continue` statements inside loops. Add support for `break` statements.**
**The syntax is a `break` keyword followed by a semicolon. It should be a syntax error to have a `break` statement appear outside of any enclosing loop. At runtime, a `break` statement causes execution to jump to the end of the nearest enclosing loop and proceeds from there. Note that the `break` may be nested inside other blocks and `if` statements that also need to be exited.**
