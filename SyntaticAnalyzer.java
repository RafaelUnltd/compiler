import java.io.IOException;
import java.util.ArrayList;

class Node {
    private Tag.Types type;
    private ArrayList<Node> children;

    public Node(Tag.Types type) {
        this.type = type;
        this.children = new ArrayList<Node>();
    }

  public void addChildren(Tag.Types type) {
    this.children.add(new Node(type));
  }

  public void addChildren(Node node) {
    if (node != null) {
        this.children.add(node);
    }
  }

  public Tag.Types getType() {
    return this.type;
  }

  public ArrayList<Node> getChildren() {
    return this.children;
  }
}

public class SyntaticAnalyzer {
    private LexicalAnalyzer lexical;
    private SemanticAnalyzer semantic;
    private Lexeme currentLexeme;
    private Node root;

    public SyntaticAnalyzer(LexicalAnalyzer lexical, SemanticAnalyzer semantic) {
        this.lexical = lexical;
        this.semantic = semantic;
        this.root = new Node(Tag.Types.PARENT_NODE);
        this.advance();
    }

    public void start() {
        Tag.Types[] ends = {Tag.Types.RW_END};
        this.eat(Tag.Types.RW_PROGRAM);
        this.root.addChildren(Tag.Types.RW_PROGRAM);
        this.eat(Tag.Types.IDL_ID);
        this.root.addChildren(Tag.Types.IDL_ID);
        // Declaration Statements until RW_BEGIN
        Node declarationsListNode = this.readDeclarationsList();
        this.root.addChildren(declarationsListNode);
        this.eat(Tag.Types.RW_BEGIN);
        this.root.addChildren(Tag.Types.RW_BEGIN);
        Node statementsListNode = this.readStatementsList(ends);
        this.root.addChildren(statementsListNode);
        // Statement List until RW_END
        this.eat(Tag.Types.RW_END);
        this.root.addChildren(Tag.Types.RW_END);
        this.semantic.start(this.root);
    }

    private void advance() {
        try {
            this.currentLexeme = this.lexical.scan();
        } catch (IOException e) {
            System.out.println("Erro ao chamar método de Scan");
            System.exit(1);
        }
    }

    private Node readDeclarationsList() {
        Node rootDeclarationsList = new Node(Tag.Types.PARENT_NODE);
        if (this.currentLexeme.getType() == Tag.Types.RW_BEGIN) {
            return null;
        }
        Node declarationNode = this.readDeclaration();
        Node declarationsListNode = this.readDeclarationsList();

        rootDeclarationsList.addChildren(declarationNode);
        rootDeclarationsList.addChildren(declarationsListNode);
        return rootDeclarationsList;
    }

    private Node readDeclaration() {
        Node rootDeclaration = new Node(Tag.Types.PARENT_NODE);
        Node identifierListNode = this.readIdentifierList();
        rootDeclaration.addChildren(identifierListNode);

        this.eat(Tag.Types.RW_IS);
        rootDeclaration.addChildren(Tag.Types.RW_IS);
   
        switch(this.currentLexeme.getType()) {
            case RW_INT:
                this.eat(Tag.Types.RW_INT);
                rootDeclaration.addChildren(Tag.Types.RW_INT);
                break;
            case RW_FLOAT:
                this.eat(Tag.Types.RW_FLOAT);
                rootDeclaration.addChildren(Tag.Types.RW_FLOAT);
                break;
            case RW_CHAR:
                this.eat(Tag.Types.RW_CHAR);
                rootDeclaration.addChildren(Tag.Types.RW_CHAR);
                break;
            default:
                this.showSyntaticError("Esperado 'int', 'float' ou 'char'. Encontrado '" + this.currentLexeme.getToken() + "'");
        }

        this.eat(Tag.Types.SY_SEMICOLON);
        rootDeclaration.addChildren(Tag.Types.SY_SEMICOLON);
        return rootDeclaration;
    }

    private Node readIdentifierList() {
        Node rootIdentifierList = new Node(Tag.Types.PARENT_NODE);
        this.eat(Tag.Types.IDL_ID);
        rootIdentifierList.addChildren(Tag.Types.IDL_ID);
        while (this.currentLexeme.getType() == Tag.Types.SY_COMMA) {
            this.eat(Tag.Types.SY_COMMA);
            rootIdentifierList.addChildren(Tag.Types.SY_COMMA);
            this.eat(Tag.Types.IDL_ID);
            rootIdentifierList.addChildren(Tag.Types.IDL_ID);
        }
        return rootIdentifierList;
    }

    private Node readStatementsList(Tag.Types[] expectedEnds) {
        Node rootStatementList = new Node(Tag.Types.PARENT_NODE);
        boolean reachedEnd = false;

        for (int i = 0; i < expectedEnds.length; i++) {
            if (this.currentLexeme.getType() == expectedEnds[i]) {
                reachedEnd = true;
                break;
            }
        }

        if (reachedEnd) {
            return null;
        }

        Node readStatementNode = this.readStatement();
        Node readStatementListNode = this.readStatementsList(expectedEnds);

        rootStatementList.addChildren(readStatementNode);
        rootStatementList.addChildren(readStatementListNode);

        return rootStatementList;
    }

    private Node readStatement() {
        Node rootStatement = new Node(Tag.Types.PARENT_NODE);

        switch (this.currentLexeme.getType()) {
            case IDL_ID:
                Node assignmentStatementNode = this.readAssignmentStatement();
                rootStatement.addChildren(assignmentStatementNode);
                break;
            case RW_IF:
                Node ifStatementNode = this.readIfStatement();
                rootStatement.addChildren(ifStatementNode);
                break;
            case RW_WHILE:
                Node whileStatementNode = this.readWhileStatement();
                rootStatement.addChildren(whileStatementNode);
                break;
            case RW_REPEAT:
                Node repeatStatementNode = this.readRepeatStatement();
                rootStatement.addChildren(repeatStatementNode);
                break;
            case RW_READ:
                Node readReadStatementNode = this.readReadStatement();
                rootStatement.addChildren(readReadStatementNode);
                break;
            case RW_WRITE:
                Node writeStatementNode = this.readWriteStatement();
                rootStatement.addChildren(writeStatementNode);
                break;
            default:
                this.showSyntaticError("Esperado 'identificador', 'if', 'while', 'repeat', 'read' ou 'write'. Encontrado '" + this.currentLexeme.getToken() + "'");
        }

        return rootStatement;
    }

    private Node readAssignmentStatement() {
        Node rootAssignmentStatement = new Node(Tag.Types.PARENT_NODE);

        this.eat(Tag.Types.IDL_ID);
        rootAssignmentStatement.addChildren(Tag.Types.IDL_ID);
        this.eat(Tag.Types.SY_ASSIGN);
        rootAssignmentStatement.addChildren(Tag.Types.SY_ASSIGN);
        Node simpleExpressionNode = this.readSimpleExpression();
        rootAssignmentStatement.addChildren(simpleExpressionNode);
        this.eat(Tag.Types.SY_SEMICOLON);
        rootAssignmentStatement.addChildren(Tag.Types.SY_SEMICOLON);

        return rootAssignmentStatement;
    }

    private Node readExpression () {
        Node rootExpression = new Node(Tag.Types.PARENT_NODE);
        Node simpleExpressionNode = this.readSimpleExpression();
        rootExpression.addChildren(simpleExpressionNode);
        if (
            this.currentLexeme.getType() == Tag.Types.RO_EQUAL ||
            this.currentLexeme.getType() == Tag.Types.RO_GREATER_EQUAL ||
            this.currentLexeme.getType() == Tag.Types.RO_LOWER_EQUAL ||
            this.currentLexeme.getType() == Tag.Types.RO_GREATER ||
            this.currentLexeme.getType() == Tag.Types.RO_LOWER ||
            this.currentLexeme.getType() == Tag.Types.RO_NOT_EQUAL
        ) {
            Node relOpNode = this.readRelOp();
            rootExpression.addChildren(relOpNode);
            Node simpleExpressionNode2 = this.readSimpleExpression();
            rootExpression.addChildren(simpleExpressionNode2);
        }
        return rootExpression;
    }

    private Node readSimpleExpression () {
        Node simpleExpressionRoot = new Node(Tag.Types.PARENT_NODE);
        Node readTermNode = this.readTerm();
        simpleExpressionRoot.addChildren(readTermNode);
        while (
            this.currentLexeme.getType() == Tag.Types.AO_ADD ||
            this.currentLexeme.getType() == Tag.Types.AO_SUB ||
            this.currentLexeme.getType() == Tag.Types.LO_OR
        ) {
            Node readAddOpNode = this.readAddOp();
            simpleExpressionRoot.addChildren(readAddOpNode);
            Node readTermNode2 = this.readTerm();
            simpleExpressionRoot.addChildren(readTermNode2);
            
        }

        return simpleExpressionRoot;
    }

    private Node readTerm () {
        Node readTermRoot = new Node(Tag.Types.PARENT_NODE);
        Node readFactorANode = this.readFactorA();
        readTermRoot.addChildren(readFactorANode);
        while (
            this.currentLexeme.getType() == Tag.Types.AO_MUL ||
            this.currentLexeme.getType() == Tag.Types.AO_DIV ||
            this.currentLexeme.getType() == Tag.Types.LO_AND    
        ) {
            Node readMulOpNode = this.readMulOp();
            readTermRoot.addChildren(readMulOpNode);
            Node readFactorANode2 = this.readFactorA();
            readTermRoot.addChildren(readFactorANode2);
        }

        return readTermRoot;
    }

    private Node readRelOp () {
        Node readRelOpRoot = new Node(Tag.Types.PARENT_NODE);
        switch (this.currentLexeme.getType()) {
            case RO_EQUAL:
                this.eat(Tag.Types.RO_EQUAL);
                readRelOpRoot.addChildren(Tag.Types.RO_EQUAL);
                break;
            case RO_GREATER_EQUAL:
                this.eat(Tag.Types.RO_GREATER_EQUAL);
                readRelOpRoot.addChildren(Tag.Types.RO_GREATER_EQUAL);
                break;
            case RO_LOWER_EQUAL:
                this.eat(Tag.Types.RO_LOWER_EQUAL);
                readRelOpRoot.addChildren(Tag.Types.RO_LOWER_EQUAL);
                break;
            case RO_GREATER:
                this.eat(Tag.Types.RO_GREATER);
                readRelOpRoot.addChildren(Tag.Types.RO_GREATER);
                break;
            case RO_LOWER:
                this.eat(Tag.Types.RO_LOWER);
                readRelOpRoot.addChildren(Tag.Types.RO_LOWER);
                break;
            case RO_NOT_EQUAL:
                this.eat(Tag.Types.RO_NOT_EQUAL);
                readRelOpRoot.addChildren(Tag.Types.RO_NOT_EQUAL);
                break;
            default:
                this.showSyntaticError("Esperado '==', '>=', '<=', '>', '<' ou '<>'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }

        return readRelOpRoot;
    }

    private Node readAddOp () {
        Node readAddOpRoot = new Node(Tag.Types.PARENT_NODE);
        switch (this.currentLexeme.getType()) {
            case AO_ADD:
                this.eat(Tag.Types.AO_ADD);
                readAddOpRoot.addChildren(Tag.Types.AO_ADD);
                break;
            case AO_SUB:
                this.eat(Tag.Types.AO_SUB);
                readAddOpRoot.addChildren(Tag.Types.AO_SUB);
                break;
            case LO_OR:
                this.eat(Tag.Types.LO_OR);
                readAddOpRoot.addChildren(Tag.Types.LO_OR);
                break;
            default:
                this.showSyntaticError("Esperado '+', '-' ou '||'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }

        return readAddOpRoot;
    }

    private Node readMulOp () {
        Node readMulOpRoot = new Node(Tag.Types.PARENT_NODE);
        switch (this.currentLexeme.getType()) {
            case AO_MUL:
                this.eat(Tag.Types.AO_MUL);
                readMulOpRoot.addChildren(Tag.Types.AO_MUL);
                break;
            case AO_DIV:
                this.eat(Tag.Types.AO_DIV);
                readMulOpRoot.addChildren(Tag.Types.AO_DIV);
                break;
            case LO_AND:
                this.eat(Tag.Types.LO_AND);
                readMulOpRoot.addChildren(Tag.Types.LO_AND);
                break;
            default:
                this.showSyntaticError("Esperado '*', '/' ou '&&'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }

        return readMulOpRoot;
    }

    private Node readFactorA () {
        Node readFactorARoot = new Node(Tag.Types.PARENT_NODE);
        switch (this.currentLexeme.getType()) {
            case IDL_ID:
            case SY_LEFT_PAR:
            case IDL_INTEGER_CONST:
            case IDL_FLOAT_CONST:
            case IDL_CHAR_CONST:
                Node readFactorNode = this.readFactor();
                readFactorARoot.addChildren(readFactorNode);
                break;
            case LO_NOT:
                this.eat(Tag.Types.LO_NOT);
                readFactorARoot.addChildren(Tag.Types.LO_NOT);
                Node readFactorNode2 = this.readFactor();
                readFactorARoot.addChildren(readFactorNode2);
                break;
            case AO_SUB:
                this.eat(Tag.Types.AO_SUB);
                readFactorARoot.addChildren(Tag.Types.AO_SUB);
                Node readFactorNode3 = this.readFactor();
                readFactorARoot.addChildren(readFactorNode3);
                break;
            default:
                this.showSyntaticError("Esperados um 'identificador', 'constante', 'separador ()' ou 'operador - / !'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }

        return readFactorARoot;
    }

    private Node readFactor () {
        Node readFactorRoot = new Node(Tag.Types.PARENT_NODE);
        switch (this.currentLexeme.getType()) {
            case IDL_ID:
                this.eat(Tag.Types.IDL_ID);
                readFactorRoot.addChildren(Tag.Types.IDL_ID);
                break;
            case SY_LEFT_PAR:
                this.eat(Tag.Types.SY_LEFT_PAR);
                readFactorRoot.addChildren(Tag.Types.SY_LEFT_PAR);
                Node readExpression = this.readExpression();
                readFactorRoot.addChildren(readExpression);
                this.eat(Tag.Types.SY_RIGHT_PAR);
                readFactorRoot.addChildren(Tag.Types.SY_RIGHT_PAR);
                break;
            case IDL_INTEGER_CONST:
                this.eat(Tag.Types.IDL_INTEGER_CONST);
                readFactorRoot.addChildren(Tag.Types.IDL_INTEGER_CONST);
                break;
            case IDL_FLOAT_CONST:
                this.eat(Tag.Types.IDL_FLOAT_CONST);
                readFactorRoot.addChildren(Tag.Types.IDL_FLOAT_CONST);
                break;
            case IDL_CHAR_CONST:
                this.eat(Tag.Types.IDL_CHAR_CONST);
                readFactorRoot.addChildren(Tag.Types.IDL_CHAR_CONST);
                break;
            default:
                this.showSyntaticError("Esperados um 'identificador', 'constante' ou 'separador ()'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }

        return readFactorRoot;
    }

    private Node readIfStatement() {
        Node rootIfStatement = new Node(Tag.Types.PARENT_NODE);
        Tag.Types[] elseEnds = {Tag.Types.RW_ELSE, Tag.Types.RW_END};
        Tag.Types[] ends = {Tag.Types.RW_END};
        this.eat(Tag.Types.RW_IF);
        rootIfStatement.addChildren(Tag.Types.RW_IF);
        Node expressionNode = this.readExpression();
        rootIfStatement.addChildren(expressionNode);
        this.eat(Tag.Types.RW_THEN);
        rootIfStatement.addChildren(Tag.Types.RW_THEN);
        Node statementsListNode = this.readStatementsList(elseEnds);
        rootIfStatement.addChildren(statementsListNode);
        if (this.currentLexeme.getType() == Tag.Types.RW_ELSE) {
            this.eat(Tag.Types.RW_ELSE);
            rootIfStatement.addChildren(Tag.Types.RW_ELSE);
            Node statementsListNode2 = this.readStatementsList(ends);
            rootIfStatement.addChildren(statementsListNode2);
        }
        this.eat(Tag.Types.RW_END);
        rootIfStatement.addChildren(Tag.Types.RW_END);
        return rootIfStatement;
    }

    private Node readWhileStatement(){
        Node rootWhileStatement = new Node(Tag.Types.PARENT_NODE);
    
        Tag.Types[] ends = {Tag.Types.RW_END};
        this.eat(Tag.Types.RW_WHILE);
        rootWhileStatement.addChildren(Tag.Types.RW_WHILE);
        Node expressionNode = this.readExpression();
        rootWhileStatement.addChildren(expressionNode);
        this.eat(Tag.Types.RW_DO);
        rootWhileStatement.addChildren(Tag.Types.RW_DO);
        Node statementsListNode = this.readStatementsList(ends);
        rootWhileStatement.addChildren(statementsListNode);
        this.eat(Tag.Types.RW_END);
        rootWhileStatement.addChildren(Tag.Types.RW_END);

        return rootWhileStatement;
    }

    private Node readRepeatStatement(){
        Node rootRepeatStatement = new Node(Tag.Types.PARENT_NODE);
        Tag.Types[] ends = {Tag.Types.RW_UNTIL};
        this.eat(Tag.Types.RW_REPEAT);
        rootRepeatStatement.addChildren(Tag.Types.RW_REPEAT);
        Node statementListNode = this.readStatementsList(ends);
        rootRepeatStatement.addChildren(statementListNode);
        this.eat(Tag.Types.RW_UNTIL);
        rootRepeatStatement.addChildren(Tag.Types.RW_UNTIL);
        Node expressionNode = this.readExpression();
        rootRepeatStatement.addChildren(expressionNode);
        return rootRepeatStatement;
    }

    private Node readReadStatement(){
        Node readReadStatementRoot = new Node(Tag.Types.PARENT_NODE);
        this.eat(Tag.Types.RW_READ);
        readReadStatementRoot.addChildren(Tag.Types.RW_READ);
        this.eat(Tag.Types.SY_LEFT_PAR);
        readReadStatementRoot.addChildren(Tag.Types.SY_LEFT_PAR);
        this.eat(Tag.Types.IDL_ID);
        readReadStatementRoot.addChildren(Tag.Types.IDL_ID);
        this.eat(Tag.Types.SY_RIGHT_PAR);
        readReadStatementRoot.addChildren(Tag.Types.SY_RIGHT_PAR);
        this.eat(Tag.Types.SY_SEMICOLON);
        readReadStatementRoot.addChildren(Tag.Types.SY_SEMICOLON);

        return readReadStatementRoot;
    }

    private Node readWriteStatement(){
        Node rootWriteStatement = new Node(Tag.Types.PARENT_NODE);

        this.eat(Tag.Types.RW_WRITE);
        rootWriteStatement.addChildren(Tag.Types.RW_WRITE);
        this.eat(Tag.Types.SY_LEFT_PAR);
        rootWriteStatement.addChildren(Tag.Types.SY_LEFT_PAR);

        if (this.currentLexeme.getType() == Tag.Types.IDL_LITERAL) {
            this.eat(Tag.Types.IDL_LITERAL);
            rootWriteStatement.addChildren(Tag.Types.IDL_LITERAL);
        } else {
            Node simpleExpressionNode = this.readSimpleExpression();
            rootWriteStatement.addChildren(simpleExpressionNode);
        }

        this.eat(Tag.Types.SY_RIGHT_PAR);
        rootWriteStatement.addChildren(Tag.Types.SY_RIGHT_PAR);
        this.eat(Tag.Types.SY_SEMICOLON);
        rootWriteStatement.addChildren(Tag.Types.SY_SEMICOLON);

        return rootWriteStatement;
    }
    
    private void eat(Tag.Types type) {
        if (this.currentLexeme.getType() == type) {
            System.out.println("Lendo " + this.currentLexeme.toString());
            this.advance();
            
        } else {
            
            this.showSyntaticError("Esperado '" + type + "', encontrado '" + this.currentLexeme.getToken()+"'");
        }
    }

    private void showSyntaticError(String msg) {
        System.out.println("Erro sintático na linha " + LexicalAnalyzer.line + ": " + msg);
        System.exit(0);
    }
}
