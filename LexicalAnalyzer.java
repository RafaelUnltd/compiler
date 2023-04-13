import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LexicalAnalyzer {
    public static int line = 1;
    private char ch = ' ';
    private FileReader file;

    private SymbolTable symbols;

    public LexicalAnalyzer(String fileName) throws FileNotFoundException {
        try {
            file = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado");
            throw e;
        }

        symbols = new SymbolTable();
    }

    private void readch() throws IOException {
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if (ch != c)
            return false;
        ch = ' ';
        return true;
    }

    public Lexeme scan() throws IOException {
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++;
            else
                break;
        }
        switch (ch) {
            case '&':
                if (readch('&'))
                    return symbols.getLexeme("&&");
            case '!':
                if (readch('!'))
                    return symbols.getLexeme("!");
            case '|':
                if (readch('|'))
                    return symbols.getLexeme("||");
            case '=':
                if (readch('='))
                    return symbols.getLexeme("==");
                else
                    return symbols.getLexeme("=");
            case '<':
                if (readch('='))
                    return symbols.getLexeme("<=");
                else if (readch('>'))
                    return symbols.getLexeme("<>");
                else
                    return symbols.getLexeme("<");
            case '>':
                if (readch('='))
                    return symbols.getLexeme(">=");
                else
                    return symbols.getLexeme(">");
            case '+':
                return symbols.getLexeme("+");
            case '*':
                return symbols.getLexeme("*");
            case '/':
                return symbols.getLexeme("/");
            case '-':
                return symbols.getLexeme("-");
            case ',':
                return symbols.getLexeme(",");
            case '(':
                return symbols.getLexeme("(");
            case ')':
                return symbols.getLexeme(")");
            case ';':
                return symbols.getLexeme(";");
            
        }
        // Números
        if (Character.isDigit(ch)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(ch, 10);
                readch();
            } while (Character.isDigit(ch));
            return new Lexeme(Integer.toString(value), Tag.Types.IDL_INTEGER_CONST);
        }
        // Identificadores
        if (Character.isLetter(ch)) {
            StringBuffer sb = new StringBuffer();
            do {
                sb.append(ch);
                readch();
            } while (Character.isLetterOrDigit(ch));
            String s = sb.toString();
            Lexeme w = symbols.getLexeme(s);
            if (w != null)
                return w; // palavra já existe na HashTable
            w = new Lexeme(s, Tag.Types.IDL_LITERAL);
            symbols.addSymbol(w);
            return w;
        }

        // Caracteres não especificados
        Lexeme t = new Lexeme(ch + "", Tag.Types.VOID_VALUE);
        ch = ' ';
        return t;
    }
}
