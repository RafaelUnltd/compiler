import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SyntaticAnalyzer {
    private LexicalAnalyzer lexical;
    private Lexeme currentLexeme;

    public LexicalAnalyzer(LexicalAnalyzer lexical) {
        this.lexical = lexical;
    }

    public void start() {
        this.eat(Tag.Types.RW_PROGRAM);
        this.eat(Tag.Types.IDL_ID);
        // Declaration Statements until RW_BEGIN
        this.readDeclarationsList();
        this.eat(Tag.Types.RW_BEGIN);
        this.readStatementsList();
        // Statement List until RW_END
        this.eat(Tag.Types.RW_END);
    }

    private void advance() {
        this.currentLexeme = this.lexical.scan();
    }

    private void readDeclarationsList() {
        if (currentLexeme.getType == Tag.Types.RW_BEGIN) {
            return;
        }
        this.readDeclaration();
        this.readDeclarationsList();
    }

    private void readDeclaration() {
        this.readIdentifierList();
        this.eat(Tag.Types.RW_IS);
        
        switch(this.currentLexeme.getType()) {
            case Tag.Types.RW_INT:
                this.eat(Tag.Types.RW_INT);
                break;
            case Tag.Types.RW_FLOAT:
                this.eat(Tag.Types.RW_FLOAT);
                break;
            case Tag.Types.RW_CHAR:
                this.eat(Tag.Types.RW_CHAR);
                break;
            default:
                this.showSyntaticError();
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

    private void readStatement() {
        switch (this.currentLexeme.getType()){
            case Tag.Types.IDL_ID:
                this.readAssignmentStatement();
                break;
            case Tag.Types.RW_IF:
                this.readIfStatement();
                break;
            case Tag.Types.RW_WHILE:
                this.readWhileStatement();
                break;
            case Tag.Types.RW_REPEAT:
                this.readRepeatStatement();
                break;
            case Tag.Types.RW_READ:
                this.readReadStatement();
                break;
            case Tag.Types.RW_WRITE:
                this.readWriteStatement();
                break;
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
        while (
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
            case Tag.Types.RO_EQUAL:
                this.eat(Tag.Types.RO_EQUAL);
                break;
            case Tag.Types.RO_GREATER_EQUAL:
                this.eat(Tag.Types.RO_GREATER_EQUAL);
                break;
            case Tag.Types.RO_LOWER_EQUAL:
                this.eat(Tag.Types.RO_LOWER_EQUAL);
                break;
            case Tag.Types.RO_GREATER:
                this.eat(Tag.Types.RO_GREATER);
                break;
            case Tag.Types.RO_LOWER:
                this.eat(Tag.Types.RO_LOWER);
                break;
            case Tag.Types.RO_NOT_EQUAL:
                this.eat(Tag.Types.RO_NOT_EQUAL);
                break;
            default:
                this.showSyntaticError();
                break;
        }
    }

    private void readAddOp () {
        switch (this.currentLexeme.getType()) {
            case Tag.Types.AO_ADD:
                this.eat(Tag.Types.AO_ADD);
                break;
            case Tag.Types.AO_SUB:
                this.eat(Tag.Types.AO_SUB);
                break;
            case Tag.Types.LO_OR:
                this.eat(Tag.Types.LO_OR);
                break;
            default:
                this.showSyntaticError();
                break;
        }
    }

    private void readMulOp () {
        switch (this.currentLexeme.getType()) {
            case Tag.Types.AO_MUL:
                this.eat(Tag.Types.AO_MUL);
                break;
            case Tag.Types.AO_DIV:
                this.eat(Tag.Types.AO_DIV);
                break;
            case Tag.Types.LO_AND:
                this.eat(Tag.Types.LO_AND);
                break;
            default:
                this.showSyntaticError();
                break;
        }
    }

    private void readFactorA () {
        switch (this.currentLexeme.getType()) {
            case Tag.Types.IDL_ID:
            case Tag.Types.SY_LEFT_PAR:
            case Tag.Types.IDL_INTEGER_CONST:
            case Tag.Types.IDL_FLOAT_CONST:
            case Tag.Types.IDL_CHAR_CONST:
                this.readFactor();
                break;
            case Tag.Types.LO_NOT:
                this.eat(Tag.Types.LO_NOT);
                this.readFactor();
                break;
            case Tag.Types.AO_SUB:
                this.eat(Tag.Types.AO_SUB);
                this.readFactor();
                break;
            default:
                this.showSyntaticError();
                break;
        }
    }

    private void readFactor () {
        switch (this.currentLexeme.getType()) {
            case Tag.Types.IDL_ID:
                this.eat(Tag.Types.IDL_ID);
                break;
            case Tag.Types.SY_LEFT_PAR:
                this.eat(Tag.Types.SY_LEFT_PAR);
                this.readExpression();
                this.eat(Tag.Types.SY_RIGHT_PAR);
                break;
            case Tag.Types.IDL_INTEGER_CONST:
                this.eat(Tag.Types.IDL_INTEGER_CONST);
                break;
            case Tag.Types.IDL_FLOAT_CONST:
                this.eat(Tag.Types.IDL_FLOAT_CONST);
                break;
            case Tag.Types.IDL_CHAR_CONST:
                this.eat(Tag.Types.IDL_CHAR_CONST);
                break;
            default:
                this.showSyntaticError();
                break;
        }
    }

    private void readIfStatement(){
        this.eat(Tag.Types.RW_IF);
        this.readExpression();
    }

    private void readWhileStatement(){

    }

    private void readRepeatStatement(){

    }

    private void readReadStatement(){
        this.eat(Tag.Types.RW_READ);
        this.eat(Tag.Types.SY_LEFT_PAR);
        this.eat(Tag.Types.IDL_ID);
        this.eat(Tag.Types.SY_RIGHT_PAR);
    }

    private void readWriteStatement(){
        this.eat(Tag.Types.RW_WRITE);
        this.eat(Tag.Types.SY_LEFT_PAR);
        
        this.currentLexeme.getType() == Tag.Types.IDL_LITERAL ?  this.eat(Tag.Types.IDL_LITERAL) : this.readSimpleExpression();

        this.eat(Tag.Types.SY_RIGHT_PAR);
    }
    
    private void eat(Tag.Types type) {
        if (this.currentLexeme.getType() == type) {
            this.advance();
        } else {
            this.showSyntaticError();
        }
    }

    private void showSyntaticError() {
        System.out.println("Erro sintático inesperado");
    }
}
