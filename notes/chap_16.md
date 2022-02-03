# Scanning on Demand

## Challenges

1. Many newer languages support [**string interpolation**](https://en.wikipedia.org/wiki/String_interpolation). Inside a string literal, you have some sort of special delimiters—most commonly `${` at the beginning and `}` at the end. Between those delimiters, any expression can appear. When the string literal is executed, the inner expression is evaluated, converted to a string, and then merged with the surrounding string literal.

   For example, if Lox supported string interpolation, then this...

   ```lox
   var drink = "Tea";
   var steep = 4;
   var cool = 2;
   print "${drink} will be ready in ${steep + cool} minutes.";
   ```

   ... would print:

   ```txt
   Tea will be ready in 6 minutes.
   ```

   What token types would you define to implement a scanner for string interpolation? What sequence of tokens would you emit for the above string literal?

   What tokens would you emit for:

   ```txt
   "Nested ${"interpolation?! Are you ${"mad?!"}"}"
   ```

   Consider looking at other language implementations that support interpolation to see how they handle it.

2. Several languages use angle brackets for generics and also have a `>>` right shift operator. This led to a classic problem in early versions of C++:

   ```c++
   vector<vector<string>> nestedVectors;
   ```

   This would produce a compile error because the `>>` was lexed to a single right shift token, not two `>` tokens. Users were forced to avoid this by putting a space between the closing angle brackets.

   Later versions of C++ are smarter and can handle the above code. Java and C# never had the problem. How do those languages specify and implement this?

3. Many languages, especially later in their evolution, define “contextual keywords”. These are identifiers that act like reserved words in some contexts but can be normal user-defined identifiers in others.

   For example, `await` is a keyword inside an `async` method in C#, but in other methods, you can use `await` as your own identifier.

   Name a few contextual keywords from other languages, and the context where they are meaningful. What are the pros and cons of having contextual keywords? How would you implement them in your language’s front end if you needed to?
