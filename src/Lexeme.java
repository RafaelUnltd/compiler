package src;

public class Lexeme {
    private String token;
    private Tag.Types type;

    public Lexeme(String token, Tag.Types type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Tag.Types getType() {
        return type;
    }

    @Override
    public String toString() {
        String lexemeString = "<" + token + ", ";

        try {
            lexemeString += type + ">";
        } catch (Exception e) {
            lexemeString += "INVALID>";
        }

        return lexemeString;
    }
}
