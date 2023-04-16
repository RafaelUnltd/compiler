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
        RW_INT,
        RW_FLOAT,
        RW_CHAR,
        RW_WRITE,
        RW_READ,
        RW_THEN,
        RW_WHILE,
        RW_IS,
        RW_REPEAT,
        RW_UNTIL,
        /*symbols*/
        SY_ASSIGN,        
        SY_COMMA,		  
        SY_LEFT_PAR,	  
        SY_RIGHT_PAR,	
        SY_LEFT_BRA,	  
        SY_RIGHT_BRA,	  
        SY_SEMICOLON,     
	    SY_COLON,		  

        IDL_ID,
        IDL_INTEGER_CONST,
        IDL_FLOAT_CONST,
        IDL_CHAR_CONST,
        IDL_LITERAL,

        VOID_VALUE;
    }
}
