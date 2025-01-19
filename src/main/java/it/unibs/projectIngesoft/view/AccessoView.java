package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.libraries.InputDati;

// classe generica per la view del login a priori
public class AccessoView{

    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";
    public static final String TITLE_STARTING_MENU = "BENVENUTO";
    public static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };


    public int menuIniziale() {
        return 0;
    }

    public String[] richiestaCredenziali() {
        String username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        String password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
        return new String[]{username,password};
    }

    public void stampaErroreCredenziali() {
        System.out.println("va una sega");
    }
}
