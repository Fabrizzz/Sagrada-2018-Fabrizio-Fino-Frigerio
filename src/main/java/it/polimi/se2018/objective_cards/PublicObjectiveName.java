package it.polimi.se2018.objective_cards;

public enum PublicObjectiveName {
    COLORIDIVERSIRIGA("Colori diversi - riga"),
    COLORIDIVERSICOLONNA("Colori diversi - colonna"),
    SFUMATUREDIVERSERIGA("Sfumature diverse - riga"),
    SFUMATUREDIVERSECOLONNA("Sfumature diverse - colonna"),
    SFUMATURECHIARE("Sfumature chiare"),
    SFUMATUREMEDIE("Sfumature medie"),
    SFUMATURESCURE("Sfumature scure"),
    SFUMATUREDIVERSE("Sfumature diverse"),
    DIAGONALICOLORATE("Diagonali colorate"),
    VARIETADICOLORE("Varieta' di colore");

    private String toolName;

    PublicObjectiveName(String str) {
        toolName = str;
    }

    @Override
    public String toString() {
        return toolName;
    }
}
