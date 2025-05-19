package src.utils;

public class Symbol {

    public String type;
    public int scope;
    public boolean initialized;
    public boolean used;

    public Symbol(String type, int scope, boolean initialized, boolean used) {
        this.type = type;
        this.scope = scope;
        this.initialized = initialized;
        this.used = used;
    }
}