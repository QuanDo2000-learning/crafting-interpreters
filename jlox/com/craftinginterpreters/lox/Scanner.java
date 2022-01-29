// Scanner.java
// Contains the Scanner class

package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.lox.TokenType.*;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  // Init Scanner state
  private int start = 0;
  private int current = 0;
  private int line = 1;

  // Creat and define a map of all reserved words
  private static final Map<String, TokenType> keywords;
  static {
    keywords = new HashMap<>();
    keywords.put("and", AND);
    keywords.put("class", CLASS);
    keywords.put("else", ELSE);
    keywords.put("false", FALSE);
    keywords.put("for", FOR);
    keywords.put("fun", FUN);
    keywords.put("if", IF);
    keywords.put("nil", NIL);
    keywords.put("or", OR);
    keywords.put("print", PRINT);
    keywords.put("return", RETURN);
    keywords.put("super", SUPER);
    keywords.put("this", THIS);
    keywords.put("true", TRUE);
    keywords.put("var", VAR);
    keywords.put("while", WHILE);
  }

  /**
   * Initializes the Scanner with the source code.
   * 
   * @param source The raw source code string
   */
  Scanner(String source) {
    this.source = source;
  }

  /**
   * Scan the entire raw source code and produce tokens.
   * 
   * @return a list of tokens
   */
  List<Token> scanTokens() {
    // Loop through the entire raw source code
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    // For cleaner Token list
    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  /**
   * Scan and produce one token
   */
  private void scanToken() {
    char c = advance();
    switch (c) {
      // Single character lexeme
      case '(':
        addToken(LEFT_PAREN);
        break;
      case ')':
        addToken(RIGHT_PAREN);
        break;
      case '{':
        addToken(LEFT_BRACE);
        break;
      case '}':
        addToken(RIGHT_BRACE);
        break;
      case ',':
        addToken(COMMA);
        break;
      case '.':
        addToken(DOT);
        break;
      case '-':
        addToken(MINUS);
        break;
      case '+':
        addToken(PLUS);
        break;
      case ';':
        addToken(SEMICOLON);
        break;
      case '*':
        addToken(STAR);
        break;
      // Operators
      case '!':
        addToken(match('=') ? BANG_EQUAL : BANG);
        break;
      case '=':
        addToken(match('=') ? EQUAL_EQUAL : EQUAL);
        break;
      case '<':
        addToken(match('=') ? LESS_EQUAL : LESS);
        break;
      case '>':
        addToken(match('=') ? GREATER_EQUAL : GREATER);
        break;
      case '/':
        if (match('/')) {
          // A comment goes until the end of the line.
          while (peek() != '\n' && !isAtEnd())
            advance();
        } else if (match('*')) {
          blockComment();
        } else {
          addToken(SLASH);
        }
        break;

      case ' ':
      case '\r':
      case '\t':
        // Ignore whitespace.
        break;

      case '\n':
        line++;
        break;

      case '"':
        // String literal
        string();
        break;

      default:
        if (isDigit(c)) {
          // Number literal
          number();
        } else if (isAlpha(c)) {
          identifier();
        } else {
          // Report an error if Lox doesn't recognize the character
          // The character is still consumed to prevent infinite loop
          Lox.error(line, "Unexpected character.");
        }
        break;
    }
  }

  /**
   * Scan and discard a block comment
   */
  private void blockComment() {
    while (!isAtEnd() && (peek() != '*' || peekNext() != '/')) {
      // Multi-line
      if (peek() == '\n')
        line++;
      else if (match('/') && match('*')) {
        // Nesting
        blockComment();
        if (isAtEnd()) {
          break;
        }
      }
      advance();
    }
    if (isAtEnd()) {
      Lox.error(line, "Unterminated block comment.");
      return;
    }
    // The closing "*/"
    advance();
    advance();
  }

  /**
   * Scan and produce an identifier.
   */
  private void identifier() {
    while (isAlphaNumeric(peek()))
      advance();

    String text = source.substring(start, current);
    // Check if the identifier is a reserved keyword or not
    TokenType type = keywords.get(text);
    if (type == null)
      type = IDENTIFIER;
    addToken(type);
  }

  /**
   * Scan and produce a number literal
   */
  private void number() {
    while (isDigit(peek()))
      advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      // Consume the "."
      advance();

      while (isDigit(peek()))
        advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }

  /**
   * Scan and produce a string literal token
   */
  private void string() {
    // Consume characters until closing quote or EOF
    while (peek() != '"' && !isAtEnd()) {
      // Support for multi-line strings
      if (peek() == '\n')
        line++;
      advance();
    }

    // Error to have unterminated string
    if (isAtEnd()) {
      Lox.error(line, "Unterminated string.");
      return;
    }

    // The closing ".
    advance();

    // Trim the surrounding quotes.
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  /**
   * Consume the current character if it matches what we expected.
   * 
   * @param expected character
   * @return {@code boolean}
   */
  private boolean match(char expected) {
    if (isAtEnd())
      return false;
    if (source.charAt(current) != expected)
      return false;
    current++;
    return true;
  }

  /**
   * Look at the current character without consuming it.
   * 
   * @return current character
   */
  private char peek() {
    if (isAtEnd())
      return '\0';
    return source.charAt(current);
  }

  /**
   * Look at the next character.
   * 
   * @return next character
   */
  private char peekNext() {
    if (current + 1 >= source.length())
      return '\0';
    return source.charAt(current + 1);
  }

  /**
   * Check if current character is alphabet.
   * 
   * @param c current character
   * @return {@code boolean}
   */
  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
  }

  /**
   * Check if current character is either alphabet or numeric.
   * 
   * @param c current character
   * @return {@code boolean}
   */
  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  /**
   * Check if the current character is a digit.
   * 
   * @param c current character
   * @return {@code boolean}
   */
  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  /**
   * Check if current Scanner position is at the end of the source code.
   * 
   * @return {@code boolean}
   */
  private boolean isAtEnd() {
    return current >= source.length();
  }

  /**
   * Consume the next character in the source file and returns it.
   * 
   * @return the character at the position before moving
   */
  private char advance() {
    return source.charAt(current++);
  }

  /**
   * Add token without literal value.
   * 
   * @param type
   */
  private void addToken(TokenType type) {
    addToken(type, null);
  }

  /**
   * Grab the text of the current lexeme and output it.
   * 
   * @param type
   * @param literal
   */
  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

}
