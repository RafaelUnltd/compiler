public class Tag {
    public enum Types {
        /* Arithmetic ops. */
        AO_ADD,
        AO_DIV,
        AO_MUL,
        AO_SUB,
        /* Logic ops. */
        LO_NOT,
        LO_OR,
        LO_AND,
        /* Relational ops. */
        RO_EQUAL,
        RO_GREATER_EQUAL,
        RO_LOWER_EQUAL,
        RO_GREATER,
        RO_LOWER,
        RO_NOT_EQUAL,
        /* Reserved words */
        RW_DO,
        RW_IF,
        RW_ELSE,
        RW_PROGRAM,
        RW_BEGIN,
        RW_END,
        RW_EXIT,
        RW_INT,
        RW_FLOAT,
        RW_PRINT,
        RW_SCAN,
        RW_START,
        RW_STRING,
        RW_THEN,
        RW_WHILE,
        /*symbols*/
        SY_ASSIGN,        
        SY_COMMA,		  
        SY_LEFT_PAR,	  
        SY_RIGHT_PAR,	  
        SY_SEMICOLON,     
	    SY_COLON,		  

        IDL_ID,
        IDL_INTEGER_CONST,
        IDL_FLOAT_CONST,
        IDL_CHAR_CONST,
        IDL_LITERAL;
    }

    public static String getTypeString(Types type) throws Exception {
        switch (type) {
            case AO_ADD:
                return "ADD";
            case AO_DIV:
                return "DIVIDED";
            case AO_MUL:
                return "MULTIPLIED";
            case AO_SUB:
                return "SUBTRACTED";
            case LO_NOT:
                return "NOT";
            case LO_OR:
                return "OR";
            case LO_AND:
                return "AND";
            case RO_EQUAL:
                return "EQUAL";
            case RO_GREATER_EQUAL:
                return "GREATER_EQUAL";
            case RO_LOWER_EQUAL:
                return "LOWER_EQUAL";
            case RO_GREATER:
                return "GREATER";
            case RO_LOWER:
                return "LOWER";
            case RO_NOT_EQUAL:
                return "NOT_EQUAL";
            case RW_DO:
                return "DO";
            case RW_IF:
                return "IF";
            case RW_FLOAT:
                return "FLOAT";
            case RW_PRINT:
                return "PRINT";
            case RW_SCAN:
                return "SCAN";
            case RW_START:
                return "START";
            case RW_STRING:
                return "STRING";
            case RW_THEN:
                return "THEN";
            case RW_WHILE:
                return "WHILE";
            case IDL_ID:
                return "ID";
            case IDL_INTEGER_CONST:
                return "INTEGER_CONST";
            case IDL_FLOAT_CONST:
                return "IDL_FLOAT_CONST";
            case IDL_CHAR_CONST:
                return "IDL_CHAR_CONST";
            case IDL_LITERAL:
                return "IDL_LITERAL";
            default:
                throw new Exception("Invalid token type.");
        }
    }
}
