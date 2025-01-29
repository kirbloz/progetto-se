package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.controller.UtentiController;
import it.unibs.projectIngesoft.view.AccessoView;
import it.unibs.projectIngesoft.view.UtenteViewableTerminal;

public class ErmesController /*implements EventListener*/ {

    Utente utenteAttivo;
    UtentiController utentiController;

    public ErmesController() {

    }

    public void mainLoop(){
        UtenteViewableTerminal view;


        //Dove svilupperemo la logica principale del programma, visto che il main lo lasceremo pressoché vuoto
        //do {
            UtentiMapper utentiMapper = new UtentiMapper("users.json",
                    "defeaultCredentials.json",
                                        new SerializerJSON<>(),
                                        new SerializerJSON<>());

            UtentiModel modelUtenti = new UtentiModel(utentiMapper);
            AccessoController controllerAccesso = new AccessoController(modelUtenti);
            AccessoView viewAccesso = new AccessoView(controllerAccesso);



            //controllerAccesso.events.subscribe("utenteOttenuto", this);
            //viewAccesso.menuIniziale();

            /*this.utenteAttivo = utentiController.effettuaAccesso();

            if(this.utenteAttivo instanceof Configuratore){
                view = new ConfiguratoreView();
            }else{
                view = new FruitoreView();
            }*/


        //}while(true);
        /*boolean exit = false;
        while (!exit) {
            view.stampaMenu();
            int choice = view.getUserSelection();
            switch (choice) {
                case 1:
                    //fai qualcosa
                    break;
                case 2:
                    //fai altro
                    break;
                case 3:
                    exit = true;
                    break;
                default:
                    //view.printMessage("Opzione non valida. Riprova.");
            }
        }*/

    }



    @Override
    public void update(String eventType, Object o) {
        switch(eventType){
            case "utenteOttenuto":
                this.utenteAttivo = (Utente)o;
        }
    }
}
