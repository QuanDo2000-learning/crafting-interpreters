# Parsing Expressions

Here is where we parse the code, tranforming the sequence of tokens into syntax trees.

## Ambiguity and the Parsing Game

A grammar is _ambiguous_ when one string can be generated through different paths of production.
During parsing, this ambiguity means that the parser might misunderstand the user's code.
Other than determining if the string is valid Lox code, we also have to track which rules match which parts so we know what part of the language each token belongs to.
Depending on how the parser is implemented, different resulting _syntax tree_ might be generated based on the same string.

Current grammar rules

```BNF
expression     → literal
               | unary
               | binary
               | grouping ;

literal        → NUMBER | STRING | "true" | "false" | "nil" ;
grouping       → "(" expression ")" ;
unary          → ( "-" | "!" ) expression ;
binary         → expression operator expression ;
operator       → "==" | "!=" | "<" | "<=" | ">" | ">="
               | "+"  | "-"  | "*" | "/" ;
```

In the case of operators, there are a few rules that we use so that our parser arrives at the correct _syntax tree_.

- **Precedence** determines which operator is evaluated first in an expression containing a mixture of different operators. Operators with higher precedence are evaluated before operators with lower precedence. Higher precedence operators are said to "bind tighter".
- **Associativity** determines which operator is evaluates first in a series of the _same_ operator. For example, `-` is **left-associative** while `=` is **right-associative**.

```lox
// Left-associative
5 - 3 - 1
(5 - 3) - 1

// Right-associative
a = b = c
a = (b = c)
```

The current grammar stuffs all expression types into a single `expression` rule. The same rule is used as non-terminal for operands, which lets the grammar accept any kind of expression as a subexpression, regardless of precedence rules.
We need to fix that by stratifying the grammar, which a rule for each precedence level.

```BNF
expression     -> ...
equality       -> ...
comparison     -> ...
term           -> ...
factor         -> ...
unary          -> ...
primary        -> ...
```

When stratifying the grammar, it is important to note the relationship between the grammar rules and the parsing technique.
Some parsing techniques, including the one in this book, will have trouble with **left-recursive** production rules.
An example of a left-recursive rule

```BNF
factor -> factor ( "/" | "*" ) unary
       | unary
```

Finally, we have our complete expression grammar

```BNF
expression     → equality ;
equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary
               | primary ;
primary        → NUMBER | STRING | "true" | "false" | "nil"
               | "(" expression ")" ;
```

## Recursive Descent Parsing

Recursive descent is considered a **top-down parser** because it starts from the top or outermost grammar rule (`expression`) and works its way down into the nested subexpression before reaching the leaves of the syntax tree.

### The parser class

This is where we add each grammar rule as a method inside the new class.

In this part is also where we see why left-recursion is problematic for recursive descent. Since the function for a left-recursive immediately calls itself, it will repeat the recursion until a stack overflow and dies.

Due to how the parser looks ahead at upcoming tokens to decide how to parse place recursive descent into the category of **predictive parser**.

## Syntax Errors

A parser has two jobs

1. Given a valid sequence of tokens, produce a corresponding syntax tree.
2. Given an _invalid_ sequence of tokens, detect any errors and tell the user about it.

There are some requirements when a parser runs into a syntax error.

- **Detect and report the error.**
- **Avoid crashing or hanging.**

A decent parser should also include

- **Be fast.**
- **Report as many distint errors as there are.**
- **Minimize _cascaded_ errors.** Cascaded errors happen when the parser detects a single error but no longer knows whats the current state of the code. It then proceeds to generate ghost errors that will go away once the first error is fixed.

The way a parser responds to errors and keeps going to look for later errors is called **error recovery**.

### Panic mode for error recovery

One recovery technique that stood out is **panic mode**. As soon as the parser detects an error, it enters panic mode where it knows at least one token doesn't make sense in its current state.
Before getting back to parsing, it needs to get its state and the sequence of upcoming tokens aligned such that the next token does match the rule being parsed. This is **synchronization**.
We select some rule in the grammar as the synchronization point and the parser will fixes its state by jumping out of the nested productions until it gets back to that rule. Then the token stream is synchronized by discarding tokens until the parser reaches one that can appear at that point in the rule.

### Entering panic mode

We won't deal with synchronize in this chapter but we will add in code for later.

One more method of dealing with common syntax errors is with **error production**. This is where we augment the grammar with a rule that _successfully_ matches the _erroneous_ syntax. The parser can parse it but still reports it as an error.

### Synchronizing a recursive descent parser

With recursive descent, the parser's state is not stored explicitly in fields but we have to trace the call stack to see what the parser is doing. To reset the state, we need to clear out the call frames.
In Java, the natural way is exceptions. When we want to synchronize, we _throw_ the ParseError object and it will be caught by a grammar rule higher up.
After the exception is caught, the parser is in the right state and all that's left is to synchronize the tokens.
We want to discard tokens until we're at the beginning of the next statement. _After_ a semicolon, the next statement should start with a keyword.

## Wiring up the Parser

Add the Parser to the main Lox class.

## Challenges

**1. In C, a block is a statement form that allows you to pack a series of statements where a single one is expected. The [comma operator](https://en.wikipedia.org/wiki/Comma_operator) is an analogous syntax for expressions. A comma-separated series of expressions can be given where a single expression is expected (except inside a function call’s argument list). At runtime, the comma operator evaluates the left operand and discards the result. Then it evaluates and returns the right operand.**

**Add support for comma expressions. Give them the same precedence and associativity as in C. Write the grammar, and then implement the necessary parsing code.**

**2. Likewise, add support for the C-style conditional or “ternary” operator ?:. What precedence level is allowed between the ? and :? Is the whole operator left-associative or right-associative?**

**3. Add error productions to handle each binary operator appearing without a left-hand operand. In other words, detect a binary operator appearing at the beginning of an expression. Report that as an error, but also parse and discard a right-hand operand with the appropriate precedence.**
