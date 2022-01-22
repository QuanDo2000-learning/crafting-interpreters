# Functions

This chapter is where we add support for real user-defined functions and function calls.

## Function Calls

The **callee** can be any expression that evaluates to a function.
A new grammar rule is created for functions.

```BNF
unary          -> ( "!" | "-" ) unary | call ;
call           -> primary ( "(" arguments? ")" )* ;

arguments      -> expression ( "," expression )* ;
```

### Maximum argument counts

For our implementation, we will limit the size of our arguments to 254.
This also makes our jlox interpreter compatible with the bytecode interpreter.

### Interpreting function calls

Here we implement function calls

### Call type errors

Check for type errors when the callee is not callable.

### Checking arity

**Arity** is the fancy term for the number of arguments a function or operation expects.
We check whether if the number of arguments in the function call matches the function's arity.

## Native Functions

**Native functions** are functions that the interpreter exposes to the user code but implemented in the host language (Java, not Lox).
They are also called **primitives**, **external functions**, or **foreign functions**.
Native functions are key in making the language good at doing useful stuff. They provide fundamental services that all programs are defined in terms of.

Many languages also allow users to provide their own native functions. The mechanism for that is called a **foreign function interface** (**FFI**), **native extension**, **native interface**, or something similar.

### Telling time

In Part III, performance will be one of our main concerns which requires measurement, or **benchmarks**.
This is where we define the function `clock()` to return the number of seconds that have passes since some point in time.

## Function Declarations

New grammar rule for function declarations.

```BNF
declaration    -> funDecl
               | varDecl
               | statement ;

funDecl        -> "fun" function ;
function       -> IDENTIFIER "(" parameters? ")" block ;

parameters     -> IDENTIFIER ( "," IDENTIFIER )* ;
```

## Function Objects

We will create a new class for Lox functions so that the runtime phase of the interpreter doesn't get mixed up with the front end's syntax classes (Stmt.Function).

### Interpreting function declarations

Here we allow our interpreter to interpret function declarations.

## Return Statements

Now we deal with the implementation of the `return` statements.

```BNF
statement      -> exprStmt
               | forStmt
               | ifStmt
               | printStmt
               | returnStmt
               | whileStmt
               | block ;

returnStmt     -> "return" expression? ";" ;
```

### Returning from calls

When executing the `return` statement, the interpreter needs to jump out of the current environment and cause the function call to complete.
To do this, we can make use of exceptions to unwind the interpreter past the visit methods back to the code that began executing the body.

## Local Functions and Closures

Currently, the _parent_ of the functions environment is always `globals`, the top-level global environment.
But in Lox, function declarations are allowed _anywhere_, we can declare functions inside blocks or other functions.
That way, we can declare **local functions** inside another function or nested inside a block.
One important data structure is **closure** where it "closes over" and holds on to the surrounding variables where the function is declared. Note that the environment held by closure should be a snapshot of the environment the moment the function is declared.

## Challenges

**1. Our interpreter carefully checks that the number of arguments passed to a function matches the number of parameters it expects. Since this check is done at runtime on every call, it has a performance cost. Smalltalk implementations don’t have that problem. Why not?**

**2. Lox’s function declaration syntax performs two independent operations. It creates a function and also binds it to a name. This improves usability for the common case where you do want to associate a name with the function. But in functional-styled code, you often want to create a function to immediately pass it to some other function or return it. In that case, it doesn’t need a name.**
**Languages that encourage a functional style usually support anonymous functions or lambdas—an expression syntax that creates a function without binding it to a name. Add anonymous function syntax to Lox so that this works:**

```jlox
fun thrice(fn) {
for (var i = 1; i <= 3; i = i + 1) {
fn(i);
}
}

thrice(fun (a) {
print a;
});
// "1".
// "2".
// "3".
```

**How do you handle the tricky case of an anonymous function expression occurring in an expression statement:**

```jlox
fun () {};
```

**3. Is this program valid?**

```jlox
fun scope(a) {
var a = "local";
}
```

**In other words, are a function’s parameters in the _same scope_ as its local variables, or in an outer scope? What does Lox do? What about other languages you are familiar with? What do you think a language _should_ do?**
