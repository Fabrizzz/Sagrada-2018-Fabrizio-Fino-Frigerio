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

    public static NumberEnum getNumber(int n) {
        return NumberEnum.values()[n - 1];

    }

    public int getInt() {
        return n;
    }
}
