package src.utils;

public class Symbol {

    String type;
    String scope;

    public Symbol(String type, String scope) {
        this.type = type;
        this.scope = scope;
    }

    @Override
    public String toString() {
        if (type != null && scope != null) {
            return String.format("%s, %s", type, scope);
        }
        if (scope == null && type != null) {
            return String.format("%s", type);
        }

        if (scope != null && type == null) {
            return String.format("%s", scope);
        }
        return "";
    }
}