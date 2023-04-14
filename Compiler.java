import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler {
    private static LexicalAnalyzer lexAnalyzer;

    public static void main(String[] args) {
        int count = 1;
        System.out.println("Test Case "+count);

        try {
            lexAnalyzer = new LexicalAnalyzer(args[0]);
            count++;
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao procurar arquivo.");
            return;
        }

        try {
            Lexeme lex;

            do {
                lex = lexAnalyzer.scan();
                System.out.println(lex.toString());
            } while (lex.getType() != Tag.Types.RW_END);

        } catch (IOException e) {
            System.out.println("Erro ao chamar método de Scan");
        }
    }
}
