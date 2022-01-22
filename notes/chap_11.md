# Resolving and Binding

In this chapter, we patch up the hole in our interpreter from adding in closures.
From this, we can gain a better understanding of lexical scoping done by Lox and other languages in the C tradition.

## Static Scope

The precise scope rule is: **A variable usage refers to the preceding declaration with the same name in the innermost scope that encloses the expression where the variable is used.**

- "Variable usage" is used instead of "variable expression" to cover both variable expressions and assignments.
- "Preceding" means appearing before _in the program text_.
- "Innermost" is there because of shadowing. There may be more than one variable with the given in enclosing scopes.

### Scopes and mutable environments

When talking about scope, we also have to take into account the fact that the scope can change in different moments in time.
In our implementation, environments for the entire block is a scope, but one that changes over time.
Closures do not like that. When a function is declared, it _should_ capture a frozen snapshot of the environment _the moment the function is declared_.
Instead, our current implementation references the mutable environment object which we don't want.

### Persistent environments

There is a style of programming that uses **persistent data structures**. Persistent data structures can never be directly modified.
Any modification to such a structure will produce a brand new object with all the data in the original structure and the new modification. The original structure is unchanged.
By applying this feature in our Environment, everytime a new variable is declared, a new Environment is created. This way, the closure will have a reference to the Environment instance at that moment only.

## Semantic Analysis

Currently, each time a variable is accesses, our interpreter **resolves** it every time. This means that our interpreter tracks down which declaration it refers to each and every time the variable is accessed.
A better solution is to only resolve each variable only _once_. By writing a chunk of code that inspects the user's program, finds every variable and finds out which declaration it refers to. This is **semantic analysis**.
A parser does _syntactic analysis_ (telling if a program is grammatically correct) and semantic analysis goes further and figure out what is the meaning of each piece of the program.
What we need to do is ensuring that a variable lookup always walk the _same_ number of links in the environment chain.

### A variable resolution pass

Between the parser producing the syntax tree and the interpreter starts executing it, we will add in code to walk the tree and resolve all the variables.
There are a few points where static analysis like this differs from dynamic execution.

- **There are no side effects.** Print statements won't print anything and calls to native functions won't have any effect.
- **There is no control flow.** Loops are visited once, both branches in `if` statement are visited, and logic operators are not short-circuited.

## A Resolver Class

For our resolver class, only a few kinds of nodes are interesting.

- A block statement introduces a new scope for the statements it contains.
- A function declaration introduces a new scope for its body and binds its parameters in that scope.
- A variable declaration adds a new variable to the current scope.
- Variable and assignment expressions need to have their variables resolved.

Other than that, we just need to implement visit methods to traverse the nodes' subtrees.

### Resolving blocks

We first start with blocks since they create local scopes.
To resolve a block, we begin a new scope, traverses into the statements inside the blocks, then discards the scope.
Our scopes will be implemented using a Java Stack.
When we begin scope, we push onto the stack.
Each scope is a HashMap (or Dictionary) storing variables -> booleans.
The scope stack here is used for local scopes. If we don't find a variable in the stack, we assume its global.
To end the scope, we just pop it out of the stack.

### Resolving variable declarations

Resolving a variable declaration means adding a new entry (key-value pair) onto the topmost scope's map. There are a few steps to do that.
There are a few options when the initializer for a local variable refers to a variable with the same name.

1. **Run the initializer, then put the new variable in scope.**
2. **Put the new variable in scope, then run the initializer.**
3. **Make it an error to reference a variable in its initializer.**

For our implementation, we will choose the third option and make it a compile error. This way, the user will be alerted to the problem before the code is run.
Back to the main issue, the first step is **declaring** the variable.
This is where our boolean value comes in handy. We can use it to know if the variable is ready (finished being resolved).
After that, we resolve the initializer expression in the same scope. Here, we do the check if the variable is not ready using the boolean value associated with the scope. This way we know if the initializer refers to the same variable.
After the initializer expression finished resolving, we know that our variable is ready and we **define** it.
Now we set the boolean value to `true` so it is initialized and available for use.

### Resolving variable expressions

We continue to add code to resolve the variable expressions.
First, we check if the variable exists and avaiable or not.
Then, we resolve the variable by starting from the innermost scope then move outwards to find the variable.
If we find the variable, we resolve it using the interpreter and passing in the number of scopes between the current innermost scope and the scope where we found the variable.
If we don't find the variable, it is left unresolved and we assume it's global.

### Resolving assignment expressions

Now we resolve the assignment expressions.
First, we resolve the assigned value in case it contains references to other variables.
Then, we use the existing resolve function above to resolve the variable being assigned to.

### Resolving function declarations

Now for function declarations.
Functions both bind names and create a scope. The name is in the surrounding scope where the function is declared and a new scope for the function's body.
First, we declare and define the name of the function in the current scope.
Different from variables, we eagerly define the name before resolving the body so the function can recursively refer to itself in its own body.
To resolve the functions body, we create a new scope and binds each parameter variables. Then we resolve the function body in that scope.
Finally, we exit the scope and discard it.

### Resolving the other syntax tree nodes

The above covers all places where varibles are declared, or modified and everywhere a scope is created or destroyed.
Now we will go through the other syntax tree nodes to recurse into their subtrees.

## Interpreting Resolved Variables

Currently, each time our resolver visits a variable, it tells the interpreter how many scopes are there between the current scope and the scope where the variable is defined.
The resolution information will be stored in a map that associates each syntax tree node with its resolved data.

### Accessing a resolved variable

Now, we can make use of each variable's resolved location.
By using the distance, we can jump to the correct environment directly without having to search every step of the way.

### Assigning to a resolved variable

Similar to accessing a variable, we also make the same changes to assigning to a resolved variable.

### Running the resolver

Finally, our resolver is finished. We can now run our resolver but what if there are errors during resolution.

## Resolution Errors

The first error is declaring variables with the same name. Despite allowing it in the global scope, it is an error in local scope.

### Invalid return errors

Just in case a `return` statement is not in a function, we need to detect that and report the error.

We can also add other types of analysis in here like unreachable code or unused variables.

## Challenges

**1. Why is it safe to eagerly define the variable bound to a function’s name when other variables must wait until after they are initialized before they can be used?**

**2. How do other languages you know handle local variables that refer to the same name in their initializer, like:**

```lox
var a = "outer";
{
  var a = a;
}
```

**Is it a runtime error? Compile error? Allowed? Do they treat global variables differently? Do you agree with their choices? Justify your answer.**

**3. Extend the resolver to report an error if a local variable is never used.**

**4. Our resolver calculates _which_ environment the variable is found in, but it’s still looked up by name in that map. A more efficient environment representation would store local variables in an array and look them up by index.**
**Extend the resolver to associate a unique index for each local variable declared in a scope. When resolving a variable access, look up both the scope the variable is in and its index and store that. In the interpreter, use that to quickly access a variable by its index instead of using a map.**
