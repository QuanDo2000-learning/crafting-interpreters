# Methods and Initializers

## Challenges

1. The hash table lookup to find a class’s `init()` method is constant time, but still fairly slow. Implement something faster. Write a benchmark and measure the performance difference.

2. In a dynamically typed language like Lox, a single callsite may invoke a variety of methods on a number of classes throughout a program’s execution. Even so, in practice, most of the time a callsite ends up calling the exact same method on the exact same class for the duration of the run. Most calls are actually not polymorphic even if the language says they can be.

   How do advanced language implementations optimize based on that observation?

3. When interpreting an `OP_INVOKE` instruction, the VM has to do two hash table lookups. First, it looks for a field that could shadow a method, and only if that fails does it look for a method. The former check is rarely useful—most fields do not contain functions. But it is _necessary_ because the language says fields and methods are accessed using the same syntax, and fields shadow methods.

   That is a language _choice_ that affects the performance of our implementation. Was it the right choice? If Lox were your language, what would you do?
