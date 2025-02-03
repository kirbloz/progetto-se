package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.ComprensorioGeograficoModel;
import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.view.AccessoView;

import static it.unibs.projectIngesoft.model.UtentiModel.isValidEmail;

public class AccessoController {

    private UtentiModel utentiModel;
    private ComprensorioGeograficoModel comprensorioModel;
    private AccessoView view;

    public AccessoController(UtentiModel utentiModel, ComprensorioGeograficoModel comprensorioModel) {
        this.utentiModel = utentiModel;
        this.comprensorioModel = comprensorioModel;
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
                view.stampaErroreCredenziali("Login fallito, credenziali errate.");
            }
        } while (!riuscito);

        return null;
    }

    private Utente register() {
        boolean riuscito = false;
        String[] credenziali;
        do{
            credenziali = view.richiestaCredenziali();
            if(!utentiModel.existsUsername(credenziali[0])) {
                riuscito = true;
            }else{
                view.visualizzaErroreUsernameGiaInUso();
            }
        } while (!riuscito);
        String comprensorio = view.selezionaNomeDaLista(comprensorioModel.getListaNomiComprensoriGeografici());

        boolean mailValid = false;
        String email;
        do {
            email = view.inserisciEmail();
            mailValid = isValidEmail(email);
            if(!mailValid) {
                view.visualizzaErroreMailNonValida();
            }
        }while (!mailValid);

        return utentiModel.aggungiFruitore(credenziali[0], credenziali[1], email, comprensorio);

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
