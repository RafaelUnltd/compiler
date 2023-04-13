import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LexicalAnalyzer {
    public static int line = 1;
    private char ch = ' ';
    private FileReader file;

    public LexicalAnalyzer(String fileName) throws FileNotFoundException{
        try{
            file = new FileReader (fileName);
        } catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado");
            throw e;
        }
    }

    private void readch() throws IOException{
        ch = (char) file.read();
    }

    private boolean readch(char c) throws IOException{
        readch();
        if (ch != c) return false;
        ch = ' ';
        return true;
    }
}
/* 

public Token scan(br BufferedReader) {
    boolean persistChar = false;
    int state = 1;

    int c = 0;
    char currentChar;
    String identifierBuffer = ""

    while ((c = br.read()) != -1) {
        if (persistChar) {
            persistChar = false;
        } else {
            currentChar = (char) c;
        }

        switch (state) {
            case 1:
                switch (currentChar) {
                    case '!':
                        state = 2;
                        break;
                    case '>':
                        state = 3;
                        break;
                    case '$':
                        identifierBuffer += "$"
                        state = 4;
                        break;
                    default:
                        throw new LexicalError("Not a valid character");
                        break;
                }
                break;
            case 2:
                switch (currentChar) {
                    case '=':
                        this.registerToken("!=");
                        state = 1;
                        break;
                    default:
                        this.registerToken("!");
                        state = 1;
                        break;
                }
                break;
            case 3:
                switch (currentChar) {
                    case '>':
                        this.registerToken(">>");
                        state = 1;
                        break;
                    default:
                        this.registerToken(">");
                        state = 1;
                        break;
                }
                break;
            case 4:
                if (this.isLetter(currentChar)) {
                    state = 5;
                } else {
                    throw new LexicalError("Error while recognizing identifier");
                }
                break;
            case 5:
                if (this.isLetter(currentChar) || this.isDigit(currentChar)) {
                    identifierBuffer += currentChar;
                } else {
                    this.registerToken(identifierBuffer);
                    state = 1;
                }
                break;
        }
    }
}*/