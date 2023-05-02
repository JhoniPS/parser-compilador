package parser;

public class Parser implements IParser {
    private String input;
    private int lookahead;

    @Override
    public boolean parse(String string) {
        this.input = string;
        lookahead = 0;
        boolean result = expr();
        if (lookahead != input.length()) {
            error("\n|String inválida: " + this.input + " |caractere desconhecido: " + lookahead());
        }
        return result;
    }

    @Override
    public char lookahead() {
        if (lookahead < input.length()) {
            return input.charAt(lookahead);
        } else {
            return EOF;
        }
    }

    @Override
    public char next() {
        char c = lookahead();
        if (c != EOF) {
            lookahead++;
        }
        return c;
    }

    @Override
    public void match(char c) {
        if (lookahead() == c) {
            next();
        } else {
            error("Expected " + c + ", found ");
        }
    }

    @Override
    public void error(String msg) {
        int col = lookahead + 1;
        for (int i = lookahead - 1; i >= 0 && input.charAt(i) != '\n'; i--) {
            col--;
        }
        System.out.println(msg + " |Erro na coluna: " + col + "|");
        System.exit(0);
    }

    private boolean expr() {
        if (term()) {
            return exprPrime();
        }
        return false;
    }

    private boolean exprPrime() {
        if (lookahead() == '+') {
            match('+');
            if (term()) {
                return exprPrime();
            }
            return false;
        } else if (lookahead() == '-') {
            match('-');
            if (term()) {
                return exprPrime();
            }
            return false;
        }
        return true;
    }

    private boolean term() {
        if (unary()) {
            return termPrime();
        }
        return false;
    }

    private boolean termPrime() {
        if (lookahead() == '*') {
            match('*');
            if (unary()) {
                return termPrime();
            }
            return false;
        } else if (lookahead() == '/') {
            match('/');
            if (unary()) {
                return termPrime();
            }
            return false;
        }
        return true;
    }

    private boolean unary() {
        if (lookahead() == '+') {
            match('+');
            return unary();
        } else if (lookahead() == '-') {
            match('-');
            return unary();
        } else {
            return pow();
        }
    }

    private boolean pow() {
        if (factor()) {
            return powPrime();
        }
        return false;
    }

    private boolean powPrime() {
        if (lookahead() == '^') {
            match('^');
            if (unary()) {
                return powPrime();
            }
            return false;
        }
        return true;
    }

    private boolean factor() {
        if (lookahead() == '(') {
            match('(');
            if (expr()) {
                match(')');
                return true;
            }
            return false;
        } else if (Character.isLetter(lookahead())) {
            match(lookahead());
            return true;
        } else if (Character.isDigit(lookahead())) {
            match(lookahead());
            return true;
        }
        return false;
    }
}
