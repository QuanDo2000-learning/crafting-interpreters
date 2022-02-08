# Classes and Instances

## Challenges

1. Trying to access a non-existent field on an object immediately aborts the entire VM. The user has no way to recover from this runtime error, nor is there any way to see if a field exists _before_ trying to access it. It’s up to the user to ensure on their own that only valid fields are read.

   How do other dynamically typed languages handle missing fields? What do you think Lox should do? Implement your solution.

2. Fields are accessed at runtime by their _string_ name. But that name must always appear directly in the source code as an _identifier_ token. A user program cannot imperatively build a string value and then use that as the name of a field. Do you think they should be able to? Devise a language feature that enables that and implement it.

3. Conversely, Lox offers no way to _remove_ a field from an instance. You can set a field’s value to `nil`, but the entry in the hash table is still there. How do other languages handle this? Choose and implement a strategy for Lox.

4. Because fields are accessed by name at runtime, working with instance state is slow. It’s technically a constant-time operation—thanks, hash tables—but the constant factors are relatively large. This is a major component of why dynamic languages are slower than statically typed ones.

   How do sophisticated implementations of dynamically typed languages cope with and optimize this?
