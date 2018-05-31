package it.polimi.se2018.utils.enums;

public enum ErrorType {
    ILLEGALMOVE("Hai eseguito una mossa illegale"),
    NOTYOURTURN("Non è il tuo turno");

    String str;

    ErrorType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
