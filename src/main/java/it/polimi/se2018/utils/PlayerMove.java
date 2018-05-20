package it.polimi.se2018.utils;

import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;

import java.util.Optional;

public class PlayerMove {
    private Tool tool;
    private Optional<Integer> row;
    private Optional<Integer> column;
    private Optional<Integer> draftPosition;
    private Optional<Boolean> aumentaValoreDado;
    private Optional<Integer> roundTrackRound;
    private Optional<Integer> roundTrackPosition;
    private Optional<NumberEnum> newDiceValue;
    private Optional<Integer> finalRow;
    private Optional<Integer> finalColumn;
    private Optional<PlayerMove> nextMove; //per la tool che permette di lanciare FINO a due dadi


    public PlayerMove(Tool tool) {
        if (tool != Tool.SKIPTURN && tool != Tool.MARTELLETTO && tool != Tool.TENAGLIAAROTELLE)
            throw new IllegalArgumentException();
        this.tool = tool;
    }

    public PlayerMove(Tool tool, int row, int column, int draftPosition) {

        if (tool != Tool.MOSSASTANDARD && tool != Tool.RIGAINSUGHERO)
            throw new IllegalArgumentException();

        this.tool = tool;
        this.row = Optional.of(row);
        this.column = Optional.of(column);
        this.draftPosition = Optional.of(draftPosition);
    }

    public PlayerMove(Tool tool, int draftPosition, boolean aumentaDiUno) {
        if (tool != Tool.PINZASGROSSATRICE)
            throw new IllegalArgumentException();

        this.tool = tool;
        this.draftPosition = Optional.of(draftPosition);
        this.aumentaValoreDado = Optional.of(aumentaDiUno);
    }

    public PlayerMove(Tool tool, int row, int column, int finalRow, int finalColumn) {

        if (tool != Tool.PENNELLOPEREGLOMISE && tool != Tool.ALESATOREPERLAMINADIRAME && tool != Tool.LATHEKIN && tool != Tool.TAGLIERINAMANUALE)
            throw new IllegalArgumentException();
        this.tool = tool;
        this.row = Optional.of(row);
        this.column = Optional.of(column);
        this.finalRow = Optional.of(finalRow);
        this.finalColumn = Optional.of(finalColumn);
    }

    public PlayerMove(Tool tool, int row, int column, int finalRow, int finalColumn, PlayerMove nextMove) {

        if (tool != Tool.TAGLIERINAMANUALE && tool != Tool.LATHEKIN)
            throw new IllegalArgumentException();
        this.tool = tool;
        this.row = Optional.of(row);
        this.column = Optional.of(column);
        this.finalRow = Optional.of(finalRow);
        this.finalColumn = Optional.of(finalColumn);
        this.nextMove = Optional.of(nextMove);
    }


    public PlayerMove(int draftPosition, int roundTrackRound, int roundTrackPosition, Tool tool) {

        if (tool != Tool.TAGLIERINACIRCOLARE)
            throw new IllegalArgumentException();

        this.tool = tool;
        this.draftPosition = Optional.of(draftPosition);
        this.roundTrackPosition = Optional.of(roundTrackPosition);
        this.roundTrackRound = Optional.of(roundTrackRound);

    }

    public PlayerMove(int row, int column, int draftPosition, NumberEnum newDiceValue, Tool tool) {

        if (tool != Tool.PENNELLOPERPASTASALDA && tool != Tool.DILUENTEPERPASTASALDA)
            throw new IllegalArgumentException();


        this.tool = tool;
        this.row = Optional.of(row);
        this.column = Optional.of(column);
        this.draftPosition = Optional.of(draftPosition);
        this.newDiceValue = Optional.of(newDiceValue);

    }

    public PlayerMove(int draftPosition, NumberEnum newDiceValue, Tool tool) {

        if (tool != Tool.PENNELLOPERPASTASALDA)
            throw new IllegalArgumentException();


        this.tool = tool;
        this.draftPosition = Optional.of(draftPosition);
        this.newDiceValue = Optional.of(newDiceValue);

    }


    public PlayerMove(Tool tool, int draftPosition) {

        if (tool != Tool.TAMPONEDIAMANTATO)
            throw new IllegalArgumentException();


        this.tool = tool;
        this.draftPosition = Optional.of(draftPosition);

    }

    public Tool getTool() {
        return tool;
    }

    public Optional<Integer> getRow() {
        return row;
    }

    public Optional<Integer> getColumn() {
        return column;
    }

    public Optional<Integer> getDraftPosition() {
        return draftPosition;
    }

    public Optional<Boolean> getAumentaValoreDado() {
        return aumentaValoreDado;
    }

    public Optional<Integer> getRoundTrackRound() {
        return roundTrackRound;
    }

    public Optional<Integer> getRoundTrackPosition() {
        return roundTrackPosition;
    }

    public Optional<NumberEnum> getNewDiceValue() {
        return newDiceValue;
    }

    public Optional<Integer> getFinalRow() {
        return finalRow;
    }

    public Optional<Integer> getFinalColumn() {
        return finalColumn;
    }

    public Optional<PlayerMove> getNextMove() {
        return nextMove;
    }
}
