package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.utente.UtentiController;

public class ErmesController {

    Utente utenteAttivo;
    UtentiController utentiController;

    public ErmesController() {

    }

    public void mainLoop(){
        //Dove svilupperemo la logica principale del programma, visto che il main lo lasceremo pressoché vuoto
        do {
            this.utentiController = new UtentiController();

            this.utenteAttivo = utentiController.effettuaAccesso();


        }while(true);

    }



}
