import java.util.ArrayList;

public class SemanticAnalyzer {
    public void start(Node syntaticTreeRoot) {
        System.out.println("Semantic analysis started.");

        printFromRoot(syntaticTreeRoot);

        System.out.println("Semantic analysis finished.");
    }

    private void printFromRoot(Node syntaticTreeRoot) {
        if (syntaticTreeRoot != null) {
            if (syntaticTreeRoot.getType() == Tag.Types.PARENT_NODE) {
                ArrayList<Node> children = syntaticTreeRoot.getChildren();
                for (Node child : children) {
                    if (child.getType() == Tag.Types.PARENT_NODE) {
                        printFromRoot(child);
                    } else {
                        System.out.println(child.getType());
                    }
                }
            } else {
                System.out.println(syntaticTreeRoot.getType());
            }
        }
    }
}