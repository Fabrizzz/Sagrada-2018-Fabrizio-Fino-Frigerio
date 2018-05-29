package it.polimi.se2018.utils.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//aggiungere le description
public enum Tool {
    MOSSASTANDARD("Mossa Standard", ""),
    PINZASGROSSATRICE("Pinza Sgrossatrice", "Dopo aver scelto un dado, aumenta o diminuisci il valore del dado scelto di 1\n" +
            "Non puoi cambiare un 6 in 1 o un 1 in 6"),
    PENNELLOPEREGLOMISE("Pennello Per Eglomise", "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di colore\n" +
            "Devi rispettare tutte le altre restrizioni di piazzamento"),
    ALESATOREPERLAMINADIRAME("Alesatore Per Lamina Di Rame", "Muovi un qualsiasi dado nella tua vetrata ignorando le restrizioni di valore\n" +
            "Devi rispettare tutte le altre restrizioni di piazzamento"),
    LATHEKIN("Lathekin", "Muovi esattamente due dadi, rispettando tutte le restrizioni di piazzamento"),
    TAGLIERINACIRCOLARE("Taglierina Circolare", "Dopo aver scelto un dado, scambia quel dado con un dado sul Tracciato dei Round"),
    PENNELLOPERPASTASALDA("Pennello Per Pasta Salda", "Dopo aver scelto un dado, tira nuovamente quel dado\n" +
            "Se non puoi piazzarlo, riponilo nella Riserva"),
    MARTELLETTO("Martelletto", "Tira nuovamente tutti i dadi della Riserva\n" +
            "Questa carta può essera usata solo durante il tuo secondo turno, prima di scegliere il secondo dado"),
    TENAGLIAAROTELLE("Tenaglia A Rotelle", "Dopo il tuo primo turno scegli immediatamente un altro dado\n" +
            "Salta il tuo secondo turno in questo round"),
    RIGAINSUGHERO("Riga In Sughero", "Dopo aver scelto un dado, piazzalo in una casella che non sia adiacente a un altro dado\n" +
            "Devi rispettare tutte le restrizioni di piazzamento"),
    TAMPONEDIAMANTATO("Tampone Di Amantato", "Dopo aver scelto un dado, giralo sulla faccia opposta\n" +
            "6 diventa 1, 5 diventa 2, 4 diventa 3 ecc."),
    DILUENTEPERPASTASALDA("Diluente Per Pasta Salda", "Dopo aver scelto un dado, riponilo nel Sacchetto, poi pescane uno dal Sacchetto\n" +
            "Scegli il valore del nuovo dado e piazzalo, rispettando tutte le restrizioni di piazzamento"),
    TAGLIERINAMANUALE("Taglierina Manuale", "Muovi fino a due dadi dello stesso colore di un solo dado sul Tracciato dei Round\n" +
            "Devi rispettare tutte le restrizioni di piazzamento"),
    SKIPTURN("Salta il turno", "");

    private String toolName;
    private String description;

    Tool(String str, String description) {
        toolName = str;
        this.description = description;
    }

    public static List<Tool> getRandTools(int n) {
        List<Tool> tools = Arrays.asList(Tool.values());
        Collections.shuffle(tools);
        return tools.subList(0, n);
    }

    @Override
    public String toString() {
        return toolName;
    }

    public String getDescription() {
        return description;
    }

}
