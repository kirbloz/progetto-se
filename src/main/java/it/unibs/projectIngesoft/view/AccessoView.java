package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.libraries.EventListener;
import it.unibs.projectIngesoft.libraries.EventManager;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.model.ComprensorioGeograficoModel;

import javax.swing.text.View;
import java.util.Arrays;

// classe generica per la view del login a priori
public class AccessoView /*implements EventListener*/ {

    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";
    public static final String MSG_RICHIESTA_EMAIL = "Inserisci la tua email: ";
    public static final String MSG_RICHIESTA_COMPRENSORIO = "Inserisci il comprensorio di appartenenza: ";

    public static final String MSG_USCITA_SUBMENU = ">> Uscita dal submenu.. <<";

    public static final String MSG_PROGRAM_EXIT = "> ARRIVEDERCI <";
    public static final String TITLE_STARTING_MENU = "BENVENUTO";
    public static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };
    /*
    //Eventi
    private static final String[] eventi = {"primoAccesso", "credenzialiInserite"};

    public EventManager events;
    */
    //private AccessoController controllerAccesso;

    public AccessoView(/*AccessoController controllerAccesso*/) {
        //this.controllerAccesso = controllerAccesso;
        /*
        events = new EventManager(eventi);
        events.subscribe(PRIMO_ACCESSO, controllerAccesso);
        events.subscribe("credenzialiInserite", controllerAccesso);
        controllerAccesso.events.subscribe("richiestaLogin", this);
         */
    }
    ////////////// CORPO //////////////
    /*public int menuIniziale() {
        Menu menuIniziale = new Menu(TITLE_STARTING_MENU,vociMenuIniziale);
        return menuIniziale.scegli();

        events.notify(PRIMO_ACCESSO, scelta);

    }*/

    public String[] richiestaCredenziali() {
        String username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        String password = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
        return new String[]{username,password};
    }

    public String[] richiestaCredenzialiRegistrazione(){
        String comprensorio = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_COMPRENSORIO);
        String email = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
        String[] credenziali = richiestaCredenziali();
        return new String[]{comprensorio, email, credenziali[0], credenziali[1]};
    }


    public void stampaErroreCredenziali(String msg) {
        System.out.println("Errore nella stampa" + msg);
    }

    public int visualizzaMenuPrincipale() {
        Menu menuPrincipale = new Menu(TITLE_STARTING_MENU,vociMenuIniziale);
        return menuPrincipale.scegli();
    }

    /*
    @Override
    public void update(String eventType, Object o) {
        switch (eventType){
            case "richiestaLogin":
                events.notify("credenzialiInserite", richiestaCredenziali());
                break;
        }
    }
    */
}
