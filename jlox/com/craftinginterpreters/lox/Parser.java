// Parser.java
// Contains the Parser class

package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.craftinginterpreters.lox.TokenType.*;

class Parser {
  // Sentinel class to unwind the parser
  private static class ParseError extends RuntimeException {
  }

  private final List<Token> tokens;
  private int current = 0; // Current position of Parser in token list

  /**
   * Initializes the Parser with the list of tokens.
   * 
   * @param tokens
   */
  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  /**
   * Initial method to start the Parser.
   * Parse the program grammar rule.
   * <p>
   * program -> declaration* EOF
   * 
   * @return list of statements
   */
  List<Stmt> parse() {
    List<Stmt> statements = new ArrayList<>();
    while (!isAtEnd()) {
      statements.add(declaration());
    }

    return statements;
  }

  /**
   * Parse the expression grammar rule
   * <p>
   * expression -> assignment
   * 
   * @return
   */
  private Expr expression() {
    return assignment();
  }

  /**
   * Parse the declaration grammar rule.
   * <p>
   * declaration -> classDecl | funDecl | varDecl | statement
   * 
   * @return
   */
  private Stmt declaration() {
    try {
      if (match(CLASS))
        return classDeclaration();
      if (match(FUN))
        // Parse funDecl
        // funDecl -> "fun" function
        return function("function");
      if (match(VAR))
        return varDeclaration();

      return statement();
    } catch (ParseError error) {
      synchronize();
      return null;
    }
  }

  /**
   * Parse the classDecl grammar rule.
   * <p>
   * classDecl -> "class" IDENTIFIER ("<" IDENTIFIER)? "{" function* "}"
   * 
   * @return
   */
  private Stmt classDeclaration() {
    Token name = consume(IDENTIFIER, "Expect class name.");

    Expr.Variable superclass = null;
    if (match(LESS)) {
      consume(IDENTIFIER, "Expect superclass name.");
      superclass = new Expr.Variable(previous());
    }

    consume(LEFT_BRACE, "Expect '{' before class body.");

    List<Stmt.Function> methods = new ArrayList<>();
    while (!check(RIGHT_BRACE) && !isAtEnd()) {
      methods.add(function("method"));
    }

    consume(RIGHT_BRACE, "Expect '}' after class body.");

    return new Stmt.Class(name, superclass, methods);
  }

  /**
   * Parse statement grammar rule.
   * <p>
   * statement -> exprStmt | forStmt | ifStmt | printStmt
   * | returnStmt | whileStmt | block
   * 
   * @return
   */
  private Stmt statement() {
    if (match(FOR))
      return forStatement();
    if (match(IF))
      return ifStatement();
    if (match(PRINT))
      return printStatement();
    if (match(RETURN))
      return returnStatement();
    if (match(WHILE))
      return whileStatement();
    if (match(LEFT_BRACE))
      return new Stmt.Block(block());

    return expressionStatement();
  }

  /**
   * Parse the forStmt grammar rule.
   * <p>
   * forStmt -> "for" "(" (varDecl | exprStmt | ";") expression? ";" expression?
   * ")" statement
   * 
   * @return
   */
  private Stmt forStatement() {
    consume(LEFT_PAREN, "Expect '(' after 'for'.");

    Stmt initializer;
    if (match(SEMICOLON)) {
      initializer = null;
    } else if (match(VAR)) {
      initializer = varDeclaration();
    } else {
      initializer = expressionStatement();
    }

    Expr condition = null;
    if (!check(SEMICOLON)) {
      condition = expression();
    }
    consume(SEMICOLON, "Expect ';' after loop condition.");

    Expr increment = null;
    if (!check(RIGHT_PAREN)) {
      increment = expression();
    }
    consume(RIGHT_PAREN, "Expect ')' after for clauses.");
    Stmt body = statement();

    if (increment != null) {
      body = new Stmt.Block(
          Arrays.asList(
              body, new Stmt.Expression(increment)));
    }

    if (condition == null) {
      condition = new Expr.Literal(true);
    }
    body = new Stmt.While(condition, body);

    if (initializer != null) {
      body = new Stmt.Block(Arrays.asList(initializer, body));
    }

    return body;
  }

  /**
   * Parse the ifStmt grammar rule.
   * <p>
   * ifStmt -> "if" "(" expression ")" statement ("else" statement)?
   * 
   * @return
   */
  private Stmt ifStatement() {
    consume(LEFT_PAREN, "Expect '(' after 'if'.");
    Expr condition = expression();
    consume(RIGHT_PAREN, "Expect ')' after if condition.");

    Stmt thenBranch = statement();
    Stmt elseBranch = null;
    if (match(ELSE)) {
      elseBranch = statement();
    }

    return new Stmt.If(condition, thenBranch, elseBranch);
  }

  /**
   * Parse printStmt grammar rule.
   * <p>
   * printStmt -> "print" expression ";"
   * 
   * @return
   */
  private Stmt printStatement() {
    Expr value = expression();
    consume(SEMICOLON, "Expect ';' after value.");
    return new Stmt.Print(value);
  }

  /**
   * Parse the returnStmt grammar rule.
   * <p>
   * returnStmt -> "return" expression? ";"
   * 
   * @return
   */
  private Stmt returnStatement() {
    Token keyword = previous();
    Expr value = null;
    if (!check(SEMICOLON)) {
      value = expression();
    }

    consume(SEMICOLON, "Expect ';' after return value.");
    return new Stmt.Return(keyword, value);
  }

  /**
   * Parse the varDecl grammar rule.
   * <p>
   * varDecl -> "var" IDENTIFIER ("=" expression)? ";"
   * 
   * @return
   */
  private Stmt varDeclaration() {
    Token name = consume(IDENTIFIER, "Expect variable name.");

    Expr initializer = null;
    if (match(EQUAL)) {
      initializer = expression();
    }

    consume(SEMICOLON, "Exprect ';' after variable declaration.");
    return new Stmt.Var(name, initializer);
  }

  /**
   * Parse the whileStmt grammar rule.
   * <p>
   * whileStmt -> "while" "(" expression ")" statement
   * 
   * @return
   */
  private Stmt whileStatement() {
    consume(LEFT_PAREN, "Expect '(' after 'while'.");
    Expr condition = expression();
    consume(RIGHT_PAREN, "Expect ')' after condition.");
    Stmt body = statement();

    return new Stmt.While(condition, body);
  }

  /**
   * Parse exprStmt grammar rule.
   * <p>
   * exprStmt -> expression ";"
   * 
   * @return
   */
  private Stmt expressionStatement() {
    Expr expr = expression();
    consume(SEMICOLON, "Expect ';' after expression.");
    return new Stmt.Expression(expr);
  }

  /**
   * Parse the function and parameters grammar rule.
   * <p>
   * function -> IDENTIFIER "(" parameters? ")" block
   * <p>
   * parameters -> IDENTIFIER ("," IDENTIFIER)*
   * 
   * @param kind
   * @return
   */
  private Stmt.Function function(String kind) {
    Token name = consume(IDENTIFIER, "Expect " + kind + " name.");
    consume(LEFT_PAREN, "Expect '(' after " + kind + " name.");
    List<Token> parameters = new ArrayList<>();
    if (!check(RIGHT_PAREN)) {
      do {
        if (parameters.size() >= 255) {
          error(peek(), "Can't have more than 255 parameters.");
        }

        parameters.add(consume(IDENTIFIER, "Expect parameter name."));
      } while (match(COMMA));
    }
    consume(RIGHT_PAREN, "Expect ')' after parameters.");

    consume(LEFT_BRACE, "Expect '{' before " + kind + " body.");
    List<Stmt> body = block();
    return new Stmt.Function(name, parameters, body);
  }

  /**
   * Parse the block grammar rule.
   * <p>
   * block -> "{" declaration* "}"
   * 
   * @return
   */
  private List<Stmt> block() {
    List<Stmt> statements = new ArrayList<>();

    while (!check(RIGHT_BRACE) && !isAtEnd()) {
      statements.add(declaration());
    }

    consume(RIGHT_BRACE, "Expect '}' after block.");
    return statements;
  }

  /**
   * Parse the assignment grammar rule.
   * <p>
   * assignment -> (call ".")? IDENTIFIER "=" assignment | logic_or
   * 
   * @return
   */
  private Expr assignment() {
    Expr expr = or();

    if (match(EQUAL)) {
      Token equals = previous();
      Expr value = assignment();

      if (expr instanceof Expr.Variable) {
        Token name = ((Expr.Variable) expr).name;
        return new Expr.Assign(name, value);
      } else if (expr instanceof Expr.Get) {
        Expr.Get get = (Expr.Get) expr;
        return new Expr.Set(get.object, get.name, value);
      }

      error(equals, "Invalid assignment target.");
    }

    return expr;
  }

  /**
   * Parse the logic_or grammar rule.
   * <p>
   * logic_or -> logic_and ("or" logic_and)*
   * 
   * @return
   */
  private Expr or() {
    Expr expr = and();

    while (match(OR)) {
      Token operator = previous();
      Expr right = and();
      expr = new Expr.Logical(expr, operator, right);
    }

    return expr;
  }

  /**
   * Parse the logic_and grammar rule.
   * <p>
   * logic_and -> equality ("and" equality)*
   * 
   * @return
   */
  private Expr and() {
    Expr expr = equality();

    while (match(AND)) {
      Token operator = previous();
      Expr right = equality();
      expr = new Expr.Logical(expr, operator, right);
    }

    return expr;
  }

  /**
   * Parse the equality grammar rule.
   * <p>
   * equality -> comparison (("!=" | "==") comparison)*
   * 
   * @return
   */
  private Expr equality() {
    Expr expr = comparison();

    while (match(BANG_EQUAL, EQUAL_EQUAL)) {
      Token operator = previous();
      Expr right = comparison();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  /**
   * Parse the comparison grammar rule.
   * <p>
   * comparison -> term ((">" | ">=" | "<" | "<=") term)*
   * 
   * @return
   */
  private Expr comparison() {
    Expr expr = term();

    while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
      Token operator = previous();
      Expr right = term();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  /**
   * Parse the term grammar rule.
   * <p>
   * term -> factor (("-" | "+") factor)*
   * 
   * @return
   */
  private Expr term() {
    Expr expr = factor();

    while (match(MINUS, PLUS)) {
      Token operator = previous();
      Expr right = factor();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  /**
   * Parse the factor grammar rule.
   * <p>
   * factor -> unary (("/" | "*") unary)*
   * 
   * @return
   */
  private Expr factor() {
    Expr expr = unary();

    while (match(SLASH, STAR)) {
      Token operator = previous();
      Expr right = unary();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  /**
   * Parse the unary grammar rule.
   * <p>
   * unary -> ("!" | "-") unary | call
   * 
   * @return
   */
  private Expr unary() {
    if (match(BANG, MINUS)) {
      Token operator = previous();
      Expr right = unary();
      return new Expr.Unary(operator, right);
    }

    return call();
  }

  /**
   * Parse the arguments grammar rule and complete the function call.
   * <p>
   * arguments -> expression ("," expression)*
   * 
   * @param callee
   * @return
   */
  private Expr finishCall(Expr callee) {
    List<Expr> arguments = new ArrayList<>();
    if (!check(RIGHT_PAREN)) {
      do {
        if (arguments.size() >= 255) {
          error(peek(), "Can't have more than 255 arguments.");
        }
        arguments.add(expression());
      } while (match(COMMA));
    }

    Token paren = consume(RIGHT_PAREN, "Expect ')' after arguments.");

    return new Expr.Call(callee, paren, arguments);
  }

  /**
   * Parse the call grammar rule.
   * <p>
   * call -> primary ("(" arguments? ")" | "." IDENTIFIER)*
   * 
   * @return
   */
  private Expr call() {
    Expr expr = primary();

    while (true) {
      if (match(LEFT_PAREN)) {
        expr = finishCall(expr);
      } else if (match(DOT)) {
        Token name = consume(IDENTIFIER, "Expect property name after '.'.");
        expr = new Expr.Get(expr, name);
      } else {
        break;
      }
    }

    return expr;
  }

  /**
   * Parse primary grammar rule.
   * <p>
   * primary -> NUMBER | STRING | "true" | "false" | "nil"
   * | "(" expression ")" | IDENTIFIER | "super" "." IDENTIFIER
   * 
   * @return
   */
  private Expr primary() {
    if (match(FALSE))
      return new Expr.Literal(false);
    if (match(TRUE))
      return new Expr.Literal(true);
    if (match(NIL))
      return new Expr.Literal(null);

    if (match(NUMBER, STRING)) {
      return new Expr.Literal(previous().literal);
    }

    if (match(SUPER)) {
      Token keyword = previous();
      consume(DOT, "Expect '.' after 'super'.");
      Token method = consume(IDENTIFIER, "Expect superclass method name.");
      return new Expr.Super(keyword, method);
    }

    if (match(THIS))
      return new Expr.This(previous());

    if (match(IDENTIFIER)) {
      return new Expr.Variable(previous());
    }

    if (match(LEFT_PAREN)) {
      Expr expr = expression();
      consume(RIGHT_PAREN, "Expect ')' after expression.");
      return new Expr.Grouping(expr);
    }

    throw error(peek(), "Expect expression.");
  }

  /**
   * Check the current token if it matches any given input types.
   * The token is consumed if true.
   * 
   * @param types
   * @return {@code boolean}
   */
  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  /**
   * Consume the next token if it matches the expected type.
   * Otherwise, an error is thrown.
   * 
   * @param type    expected typed of token
   * @param message message for the user if not expected type
   * @return token
   */
  private Token consume(TokenType type, String message) {
    if (check(type))
      return advance();

    throw error(peek(), message);
  }

  /**
   * Check if the current token matches the given type.
   * 
   * @param type
   * @return {@code boolean}
   */
  private boolean check(TokenType type) {
    if (isAtEnd())
      return false;
    return peek().type == type;
  }

  /**
   * Consume the current token and return it.
   * Move the Parser to the next token.
   * 
   * @return token
   */
  private Token advance() {
    if (!isAtEnd())
      current++;
    return previous();
  }

  /**
   * Check if the Parser reached the EOF.
   * 
   * @return {@code boolean}
   */
  private boolean isAtEnd() {
    return peek().type == EOF;
  }

  /**
   * Get the current token
   * 
   * @return token
   */
  private Token peek() {
    return tokens.get(current);
  }

  /**
   * Get the previous token
   * 
   * @return token
   */
  private Token previous() {
    return tokens.get(current - 1);
  }

  /**
   * Report and returns an error at the specified token.
   * The error is returned so the calling function can decide what to do.
   * 
   * @param token   token where error happened
   * @param message message to the user
   * @return {@code ParseError}
   */
  private ParseError error(Token token, String message) {
    Lox.error(token, message);
    return new ParseError();
  }

  /**
   * Synchronizes the parser after an error happened.
   * Synchronization point is between statements.
   * Detected by finding a semicolon or a keyword.
   */
  private void synchronize() {
    advance();

    while (!isAtEnd()) {
      if (previous().type == SEMICOLON)
        return;

      switch (peek().type) {
        case CLASS:
        case FUN:
        case VAR:
        case FOR:
        case IF:
        case WHILE:
        case PRINT:
        case RETURN:
          return;
        default:
          break;
      }

      advance();
    }
  }
}
