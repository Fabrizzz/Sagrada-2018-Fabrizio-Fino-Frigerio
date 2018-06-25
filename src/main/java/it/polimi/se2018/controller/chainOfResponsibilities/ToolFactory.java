package it.polimi.se2018.controller.chainOfResponsibilities;

import it.polimi.se2018.utils.enums.Tool;

public class ToolFactory {
    public static Handler createFirstHandler() {
        return new FirstCheck();
    }

    public static Handler createLastHandler() {
        return new EndOfTheChainHandler();
    }

    public static Handler createToolHandler(Tool tool) {
        switch (tool) {
            case DILUENTEPERPASTASALDA:
                return new DiluentePerPastaSaldaHandler();
            case PINZASGROSSATRICE:
                return new PinzaSgrossatriceHandler();
            case RIGAINSUGHERO:
                return new RigaInSugheroHandler();
            case TAGLIERINACIRCOLARE:
                return new TaglierinaCircolareHandler();
            case PENNELLOPERPASTASALDA:
                return new PennelloPerPastaSaldaHandler();
            case MARTELLETTO:
                return new MartellettoHandler();
            case TENAGLIAAROTELLE:
                return new TenagliaARotelleHandler();
            case TAMPONEDIAMANTATO:
                return new TamponeDiamantatoHandler();
            case MOSSASTANDARD:
                return new NormalMoveHandler();
            case ALESATOREPERLAMINADIRAME:
            case PENNELLOPEREGLOMISE:
                return new Tool2_3Handler(tool);
            case TAGLIERINAMANUALE:
            case LATHEKIN:
                return new Tool4_12Handler(tool);
            default:
                throw new IllegalArgumentException();
        }
    }
}
