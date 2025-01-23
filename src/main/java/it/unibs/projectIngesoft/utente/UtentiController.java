package it.unibs.projectIngesoft.utente;

import it.unibs.projectIngesoft.gestori.UtentiModel;
import it.unibs.projectIngesoft.view.AccessoView;

public class UtentiController {

    AccessoView accessoView;
    UtentiModel utentiModel;

    //IIOList<Utente> listHandler;

    public UtentiController(AccessoView accessoView, UtentiModel utentiModel/*, IIOList<Utente> listHandler*/) {
        this.accessoView = accessoView;
        this.utentiModel = utentiModel;
        //this.listHandler = listHandler;
    }


/*
        mostra il menu di accesso -> AccessoView::stampaMenu()
        fa accedere -> attende input da view (con observer) string input = AccessoView::attendiInput()
            -> 1 o 2, sceglie login o registrazione
            -> manda al model::controlloLogin
            -> riconosce il tipo di utente all’accesso
     */
    public Utente effettuaAccesso(){
        // assert che sia stato letto utenti
               return gestioneMenuIniziale();

    }

    private Utente gestioneMenuIniziale(){
        Utente utenteAttivo = null;
        int option = accessoView.menuIniziale();
        switch(option){
            case 1:
                utenteAttivo = login();
                break;
            case 2:
                utenteAttivo = register();
                break;
            default:

                break;
        }
        return utenteAttivo;
    }

    private Utente login(){ //todo controllare il funzionamento del return con le eccezioni
        boolean riuscito = true;
        do{
            String[] credenziali = accessoView.richiestaCredenziali();
            try{
                //NOTA: questo ritorna un Configuratore con firstAccess true se si usano le credenziali di default
                return utentiModel.verificaCredenziali(credenziali);
            }catch (Exception e){
                riuscito = false;
                accessoView.stampaErroreCredenziali("Login fallito");
            }
        }while(!riuscito);
        return null;
    }

    private Utente register(){
        return null;
    }
}
