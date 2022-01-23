# Representing Code

Previously, we have transformed the source code into a series of tokens. Next, our parser will take the tokens and transform them into a richer, more complex representation.
Before getting to the complex representation, we need to define it.
Our main goal is finding a representation for code. One way of doing that is by using a tree that matches the grammatical structure of the language.

## Context-Free Grammars

In the previous chapter, _regular expression_ helped us group characters into tokens. Now, we need another tool to handle expressions which is **context-free grammar** (**CFG**). This is a powerful tool in the toolbox of [**formal grammars**](https://en.wikipedia.org/wiki/Formal_grammar).
A formal grammar has a set of atomic pieces called the "alphabet". Then there is a set of "strings" that are "in" the grammar and each string is a sequence of "letters" from the alphabet.
In the scanner's grammar (lexical grammar), the alphabet is the individual characters and the strings are the lexemes. In the syntactic grammar now, the alphabet is the individual tokens and the string is a sequence of tokens.
The job of a formal grammar is deciding which strings are valid in the grammar and which aren't.

### Rules for grammars

We can define a grammar by creating a finite set of rules that determine which strings are valid.
Using the rules, we can _generate_ strings in the grammar. These strings are called **derivations** because each is _derived_ from the rules.
The rules are called **productions** because they _produce_ strings.
Each rule in a context-free grammar has a **head** - the name - and a **body**, the description of what it generates.
The body contains a list of symbols, divided into two types.
A **terminal** is a letter from the grammar's alphabet. It can be thought of as a literal value. They are called "terminals" because they don't lead to any further "moves"
A **nonterminal** will reference another rule in the grammar and composes.
One thing to note is there maybe multiple rules with the same name. If we reach a nonterminal with that name, any option will suffice.
To write down these grammar rules, we have come up with a syntax to define the rules. In this book, the rule will consists of a name, followed by an arrow (->), then a sequence of symbols, and ending with a semicolon (`;`).
A good sign that the language is context-free instead of regular is the presence of recursion in the defined rules.

### Enhancing our notation

Here we define some notations to add extra convenience.
A pipe (`|`) can be used to add another production instead of repeating the rule name.
Parentheses can be used for grouping and also allow `|` within to select from a series in a middle of a production.

```BNF
bread -> "toast" | "biscuits" | "English muffin" ;
protein -> ( "scrambled" | "poached" | "fried" ) "eggs" ;
```

Instead of recursion, we can use a postfix `*` to allow the previous symbol or group to be repeated zero or more times.
A postfix `+` is similar but the symbol has to appear once.
We also have a postfix `?` for optional production. The symbol before can appear zero or one time, but not more.

```BNF
crispiness -> "really" "really"* ;
crispiness -> "really"+ ;
breakfast -> protein ( "with" breakfast "on the side" )? ;
```

### A Grammar for Lox expressions

We will implement a subset of grammar rules first then add more features later on.
First, we will worry about some necessary expressions.

- **Literals**
- **Unary expressions**
- **Binary expressions**
- **Parentheses**

```BNF
expression -> literal | unary | binary | grouping ;
literal -> NUMBER | STRING | "true" | "false" | "nil" ;
grouping -> "(" expression ")" ;
unary -> ( "-" | "!" ) expression ;
binary -> expression operator expression ;
operator -> "==" | "!=" | "<" | "<=" | ">" | ">=" | "+" | "-" | "*" | "/" ;
```

## Implementing Syntax Trees

Since our grammar is recursive, the data structure will form a tree. This structure is aptly called **syntax tree**.
Specifically, it is an **abstract syntax tree** (**AST**).

### Disoriented objects

In an object-oriented language like Java, classes usually have methods but here, all we have is a bunch of data without any methods.
The problem here is that AST lies on the border between parsing and interpreting, and owned by neither territory. Therefore, the main purpose of the tree is communication between the two territories and not associated with any behavior.

### Metaprogramming the trees

Despite the possibility of implementing each class in Java, it will get tedious very fast so we will write code to generate that for us.

## Working with Trees

For each expression, the interpreter will need to select a different chunk of code to handle that expression. Doing this a chain of `if` test will not be efficient and expressions with name lower in the alphabet will take longer.
On the other hand, if we add separate methods to each classes for each expressions, our AST will step into the domain of the interpreter and we don't want that.
We want to keep our AST as a communication tools without stepping into the domain either the interpreter or the parser. By doing so, we can keep different functionalities separate from each other and make the code easy to maintain.

### The expression problem

We have a table where the rows are classes, or types, and the columns are methods, or operations. Each cell contains the piece of code to do the operation on that type.
An object-oriented language will assume that operations we do with a type is related to each other. These languages make it easy to define new class with similar types. This means that it is easy to add rows using these languages but hard to add columns.
On the other hand, functional languages assumes the opposite. An operation is implemented with different types which makes it easy to add new operations. These languages make it easy to add columns but hard to add rows.
As we see, each style has its up and downsides and neither is good at adding _both_ rows and columns to the table. This is the "expression problem".
No perfect solution yet exists for this problem and all we can do is implement features, design patterns or tricks to try and circumvent the issue.
For our interpreter, we will use a design pattern to overcome this specific issue.

### The Visitor pattern

The **Visitor pattern** is the most widely misunderstood pattern in all of _Design Patterns_.
The trouble starts with terminology. The pattern isn't about "visiting", and the "accept" method doesn't provide a helpful image.
Many thinks that the pattern has to do with traversing trees, which isn't the case. It is just a coincidence that we will use it on tree-like classes, but it is possible to work on a single object.
The Visitor pattern is about approximating the functional style within an OOP language. It lets us add new columns to the table easily.
The heart of the Visitor pattern is using polymorphic dispatch on the _type_ classes to select the appropriate method on the _visitor_ class.
Each type classes is a row but each methods for a sinlge visitor is a column.
Each class has one `accept()` method which is overriden in each subclass and it can be used for as many visitors as we want without having to modify the base type class again.

### Visitors for expressions

Now, we will implement the visitor pattern for our expressions.

## A (Not Very) Pretty Printer

To inspect the parsed syntax tree, we will create a pretty printer to produce a string of text which is valid syntax in the source language. This act of converting a tree to a string is often called "pretty printing".
The string representation we product will look more like Lisp instead of Lox syntax to clearly show the order of operations.

## Challenges

1. Earlier, I said that the `|`, `*`, and `+` forms we added to our grammar metasyntax were just syntactic sugar. Take this grammar:

   ```BNF
   expr -> expr ( "(" ( expr ( "," expr )* )? ")" | "." IDENTIFIER )+ | IDENTIFIER | NUMBER
   ```

   Produce a grammar that matches the same language but does not use any of that notational sugar.
   _Bonus_: What kind of expression does this bit of grammar encode?

   > ```BNF
   > expr -> expr subexpr1 ;
   > expr -> IDENTIFIER ;
   > expr -> NUMBER ;
   >
   > subexpr1 -> subexpr2 ;
   > subexpr1 -> subexpr2 subexpr1 ;
   >
   > subexpr2 -> "(" subexpr3 ")" ;
   > subexpr2 -> "()" ;
   > subexpr2 -> "." IDENTIFIER ;
   >
   > subexpr3 -> expr subexpr4 ;
   > subexpr3 -> expr ;
   >
   > subexpr4 -> "," expr ;
   > ```
   >
   > This bit of grammar encode function calls.

2. The Visitor pattern lets you emulate the functional style in an object-oriented language. Devise a complementary pattern for a functional language. It should let you bundle all of the operations on one type together and let you define new types easily.

   (SML or Haskell would be ideal for this exercise, but Scheme or another Lisp works as well.)

   > For a functional paradigm, we have operations that can operate on all of our type by pattern matching.  
   > This means creating a new operation to operate on a types is easy but creating a new type is difficult and time consuming.  
   > It might be possible to use Haskell [classes](https://www.haskell.org/tutorial/classes.html).  
   > By using Haskell classes, we can bundle the operations on the various types into one class and each type will be an instance of that class.  
   > This way, new types can be defined easily by creating new instances of that class and specify the exact function, used for that type.
   >
   > In conclusion, the main question can be answered in Haskell by using classes, but that solution will re-introduce the original issue in OOP where it would add complexity when adding more functions.  
   > It is also impossible to do something similar to the Visitor pattern due to how Haskell, or functional languages in general, does not have the concept of state within the types or classes.

3. In [reverse Polish notation](https://en.wikipedia.org/wiki/Reverse_Polish_notation) (RPN), the operands to an arithmetic operator are both placed before the operator, so `1 + 2` becomes `1 2 +`. Evaluation proceeds from left to right. Numbers are pushed onto an implicit stack. An arithmetic operator pops the top two numbers, performs the operation, and pushes the result. Thus, this:

   ```BNF
   (1 + 2) * (4 - 3)
   ```

   in RPN becomes:

   ```BNF
   1 2 + 4 3 - *
   ```

   Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN, and returns the resulting string.

   > By using the AstPrinter, we can easily convert the resulting string to RPN by modifying the functions so that the operator is printed at the end.  
   > By modifying the `parenthesize()` function to print the operator at the end instead of the beginning, we can convert the string to RPN.  
   > The example code is RPNPrinter.java
