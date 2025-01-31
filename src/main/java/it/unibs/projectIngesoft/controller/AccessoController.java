package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.view.AccessoView;

public class AccessoController {

    private UtentiModel utentiModel;
    private AccessoView view;

    public AccessoController(UtentiModel utentiModel) {
        this.utentiModel = utentiModel;
        this.view = new AccessoView();
    }



    /// DA qui la roba ha un senso (Forse)
    public Utente login() { //todo controllare il funzionamento del return con le eccezioni
        boolean riuscito = true;
        do {
            String[] credenziali = view.richiestaCredenziali();
            try {
                //NOTA: questo ritorna un Configuratore con firstAccess true se si usano le credenziali di default
                return utentiModel.verificaCredenziali(credenziali);
            } catch (Exception e) {
                riuscito = false;
                view.stampaErroreCredenziali("Login fallito "+ e.getMessage());
            }
        } while (!riuscito);

        return null;
    }

    private Utente register() {
        boolean riuscito = true;
        do{
            String[] credenziali = view.richiestaCredenzialiRegistrazione();
            if(utentiModel.verificaCredenzialiRegistrazione(credenziali) == null) {
                riuscito = false;
                System.out.println("Registrazione fallita");
            }
            else{
                return utentiModel.verificaCredenzialiRegistrazione(credenziali);
            }
        } while (!riuscito);
        return null;
    }

    public Utente run() {
        Utente utenteAttivo = null;

        do {
            //1. print menu principale
            int scelta = view.visualizzaMenuPrincipale();
            //2. sceglie tra login e register
            switch (scelta) {
                case 1 -> utenteAttivo = this.login(); //config & fruitore
                case 2 -> utenteAttivo = this.register(); //fruitore
            }
        } while (utenteAttivo == null);

        //3. gestisce IO con accessoVIew
        return utenteAttivo;

    }


}
