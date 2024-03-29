package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.enums.Tool;

import java.util.logging.Level;

public abstract class ToolHandler extends Handler {

    protected boolean cantUseTool(Player player, Model model, Tool tool) {
        boolean alreadyUsed = model.getTools().get(tool);
        LOGGER.log(Level.FINEST, "This tool has already been used = " + alreadyUsed);
        LOGGER.log(Level.FINEST, "Tha player has already used the tool = " + model.hasUsedTool());
        LOGGER.log(Level.FINEST, "getFavorTokens < 2 = " + player.getFavorTokens());
        return (model.hasUsedTool() || (alreadyUsed && player.getFavorTokens() < 2) || (!alreadyUsed && player.getFavorTokens() < 1));
    }

    protected void completeTool(Player player, Model model, Tool tool) {
        boolean alreadyUsed = model.getTools().get(tool);
        player.setFavorTokens(player.getFavorTokens() - (alreadyUsed ? 2 : 1));
        model.getTools().put(tool, true);
        model.setUsedTool(true);
    }

}
