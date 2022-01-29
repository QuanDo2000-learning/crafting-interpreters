// LoxInstance.java
// Contains the class and methods to implement the runtime
// representation of an instance of Lox class.

package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

class LoxInstance {
  private LoxClass klass;
  private final Map<String, Object> fields = new HashMap<>();

  /**
   * Initialize an instance of the provided Lox class.
   * 
   * @param klass
   */
  LoxInstance(LoxClass klass) {
    this.klass = klass;
  }

  /**
   * Get a property from the current instance.
   * The property can be a field or a method.
   * 
   * @param name
   * @return
   */
  Object get(Token name) {
    // Fields shadow methods.
    if (fields.containsKey(name.lexeme)) {
      return fields.get(name.lexeme);
    }

    LoxFunction method = klass.findMethod(name.lexeme);
    if (method != null)
      return method.bind(this);

    throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
  }

  /**
   * Set a field in the instance.
   */
  void set(Token name, Object value) {
    fields.put(name.lexeme, value);
  }

  @Override
  public String toString() {
    return klass.name + " instance";
  }
}
