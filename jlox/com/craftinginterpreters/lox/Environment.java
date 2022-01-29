// Environment.java
// Contains the class for the Environment and all methods

package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
  final Environment enclosing;
  private final Map<String, Object> values = new HashMap<>();

  /**
   * Default constructor for the environment.
   * Initializes the enclosing environment to null.
   */
  Environment() {
    enclosing = null;
  }

  /**
   * Constructor where the enclosing environment is specified.
   * 
   * @param enclosing
   */
  Environment(Environment enclosing) {
    this.enclosing = enclosing;
  }

  /**
   * Get a variable from the environment.
   * Throw an error if variable is not found.
   * 
   * @param name variable name
   * @return
   */
  Object get(Token name) {
    // Find in current environment
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    // Find in enclosing environments
    if (enclosing != null)
      return enclosing.get(name);

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  /**
   * Assign a value to an existing variable.
   * 
   * @param name
   * @param value
   */
  void assign(Token name, Object value) {
    // Assign to the current environment
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }

    // Assign at the enclosing environment when variable is not found in current
    // environment
    if (enclosing != null) {
      enclosing.assign(name, value);
      return;
    }

    throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  /**
   * Create/Define a new variable in the environment.
   * Allow redefinition of variable.
   * 
   * @param name  variable name
   * @param value variable value
   */
  void define(String name, Object value) {
    values.put(name, value);
  }

  /**
   * Obtain the environment a given distance away from current.
   * 
   * @param distance
   * @return
   */
  Environment ancestor(int distance) {
    Environment environment = this;
    for (int i = 0; i < distance; i++) {
      environment = environment.enclosing;
    }

    return environment;
  }

  /**
   * Get a variable given the distance to it.
   * 
   * @param distance
   * @param name
   * @return
   */
  Object getAt(int distance, String name) {
    return ancestor(distance).values.get(name);
  }

  /**
   * Assign a variable at a given distance away.
   * 
   * @param distance
   * @param name
   * @param value
   */
  void assignAt(int distance, Token name, Object value) {
    ancestor(distance).values.put(name.lexeme, value);
  }
}
