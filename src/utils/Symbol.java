package src.utils;

public class Symbol {

    public String type;
    public String scope;
    public boolean initialized;
    public boolean used;

    public Symbol(String type, String scope, boolean initialized, boolean used) {
        this.type = type;
        this.scope = scope;
        this.initialized = initialized;
        this.used = used;
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