package it.unibs.projectIngesoft.view;

// classe generica per la view del login a priori
public class AccessoView implements UtenteViewableTerminal{

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";
    public static final String TITLE_STARTING_MENU = "BENVENUTO";
    public static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };


    @Override
    public void stampaMenu() {

    }
}
