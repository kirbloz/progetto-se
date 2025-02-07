package it.unibs.projectIngesoft.presentation.controllers;

import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;
import it.unibs.projectIngesoft.core.domain.model.ComprensorioGeograficoModel;
import it.unibs.projectIngesoft.core.domain.model.UtentiModel;
import it.unibs.projectIngesoft.presentation.view.AccessoView;

import static it.unibs.projectIngesoft.libraries.Utilitas.isValidEmail;

public class AccessoController {


    private final UtentiModel utentiModel;
    private final ComprensorioGeograficoModel comprensorioModel;
    private final AccessoView view;

    public AccessoController(UtentiModel utentiModel, ComprensorioGeograficoModel comprensorioModel) {
        this.utentiModel = utentiModel;
        this.comprensorioModel = comprensorioModel;
        this.view = new AccessoView();
    }

    public Utente login() {
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
        do {
            credenziali = view.richiestaCredenziali();
            if (!utentiModel.existsUsername(credenziali[0])) {
                riuscito = true;
            } else {
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
            int scelta = view.visualizzaMenuPrincipale();
            switch (scelta) {
                case 1 -> utenteAttivo = this.login();
                case 2 -> utenteAttivo = this.register();
            }
        } while (utenteAttivo == null);
        return utenteAttivo;
    }

}
