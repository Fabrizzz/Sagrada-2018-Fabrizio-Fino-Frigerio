package it.polimi.se2018.objective_cards;

public enum PublicObjectiveName {
    COLORIDIVERSIRIGA("Colori diversi - riga", "Righe senza colori ripetuti"),
    COLORIDIVERSICOLONNA("Colori diversi - colonna", "Colonne senza colori ripetuti"),
    SFUMATUREDIVERSERIGA("Sfumature diverse - riga", "Righe senza sfumature ripeture"),
    SFUMATUREDIVERSECOLONNA("Sfumature diverse - colonna", "Colonne senza sfumature ripetute"),
    SFUMATURECHIARE("Sfumature chiare", "Set di 1 e 2 ovunque"),
    SFUMATUREMEDIE("Sfumature medie", "Set di 3 e 4 ovunque"),
    SFUMATURESCURE("Sfumature scure", "Set di 5 e 6 ovunque"),
    SFUMATUREDIVERSE("Sfumature diverse", "Set di dadi di ogni valore ovunque"),
    DIAGONALICOLORATE("Diagonali colorate", "Numero di dadi dello stesso colore diagonalmente adiacenti"),
    VARIETADICOLORE("Varieta' di colore", "Set di dadi di ogni colore ovunque");

    private String toolName;
    private String description;

    PublicObjectiveName(String name, String description) {
        toolName = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return toolName;
    }

    public String getDescription() {
        return description;
    }
}
