package it.polimi.se2018.utils;

public enum NumberEnum {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);

    int n;

    NumberEnum(int n) {
        this.n = n;
    }

    public int getNumber() {
        return n;
    }
}
