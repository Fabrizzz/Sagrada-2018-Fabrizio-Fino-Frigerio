package it.polimi.se2018.utils;

import it.polimi.se2018.utils.enums.NumberEnum;
import it.polimi.se2018.utils.enums.Tool;

import java.util.Optional;

public class PlayerMove {
    private Tool tool;
    private Integer row;
    private Integer column;
    private Integer draftPosition;
    private Boolean aumentaValoreDado;
    private Integer roundTrackRound;
    private Integer roundTrackPosition;
    private NumberEnum newDiceValue;
    private Integer finalRow;
    private Integer finalColumn;
    private PlayerMove nextMove; //per la tool che permette di lanciare FINO a due dadi


    public PlayerMove(Tool tool) {
        if (tool != Tool.SKIPTURN && tool != Tool.MARTELLETTO && tool != Tool.TENAGLIAAROTELLE)
            throw new IllegalArgumentException();
        this.tool = tool;
    }

    public PlayerMove(Tool tool, int row, int column, int draftPosition) {

        if (tool != Tool.MOSSASTANDARD && tool != Tool.RIGAINSUGHERO)
            throw new IllegalArgumentException();

        this.tool = tool;
        this.row = row;
        this.column = column;
        this.draftPosition = draftPosition;
    }

    public PlayerMove(Tool tool, int draftPosition, boolean aumentaDiUno) {
        if (tool != Tool.PINZASGROSSATRICE)
            throw new IllegalArgumentException();

        this.tool = tool;
        this.draftPosition = draftPosition;
        this.aumentaValoreDado = aumentaDiUno;
    }

    public PlayerMove(Tool tool, int row, int column, int finalRow, int finalColumn) {

        if (tool != Tool.PENNELLOPEREGLOMISE && tool != Tool.ALESATOREPERLAMINADIRAME && tool != Tool.LATHEKIN && tool != Tool.TAGLIERINAMANUALE)
            throw new IllegalArgumentException();
        this.tool = tool;
        this.row = row;
        this.column = column;
        this.finalRow = finalRow;
        this.finalColumn = finalColumn;
    }

    public PlayerMove(Tool tool, int row, int column, int finalRow, int finalColumn, PlayerMove nextMove) {

        if (tool != Tool.TAGLIERINAMANUALE && tool != Tool.LATHEKIN)
            throw new IllegalArgumentException();
        this.tool = tool;
        this.row = row;
        this.column = column;
        this.finalRow = finalRow;
        this.finalColumn = finalColumn;
        this.nextMove = nextMove;
    }


    public PlayerMove(int draftPosition, int roundTrackRound, int roundTrackPosition, Tool tool) {

        if (tool != Tool.TAGLIERINACIRCOLARE)
            throw new IllegalArgumentException();

        this.tool = tool;
        this.draftPosition = draftPosition;
        this.roundTrackPosition = roundTrackPosition;
        this.roundTrackRound = roundTrackRound;

    }

    public PlayerMove(int row, int column, int draftPosition, NumberEnum newDiceValue, Tool tool) {

        if (tool != Tool.PENNELLOPERPASTASALDA && tool != Tool.DILUENTEPERPASTASALDA)
            throw new IllegalArgumentException();


        this.tool = tool;
        this.row = row;
        this.column = column;
        this.draftPosition = draftPosition;
        this.newDiceValue = newDiceValue;

    }

    public PlayerMove(int draftPosition, NumberEnum newDiceValue, Tool tool) {

        if (tool != Tool.PENNELLOPERPASTASALDA)
            throw new IllegalArgumentException();


        this.tool = tool;
        this.draftPosition = draftPosition;
        this.newDiceValue = newDiceValue;

    }


    public PlayerMove(Tool tool, int draftPosition) {

        if (tool != Tool.TAMPONEDIAMANTATO)
            throw new IllegalArgumentException();


        this.tool = tool;
        this.draftPosition = draftPosition;

    }

    public Tool getTool() {
        return tool;
    }

    public Optional<Integer> getRow() {
        if (row == null)
            return Optional.empty();
        return Optional.of(row);
    }

    public Optional<Integer> getColumn() {
        if (column == null)
            return Optional.empty();
        return Optional.of(column);
    }

    public Optional<Integer> getDraftPosition() {
        if (draftPosition == null)
            return Optional.empty();
        return Optional.of(draftPosition);
    }

    public Optional<Boolean> getAumentaValoreDado() {
        if (aumentaValoreDado == null)
            return Optional.empty();
        return Optional.of(aumentaValoreDado);
    }

    public Optional<Integer> getRoundTrackRound() {
        if (roundTrackRound == null)
            return Optional.empty();
        return Optional.of(roundTrackRound);
    }

    public Optional<Integer> getRoundTrackPosition() {
        if (roundTrackPosition == null)
            return Optional.empty();
        return Optional.of(roundTrackPosition);
    }

    public Optional<NumberEnum> getNewDiceValue() {
        if (newDiceValue == null)
            return Optional.empty();
        return Optional.of(newDiceValue);
    }

    public Optional<Integer> getFinalRow() {
        if (finalRow == null)
            return Optional.empty();
        return Optional.of(finalRow);
    }

    public Optional<Integer> getFinalColumn() {
        if (finalColumn == null)
            return Optional.empty();
        return Optional.of(finalColumn);
    }

    public Optional<PlayerMove> getNextMove() {
        if (nextMove == null)
            return Optional.empty();
        return Optional.of(nextMove);
    }
}
