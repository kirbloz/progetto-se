package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.libraries.EventListener;
import it.unibs.projectIngesoft.parsing.UtentiMapper;
import it.unibs.projectIngesoft.gestori.UtentiModel;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.controller.UtentiController;
import it.unibs.projectIngesoft.view.AccessoView;

public class ErmesController implements EventListener {

    Utente utenteAttivo;
    UtentiController utentiController;

    public ErmesController() {

    }

    public void mainLoop(){
        //Dove svilupperemo la logica principale del programma, visto che il main lo lasceremo pressoché vuoto
        do {
            UtentiMapper utentiMapper = new UtentiMapper("users.json", "defeaultCredentials.json");
            UtentiModel utentiModel = new UtentiModel(utentiMapper);
            AccessoController controllerAccesso = new AccessoController(utentiModel);
            AccessoView viewAccesso = new AccessoView(controllerAccesso);

            controllerAccesso.events.subscribe("utenteOttenuto", this);
            viewAccesso.menuIniziale();

            this.utenteAttivo = utentiController.effettuaAccesso();


        }while(true);

    }


    @Override
    public void update(String eventType, Object o) {
        switch(eventType){
            case "utenteOttenuto":
                this.utenteAttivo = (Utente)o;
        }
    }
}
