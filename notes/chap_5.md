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
