import java.io.FileNotFoundException;

public class Compiler {
    private static LexicalAnalyzer lexAnalyzer;
    private static SyntaticAnalyzer synAnalyzer;
    private static SemanticAnalyzer semAnalyzer;

    public static void main(String[] args) {

        try {
            lexAnalyzer = new LexicalAnalyzer(args[0]);
        } catch (FileNotFoundException e) {
            System.out.println("Erro ao procurar arquivo.");
            return;
        }

        semAnalyzer = new SemanticAnalyzer();
        synAnalyzer = new SyntaticAnalyzer(lexAnalyzer, semAnalyzer);
        synAnalyzer.start();
    }
}
