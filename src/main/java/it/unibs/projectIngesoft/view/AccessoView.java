package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.libraries.EventListener;
import it.unibs.projectIngesoft.libraries.EventManager;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;

import javax.swing.text.View;

// classe generica per la view del login a priori
public class AccessoView implements EventListener {

    public static final String PRIMO_ACCESSO = "primoAccesso";
    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";
    public static final String TITLE_STARTING_MENU = "BENVENUTO";
    public static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };

    //Eventi
    private static final String[] eventi = {PRIMO_ACCESSO, "credenzialiInserite"};

    public EventManager events;
    private AccessoController controllerAccesso;

    public AccessoView(AccessoController controllerAccesso) {
        this.controllerAccesso = controllerAccesso;
        events = new EventManager(eventi);
        events.subscribe(PRIMO_ACCESSO, controllerAccesso);
        events.subscribe("credenzialiInserite", controllerAccesso);
        controllerAccesso.events.subscribe("richiestaLogin", this);
    }

    ////////////// CORPO //////////////
    public void menuIniziale() {
        Menu menuIniziale = new Menu(TITLE_STARTING_MENU,vociMenuIniziale);
        int scelta = menuIniziale.scegli();
        events.notify(PRIMO_ACCESSO, scelta);
    }

    public String[] richiestaCredenziali() {
        String username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        String password = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
        return new String[]{username,password};
    }

    public void stampaErroreCredenziali(String msg) {
        System.out.println("Errore nella stampa" + msg);
    }

    @Override
    public void update(String eventType, Object o) {
        switch (eventType){
            case "richiestaLogin":
                events.notify("credenzialiInserite", richiestaCredenziali());
                break;
        }
    }
}
