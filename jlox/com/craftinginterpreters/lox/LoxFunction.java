// LoxFunction.java
// Contains the class and methods to implement functions.

package com.craftinginterpreters.lox;

import java.util.List;

class LoxFunction implements LoxCallable {
  private final Stmt.Function declaration;
  private final Environment closure;

  private final boolean isInitializer;

  /**
   * Initializes the Lox function with the declaration and the closing
   * environment.
   * 
   * @param declaration
   * @param closure
   * @param isInitializer whether the function is an initializer for a class
   */
  LoxFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
    this.isInitializer = isInitializer;
    this.closure = closure;
    this.declaration = declaration;
  }

  /**
   * Bind the function to an environment where "this" is bound.
   * 
   * @param instance
   * @return
   */
  LoxFunction bind(LoxInstance instance) {
    Environment environment = new Environment(closure);
    environment.define("this", instance);
    return new LoxFunction(declaration, environment, isInitializer);
  }

  @Override
  public String toString() {
    return "<fn " + declaration.name.lexeme + ">";
  }

  @Override
  public int arity() {
    return declaration.params.size();
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments) {
    // Create a new environment for the function
    Environment environment = new Environment(closure);
    // Define all variables in the parameters
    for (int i = 0; i < declaration.params.size(); i++) {
      environment.define(declaration.params.get(i).lexeme, arguments.get(i));
    }

    try {
      interpreter.executeBlock(declaration.body, environment);
    } catch (Return returnValue) {
      // catch is used to unwind the stack to the function call
      if (isInitializer)
        return closure.getAt(0, "this");
      return returnValue.value;
    }

    if (isInitializer)
      return closure.getAt(0, "this");
    return null;
  }
}
