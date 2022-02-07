# Closures

## Challenges

1. Wrapping every ObjFunction in an ObjClosure introduces a level of indirection that has a performance cost. That cost isn’t necessary for functions that do not close over any variables, but it does let the runtime treat all calls uniformly.

   Change clox to only wrap functions in ObjClosures that need upvalues. How does the code complexity and performance compare to always wrapping functions? Take care to benchmark programs that do and do not use closures. How should you weight the importance of each benchmark? If one gets slower and one faster, how do you decide what trade-off to make to choose an implementation strategy?

2. Read the design note below. I’ll wait. Now, how do you think Lox _should_ behave? Change the implementation to create a new variable for each loop iteration.

3. A [famous koan](http://wiki.c2.com/?ClosuresAndObjectsAreEquivalent) teaches us that “objects are a poor man’s closure” (and vice versa). Our VM doesn’t support objects yet, but now that we have closures we can approximate them. Using closures, write a Lox program that models two-dimensional vector “objects”. It should:

   - Define a “constructor” function to create a new vector with the given x and y coordinates.
   - Provide “methods” to access the x and y coordinates of values returned from that constructor.
   - Define an addition “method” that adds two vectors and produces a third.
