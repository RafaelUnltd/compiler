import java.io.IOException;

public class SyntaticAnalyzer {
    private LexicalAnalyzer lexical;
    private Lexeme currentLexeme;

    public SyntaticAnalyzer(LexicalAnalyzer lexical) {
        this.lexical = lexical;
        this.advance();
    }

    public void start() {
        Tag.Types[] ends = {Tag.Types.RW_END};
        this.eat(Tag.Types.RW_PROGRAM);
        this.eat(Tag.Types.IDL_ID);
        // Declaration Statements until RW_BEGIN
        this.readDeclarationsList();
        this.eat(Tag.Types.RW_BEGIN);
        this.readStatementsList(ends);
        // Statement List until RW_END
        this.eat(Tag.Types.RW_END);
    }

    private void advance() {
        try {
            this.currentLexeme = this.lexical.scan();
        } catch (IOException e) {
            System.out.println("Erro ao chamar método de Scan");
            System.exit(1);
        }
    }

    private void readDeclarationsList() {
        if (this.currentLexeme.getType() == Tag.Types.RW_BEGIN) {
            return;
        }
        this.readDeclaration();
        this.readDeclarationsList();
    }

    private void readDeclaration() {
        this.readIdentifierList();
        this.eat(Tag.Types.RW_IS);
        
        switch(this.currentLexeme.getType()) {
            case RW_INT:
                this.eat(Tag.Types.RW_INT);
                break;
            case RW_FLOAT:
                this.eat(Tag.Types.RW_FLOAT);
                break;
            case RW_CHAR:
                this.eat(Tag.Types.RW_CHAR);
                break;
            default:
                this.showSyntaticError("Esperado 'int', 'float' ou 'char'. Encontrado '" + this.currentLexeme.getToken() + "'");
        }

        this.eat(Tag.Types.SY_SEMICOLON);
    }

    private void readIdentifierList() {
        this.eat(Tag.Types.IDL_ID);
        while (this.currentLexeme.getType() == Tag.Types.SY_COMMA) {
            this.eat(Tag.Types.SY_COMMA);
            this.eat(Tag.Types.IDL_ID);
        }
    }

    private void readStatementsList(Tag.Types[] expectedEnds) {
        boolean reachedEnd = false;

        for (int i = 0; i < expectedEnds.length; i++) {
            if (this.currentLexeme.getType() == expectedEnds[i]) {
                reachedEnd = true;
                break;
            }
        }

        if (reachedEnd) {
            return;
        }

        this.readStatement();
        this.readStatementsList(expectedEnds);
    }

    private void readStatement() {
        switch (this.currentLexeme.getType()) {
            case IDL_ID:
                this.readAssignmentStatement();
                break;
            case RW_IF:
                this.readIfStatement();
                break;
            case RW_WHILE:
                this.readWhileStatement();
                break;
            case RW_REPEAT:
                this.readRepeatStatement();
                break;
            case RW_READ:
                this.readReadStatement();
                break;
            case RW_WRITE:
                this.readWriteStatement();
                break;
            default:
                this.showSyntaticError("Esperado 'identificador', 'if', 'while', 'repeat', 'read' ou 'write'. Encontrado '" + this.currentLexeme.getToken() + "'");
        }
    }

    private void readAssignmentStatement() {
        this.eat(Tag.Types.IDL_ID);
        this.eat(Tag.Types.SY_ASSIGN);
        this.readSimpleExpression();
        this.eat(Tag.Types.SY_SEMICOLON);
    }

    private void readExpression () {
        this.readSimpleExpression();
        if (
            this.currentLexeme.getType() == Tag.Types.RO_EQUAL ||
            this.currentLexeme.getType() == Tag.Types.RO_GREATER_EQUAL ||
            this.currentLexeme.getType() == Tag.Types.RO_LOWER_EQUAL ||
            this.currentLexeme.getType() == Tag.Types.RO_GREATER ||
            this.currentLexeme.getType() == Tag.Types.RO_LOWER ||
            this.currentLexeme.getType() == Tag.Types.RO_NOT_EQUAL
        ) {
            this.readRelOp();
            this.readSimpleExpression();
        }
    }

    private void readSimpleExpression () {
        this.readTerm();
        while (
            this.currentLexeme.getType() == Tag.Types.AO_ADD ||
            this.currentLexeme.getType() == Tag.Types.AO_SUB ||
            this.currentLexeme.getType() == Tag.Types.LO_OR
        ) {
            this.readAddOp();
            this.readTerm();
        }
    }

    private void readTerm () {
        this.readFactorA();
        while (
            this.currentLexeme.getType() == Tag.Types.AO_MUL ||
            this.currentLexeme.getType() == Tag.Types.AO_DIV ||
            this.currentLexeme.getType() == Tag.Types.LO_AND    
        ) {
            this.readMulOp();
            this.readFactorA();
        }
    }

    private void readRelOp () {
        switch (this.currentLexeme.getType()) {
            case RO_EQUAL:
                this.eat(Tag.Types.RO_EQUAL);
                break;
            case RO_GREATER_EQUAL:
                this.eat(Tag.Types.RO_GREATER_EQUAL);
                break;
            case RO_LOWER_EQUAL:
                this.eat(Tag.Types.RO_LOWER_EQUAL);
                break;
            case RO_GREATER:
                this.eat(Tag.Types.RO_GREATER);
                break;
            case RO_LOWER:
                this.eat(Tag.Types.RO_LOWER);
                break;
            case RO_NOT_EQUAL:
                this.eat(Tag.Types.RO_NOT_EQUAL);
                break;
            default:
                this.showSyntaticError("Esperado '==', '>=', '<=', '>', '<' ou '<>'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }
    }

    private void readAddOp () {
        switch (this.currentLexeme.getType()) {
            case AO_ADD:
                this.eat(Tag.Types.AO_ADD);
                break;
            case AO_SUB:
                this.eat(Tag.Types.AO_SUB);
                break;
            case LO_OR:
                this.eat(Tag.Types.LO_OR);
                break;
            default:
                this.showSyntaticError("Esperado '+', '-' ou '||'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }
    }

    private void readMulOp () {
        switch (this.currentLexeme.getType()) {
            case AO_MUL:
                this.eat(Tag.Types.AO_MUL);
                break;
            case AO_DIV:
                this.eat(Tag.Types.AO_DIV);
                break;
            case LO_AND:
                this.eat(Tag.Types.LO_AND);
                break;
            default:
                this.showSyntaticError("Esperado '*', '/' ou '&&'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }
    }

    private void readFactorA () {
        switch (this.currentLexeme.getType()) {
            case IDL_ID:
            case SY_LEFT_PAR:
            case IDL_INTEGER_CONST:
            case IDL_FLOAT_CONST:
            case IDL_CHAR_CONST:
                this.readFactor();
                break;
            case LO_NOT:
                this.eat(Tag.Types.LO_NOT);
                this.readFactor();
                break;
            case AO_SUB:
                this.eat(Tag.Types.AO_SUB);
                this.readFactor();
                break;
            default:
                this.showSyntaticError("Esperados um 'identificador', 'constante', 'separador ()' ou 'operador - / !'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }
    }

    private void readFactor () {
        switch (this.currentLexeme.getType()) {
            case IDL_ID:
                this.eat(Tag.Types.IDL_ID);
                break;
            case SY_LEFT_PAR:
                this.eat(Tag.Types.SY_LEFT_PAR);
                this.readExpression();
                this.eat(Tag.Types.SY_RIGHT_PAR);
                break;
            case IDL_INTEGER_CONST:
                this.eat(Tag.Types.IDL_INTEGER_CONST);
                break;
            case IDL_FLOAT_CONST:
                this.eat(Tag.Types.IDL_FLOAT_CONST);
                break;
            case IDL_CHAR_CONST:
                this.eat(Tag.Types.IDL_CHAR_CONST);
                break;
            default:
                this.showSyntaticError("Esperados um 'identificador', 'constante' ou 'separador ()'. Encontrado '" + this.currentLexeme.getToken() + "'");
                break;
        }
    }

    private void readIfStatement() {
        Tag.Types[] elseEnds = {Tag.Types.RW_ELSE, Tag.Types.RW_END};
        Tag.Types[] ends = {Tag.Types.RW_END};
        this.eat(Tag.Types.RW_IF);
        this.readExpression();
        this.eat(Tag.Types.RW_THEN);
        this.readStatementsList(elseEnds);
        if (this.currentLexeme.getType() == Tag.Types.RW_ELSE) {
            this.eat(Tag.Types.RW_ELSE);
            this.readStatementsList(ends);
        }
        this.eat(Tag.Types.RW_END);
    }

    private void readWhileStatement(){
        Tag.Types[] ends = {Tag.Types.RW_END};
        this.eat(Tag.Types.RW_WHILE);
        this.readExpression();
        this.eat(Tag.Types.RW_DO);
        this.readStatementsList(ends);
        this.eat(Tag.Types.RW_END);
    }

    private void readRepeatStatement(){
        Tag.Types[] ends = {Tag.Types.RW_UNTIL};
        this.eat(Tag.Types.RW_REPEAT);
        this.readStatementsList(ends);
        this.eat(Tag.Types.RW_UNTIL);
        this.readExpression();
    }

    private void readReadStatement(){
        this.eat(Tag.Types.RW_READ);
        this.eat(Tag.Types.SY_LEFT_PAR);
        this.eat(Tag.Types.IDL_ID);
        this.eat(Tag.Types.SY_RIGHT_PAR);
        this.eat(Tag.Types.SY_SEMICOLON);
    }

    private void readWriteStatement(){
        this.eat(Tag.Types.RW_WRITE);
        this.eat(Tag.Types.SY_LEFT_PAR);

        if (this.currentLexeme.getType() == Tag.Types.IDL_LITERAL) {
            this.eat(Tag.Types.IDL_LITERAL);
        } else {
            this.readSimpleExpression();
        }

        this.eat(Tag.Types.SY_RIGHT_PAR);
        this.eat(Tag.Types.SY_SEMICOLON);
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
