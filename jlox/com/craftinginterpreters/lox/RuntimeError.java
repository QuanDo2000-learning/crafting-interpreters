// RuntimeError.java
// Custom class to catch RuntimeException during interpreter execution

package com.craftinginterpreters.lox;

class RuntimeError extends RuntimeException {
  final Token token;

  /**
   * Creates a RuntimeError which tracks the token and prints a message for the
   * user
   * 
   * @param token
   * @param message
   */
  RuntimeError(Token token, String message) {
    super(message);
    this.token = token;
  }

}
