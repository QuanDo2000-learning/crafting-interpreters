# Statements and State

In this part, we will implement internal states for our interpreter.
This allows the interpreter to remember the variables and reuse them.
State and statements go hand in hand. Statements, by definition, don't evaluate to a value so their purpose lies in their **side effects**.
In this chapter, we will define statements that product output (`print`) and create a state (`var`). We also add expressions to access and assign values to those states. And finally, we will add blocks and local scope.

## Statements

There are two kinds of statements.

1. An **expression statement** lets you place an expression where a statement is expected. These statements exist to evaluate expresssions that have side effects.
2. A **`print` statement** evaluates an expression and displays the result to the user.

We also need new grammar rules to support the syntax.

```BNF
program        → statement* EOF ;

statement      → exprStmt
               | printStmt ;

exprStmt       → expression ";" ;
printStmt      → "print" expression ";" ;
```

### Statement syntax trees

A new class will be created for statements.

### Parsing statements

Here we change our previous `parse()` method to actually parse the statements.

### Executing statements

We modify the Interpreter class and Lox class to allow our interpreter to execute the statements.

## Global Variables

Two new constructs are required for global variables.

1. A **variable declaration** to create a new binding between a name and a value.
2. A **variable expression** to access the binding that was created.

### Variable syntax

First, we define the syntax, grammar for variable declaration.
This means creating another level of precedence to restrict where some kind of statements are allowed.

```BNF
program        -> declaration* EOF ;

declaration    -> varDecl
               | statement ;

statement      -> exprStmt
               | printStmt ;
```

Then we have the rule to declare a variable.

```BNF
varDecl        -> "var" IDENTIFIER ( "=" expression )? ";" ;
```

To access a variable, we need to modify our previous primary expression.

```BNF
primary        -> "true" | "false" | "nil"
               | NUMBER | STRING
               | "(" expression ")"
               | IDENTIFIER ;
```

After all the grammar is done, we add that to our syntax trees.

### Parsing variables

Some modification is needed to make room for our new `declaration` rule in the grammar.
Here is also where we put our error infrastructure to use for error recovery.

## Environments

In this section, we will create a class to store the bindings that associate variables to values.
This is usually called the **environment**.
For our specific implementation, we will use **maps** or **hashmaps** in Java.

### Interpreting global variables

Here, we create our environment to store global variables.

## Assignment

Being an imperative language, we will implement assignment to variables, or the ability to **mutate** variables.

### Assignment syntax

First, we define the grammar rules for assignment.

```BNF
expression     -> assignment ;
assignment     -> IDENTIFIER "=" assignment
               | equality ;
```

Then, add in code to support these rules.

### Assignment semantics

Let's add in code for a new visit method.
We also differentiate between assignment and definition.
In our implementation, assignment is not allowed to create _new_ variables.

## Scope

A **scope** defines a region where a name maps to a certain entity.
Multiple scopes allows the same name to refer to different things in different context.

**Lexical scope** (or **static scope**) is a style of scoping where the text of the program shows where a scope begins and ends.
This is opposite to **dynamic scope** where we don't know what a name refers to until the code is executed.
Lox doesn't have dynamically scoped _variables_, but methods and fields on objects are dynamically scoped.

Scope and environment closely relates to one another. Scope is a theoretical concept while environment is the machinery that implements it.
In Lox, we will use **block scope**, controlled by curly-braces.

### Nesting and shadowing

**Shadowing** is when a local variable has the same name as a variable in an enclosing scope. Here, the local variable **shadows** the outer one.
The code inside the block can't see the outer variable because it is hidden in the "shadow" of the inner variable.

It is also important to handle enclosing variables that are _not_ shadowed. The interpreter must find variables not only in the current innermost environment but also any enclosing ones.

This will be implemented by nesting the environments together. The interpreter will look at the innermost environment and move outwards until the variable is found.

### Block syntax and semantics

We add block syntax to the grammar and code to support them.

```BNF
statement      -> exprStmt
               | printStmt
               | block ;

block          -> "{" declaration* "}" ;
```

## Challenges

1. The REPL no longer supports entering a single expression and automatically printing its result value. That’s a drag. Add support to the REPL to let users type in both statements and expressions. If they enter a statement, execute it. If they enter an expression, evaluate it and display the result value.

2. Maybe you want Lox to be a little more explicit about variable initialization. Instead of implicitly initializing variables to `nil`, make it a runtime error to access a variable that has not been initialized or assigned to, as in:

   ```lox
   // No initializers.
   var a;
   var b;

   a = "assigned";
   print a; // OK, was assigned first.

   print b; // Error!
   ```

3. What does the following program do?

   ```lox
   var a = 1;
   {
     var a = a + 2;
     print a;
   }
   ```

   What did you _expect_ it to do? Is it what you think it should do? What does analogous code in other languages you are familiar with do? What do you think users will expect this to do?
