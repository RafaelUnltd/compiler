import java.io.FileNotFoundException;

public class Compiler {
    private static LexicalAnalyzer lexAnalyzer;
    private static SyntaticAnalyzer synAnalyzer;

    public static void main(String[] args) {

        try {
            lexAnalyzer = new LexicalAnalyzer(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao procurar arquivo.");
            return;
        }

        synAnalyzer = new SyntaticAnalyzer(lexAnalyzer);
        synAnalyzer.start();
    }
}
