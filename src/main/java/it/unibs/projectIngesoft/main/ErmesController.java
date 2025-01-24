package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.parsing.UtentiMapper;
import it.unibs.projectIngesoft.gestori.UtentiModel;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.utente.UtentiController;
import it.unibs.projectIngesoft.view.AccessoView;

public class ErmesController {

    Utente utenteAttivo;
    UtentiController utentiController;

    public ErmesController() {

    }

    public void mainLoop(){
        //Dove svilupperemo la logica principale del programma, visto che il main lo lasceremo pressoché vuoto
        do {
            UtentiMapper utentiMapper = new UtentiMapper("users.json", "defeaultCredentials.json");
            this.utentiController = new UtentiController(new AccessoView(),
                    new UtentiModel(utentiMapper));

            this.utenteAttivo = utentiController.effettuaAccesso();


        }while(true);

    }



}
