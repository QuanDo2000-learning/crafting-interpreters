// Return.java
// Implement the return in Lox using Java's exception.

package com.craftinginterpreters.lox;

class Return extends RuntimeException {
  final Object value;

  /**
   * Initialize the return exception with the return value.
   * 
   * @param value
   */
  Return(Object value) {
    super(null, null, false, false);
    this.value = value;
  }
}
