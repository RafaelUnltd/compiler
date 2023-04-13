import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Tag.Types> symbols;

    public SymbolTable() {
        symbols = new HashMap<String, Tag.Types>();

        symbols.put("+", Tag.Types.AO_ADD);
        symbols.put("-", Tag.Types.AO_SUB);
        symbols.put("*", Tag.Types.AO_MUL);
        symbols.put("/", Tag.Types.AO_DIV);

        symbols.put("!", Tag.Types.LO_NOT);
        symbols.put("&&", Tag.Types.LO_OR);
        symbols.put("||", Tag.Types.LO_AND); 

        symbols.put("==", Tag.Types.RO_EQUAL);
        symbols.put(">=", Tag.Types.RO_GREATER_EQUAL);
        symbols.put("<=", Tag.Types.RO_LOWER_EQUAL);
        symbols.put(">", Tag.Types.RO_GREATER);
        symbols.put("<", Tag.Types.RO_LOWER);
        symbols.put("<>", Tag.Types.RO_NOT_EQUAL);

        symbols.put("do", Tag.Types.RW_DO);
        symbols.put("if", Tag.Types.RW_IF);
        symbols.put("else", Tag.Types.RW_ELSE);
        symbols.put("program", Tag.Types.RW_PROGRAM);
        symbols.put("begin", Tag.Types.RW_BEGIN);
        symbols.put("end", Tag.Types.RW_END);
        symbols.put("exit", Tag.Types.RW_EXIT);
        symbols.put("int", Tag.Types.RW_INT);
        symbols.put("float", Tag.Types.RW_FLOAT);
        symbols.put("print", Tag.Types.RW_PRINT); 
        symbols.put("scan", Tag.Types.RW_SCAN); 
        symbols.put("start", Tag.Types.RW_START); 
        symbols.put("string", Tag.Types.RW_STRING); 
        symbols.put("then", Tag.Types.RW_THEN); 
        symbols.put("while", Tag.Types.RW_WHILE);

        symbols.put("=", Tag.Types.SY_ASSIGN); 
        symbols.put(",", Tag.Types.SY_COMMA); 		  
        symbols.put("(", Tag.Types.SY_LEFT_PAR); 
        symbols.put(")", Tag.Types.SY_RIGHT_PAR); 
        symbols.put(";", Tag.Types.SY_SEMICOLON);    
    }

    public Boolean hasSymbol(String symbolKey){
        return symbols.containsKey(symbolKey);
    }
    
    public Tag.Types getSymbol(String symbolKey) {
        return symbols.get(symbolKey);
    }

    public Lexeme getLexeme(String symbolKey) {
        return new Lexeme(symbolKey, symbols.get(symbolKey));
    }

    public void addSymbol(Lexeme lexeme) {
        symbols.put(lexeme.getToken(), lexeme.getType());
    }
}