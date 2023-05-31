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
        symbols.put("&&", Tag.Types.LO_AND);
        symbols.put("||", Tag.Types.LO_OR); 

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
        symbols.put("int", Tag.Types.RW_INT);
        symbols.put("float", Tag.Types.RW_FLOAT);
        symbols.put("char", Tag.Types.RW_CHAR);
        symbols.put("write", Tag.Types.RW_WRITE); 
        symbols.put("read", Tag.Types.RW_READ);   
        symbols.put("then", Tag.Types.RW_THEN); 
        symbols.put("while", Tag.Types.RW_WHILE);
        symbols.put("is", Tag.Types.RW_IS);
        symbols.put("repeat", Tag.Types.RW_REPEAT);
        symbols.put("until", Tag.Types.RW_UNTIL);

        symbols.put("=", Tag.Types.SY_ASSIGN); 
        symbols.put(",", Tag.Types.SY_COMMA); 		  
        symbols.put("(", Tag.Types.SY_LEFT_PAR); 
        symbols.put(")", Tag.Types.SY_RIGHT_PAR); 
        symbols.put("{", Tag.Types.SY_LEFT_BRA); 
        symbols.put("}", Tag.Types.SY_RIGHT_BRA); 
        symbols.put(";", Tag.Types.SY_SEMICOLON);    
    }

    public Boolean hasSymbol(String symbolKey){
        return symbols.containsKey(symbolKey);
    }
    
    public Tag.Types getSymbol(String symbolKey) {
        return symbols.get(symbolKey);
    }

    public Lexeme getLexeme(String symbolKey) {
        Tag.Types obj = symbols.get(symbolKey);

        if (obj == null) {
            return null;
        }

        return new Lexeme(symbolKey, obj);
    }

    public void addSymbol(Lexeme lexeme) {
        symbols.put(lexeme.getToken(), lexeme.getType());
    }
}