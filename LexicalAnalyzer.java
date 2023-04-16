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

    private Lexeme getLexemeAndReadNext(String key) throws IOException {
        readch();
        return symbols.getLexeme(key);
    }

    public Lexeme scan() throws IOException {
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b'){
                continue;
            }
            else if (ch == '\n')
                line++;
            else
                break;
        }
        
        switch (ch) {
            case '&':
                if (readch('&'))
                    return this.getLexemeAndReadNext("&&");
            case '!':
                if (readch('!'))
                    return this.getLexemeAndReadNext("!");
            case '|':
                if (readch('|'))
                    return this.getLexemeAndReadNext("||");
            case '=':
                if (readch('='))
                    return this.getLexemeAndReadNext("==");
                else
                    return this.getLexemeAndReadNext("=");
            case '<':
                if (readch('='))
                    return this.getLexemeAndReadNext("<=");
                else if (readch('>'))
                    return this.getLexemeAndReadNext("<>");
                else
                    return this.getLexemeAndReadNext("<");
            case '>':
                if (readch('='))
                    return this.getLexemeAndReadNext(">=");
                else
                    return this.getLexemeAndReadNext(">");
            case '+':
                return this.getLexemeAndReadNext("+");
            case '*':
                return this.getLexemeAndReadNext("*");
            case '/':
                if (readch('*')) {
                    char ch1 = ' ';
                    char ch2 = ' ';
                    do {
                        readch();
                        ch1 = ch;
                        readch();
                        ch2 = ch;
                    } while(ch1 != '*' && ch2 != '/');
                    return this.scan();
                }
                else
                    return symbols.getLexeme("/");
            case '-':
                return this.getLexemeAndReadNext("-");
            case ',':
                return this.getLexemeAndReadNext(",");
            case '(':
                return this.getLexemeAndReadNext("(");
            case ')':
                return this.getLexemeAndReadNext(")");
            case ';':
                return this.getLexemeAndReadNext(";");
            
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
            if(w == null){
                w = new Lexeme(s, Tag.Types.IDL_ID);
                symbols.addSymbol(w);
            }
            return w;
        }

        if (ch == '{') {
            StringBuffer sb = new StringBuffer();
            readch();
            while (ch != '}') {
                sb.append(ch);
                readch();
            };
            readch();
            String s = sb.toString();
            return new Lexeme(s, Tag.Types.IDL_LITERAL);
        }

        if (ch == '\'') {
            readch();
            char charBuffer = ch;
            readch();
            if (ch == '\'') {
                readch();
                return new Lexeme(charBuffer + "", Tag.Types.IDL_CHAR_CONST);
            }
        }

        // Caracteres não especificados
        Lexeme t = new Lexeme(ch + "", Tag.Types.VOID_VALUE);
        ch = ' ';
        return t;
    }
}
