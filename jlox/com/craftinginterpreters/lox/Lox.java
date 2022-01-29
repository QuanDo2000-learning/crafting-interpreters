// Lox.java
// The main Lox interpreter

package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
  // Static interpreter so the REPL uses the same interpreter
  private static final Interpreter interpreter = new Interpreter();
  // Keep track of error status during execution
  static boolean hadError = false;
  static boolean hadRuntimeError = false;

  /**
   * Starts the interpreter in specified mode.
   * Only accepts 0 or 1 argument.
   * 
   * @param args A list of command line arguments
   * @throws IOException If an input or output exception occurred
   */
  public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      // Invalid to have more than 1 argument
      System.out.println("Usage: jlox [script]");
      System.exit(64);
    } else if (args.length == 1) {
      // Execute the input file
      runFile(args[0]);
    } else {
      // Run the interpreter in REPL mode
      runPrompt();
    }
  }

  /**
   * Execute the file pointed to by path.
   * 
   * @param path Path to the file being executed
   * @throws IOException If an input or output exception occurred
   */
  private static void runFile(String path) throws IOException {
    // Read content from file as bytes
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    // Convert the bytes to String and execute it as raw source code
    run(new String(bytes, Charset.defaultCharset()));

    // Indicate an error in the exit code.
    if (hadError)
      System.exit(65);
    if (hadRuntimeError)
      System.exit(70);
  }

  /**
   * Run the interpreter in REPL mode on the command line.
   * 
   * @throws IOException If an input or output exception ocurred
   */
  private static void runPrompt() throws IOException {
    // Setup reader to read inputs from command line.
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    // Infinite loop to get user input
    for (;;) {
      System.out.print("> ");
      String line = reader.readLine();
      // Stop REPL when Ctrl+C or Ctrl+D
      if (line == null)
        break;
      // Execute the current line
      run(line);
      // Keep the REPL session running
      hadError = false;
    }
  }

  /**
   * Execute the source code.
   * 
   * @param source A String input as raw source code
   */
  private static void run(String source) {
    // Tranform source code into tokens
    Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();
    // Transform tokens into statements
    Parser parser = new Parser(tokens);
    List<Stmt> statements = parser.parse();

    // Stop if there was a syntax error;
    if (hadError)
      return;

    Resolver resolver = new Resolver(interpreter);
    resolver.resolve(statements);

    // Stop if there was a resolution error.
    if (hadError) {
      return;
    }

    interpreter.interpret(statements);
  }

  /**
   * Report error during execution of the interpreter.
   * 
   * @param line    Line number where error occurred.
   * @param message Message to the user.
   */
  static void error(int line, String message) {
    report(line, "", message);
  }

  /**
   * Print the error message to the screen and set {@code hadError} to true.
   * 
   * @param line    Line number where error occurred.
   * @param where   Where the error occurred on the line.
   * @param message Message to the user.
   */
  private static void report(int line, String where, String message) {
    System.err.println("[line " + line + "] Error" + where + ": " + message);
    hadError = true;
  }

  /**
   * Reports and error at the given token.
   * 
   * @param token   token where error happened
   * @param message message to user
   */
  static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }

  /**
   * Catch the custom RuntimeError class and report it.
   * Set the flag so the script knows about the runtime error.
   * 
   * @param error
   */
  static void runtimeError(RuntimeError error) {
    System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
    hadRuntimeError = true;
  }
}
