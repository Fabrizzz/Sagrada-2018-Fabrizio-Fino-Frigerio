package it.polimi.se2018.model;

import java.io.Serializable;
import java.util.*;

public class ModelViewUpdate extends Observable implements Observer, Serializable {


    private DiceBag diceBag;
    private DraftPool draftPool;
    private RoundTrack roundTrack;
    private List<Player> players;
    private Map<Player, PlayerBoard> boardMap;

    private Integer round = 0;
    private Boolean firstTurn = false;
    private Boolean usedTool = false;
    private Boolean normalMove = false;


    @Override
    public void update(Observable o, Object arg) {
        Model model = (Model) o;

        if (model.getDiceBag().isChanged())
            diceBag = model.getDiceBag();
        else
            diceBag = null;
        if (model.getDraftPool().isChanged())
            draftPool = model.getDraftPool();
        else
            draftPool = null;
        if (model.getRoundTrack().isChanged())
            roundTrack = model.getRoundTrack();
        else
            roundTrack = null;


        boardMap = null;
        players = null;
        for (Player player : model.getBoardMap().keySet()) {
            if (model.getBoard(player).isChanged()) {
                boardMap = model.getBoardMap();
                players = model.getPlayers();
                break;
            }
        }


        for (Player player : model.getPlayers()) {
            if (player.isChanged() && players == null) {
                boardMap = model.getBoardMap();
                players = model.getPlayers();
                break;
            }
        }

        if (model.getRound() != round)
            round = model.getRound();
        else
            round = null;

        if (model.isFirstTurn() != firstTurn)
            firstTurn = model.isFirstTurn();
        else
            firstTurn = null;

        if (model.hasUsedTool() != usedTool)
            usedTool = model.hasUsedTool();
        else
            usedTool = null;

        if (model.hasUsedNormalMove() != normalMove)
            normalMove = model.hasUsedNormalMove();
        else
            normalMove = null;

        setChanged();
        notifyObservers();
        round = model.getRound();
        firstTurn = model.isFirstTurn();
        usedTool = model.hasUsedTool();
        normalMove = model.hasUsedNormalMove();
    }

    public Optional<DiceBag> getDiceBag() {
        return Optional.ofNullable(diceBag);
    }

    public Optional<DraftPool> getDraftPool() {
        return Optional.ofNullable(draftPool);
    }

    public Optional<RoundTrack> getRoundTrack() {
        return Optional.ofNullable(roundTrack);
    }

    public Optional<List<Player>> getPlayers() {
        return Optional.ofNullable(players);
    }

    public Optional<Map<Player, PlayerBoard>> getBoardMap() {
        return Optional.ofNullable(boardMap);
    }

    public Optional<Integer> getRound() {
        return Optional.ofNullable(round);
    }

    public Optional<Boolean> isFirstTurn() {
        return Optional.ofNullable(firstTurn);
    }

    public Optional<Boolean> isUsedTool() {
        return Optional.ofNullable(usedTool);
    }

    public Optional<Boolean> isNormalMove() {
        return Optional.ofNullable(normalMove);
    }


}
