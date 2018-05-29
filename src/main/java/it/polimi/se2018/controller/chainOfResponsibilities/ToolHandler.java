package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.model.Model;
import it.polimi.se2018.model.Player;
import it.polimi.se2018.utils.enums.Tool;

public abstract class ToolHandler extends Handler {

    protected boolean canUseTool(Player player, Model model, Tool tool) {
        boolean alreadyUsed = model.getTools().get(tool);
        return !(model.hasUsedTool() || (alreadyUsed && player.getFavorTokens() < 2) || (!alreadyUsed && player.getFavorTokens() < 1));
    }

    protected void completeTool(Player player, Model model, Tool tool) {
        boolean alreadyUsed = model.getTools().get(tool);
        player.setFavorTokens(player.getFavorTokens() - (alreadyUsed ? 2 : 1));
        model.getTools().put(tool, true);
        model.setUsedTool(true);
    }

}
