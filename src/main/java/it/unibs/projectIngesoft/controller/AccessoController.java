package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.libraries.EventListener;
import it.unibs.projectIngesoft.libraries.EventManager;
import it.unibs.projectIngesoft.utente.Utente;

public class AccessoController implements EventListener {

    public static final String RICHIESTA_LOGIN = "richiestaLogin";
    private static final String UTENTE_OTTENUTO = "utenteOttenuto";

    public EventManager events;
    UtentiModel utentiModel;

    public AccessoController(UtentiModel utentiModel) {
        this.utentiModel = utentiModel;
        events = new EventManager(UTENTE_OTTENUTO, RICHIESTA_LOGIN);
    }

    @Override
    public void update(String eventType, Object o) {
        switch (eventType) {
            case "primoAccesso":
                switch((int)o) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    default:
                        //Send ERROR
                        break;
                }
                break;
            case "credenzialiInserite":
                try{
                    Utente utenteAttivo = utentiModel.verificaCredenziali((String[])o);
                    events.notify(UTENTE_OTTENUTO, utenteAttivo);
                }catch(Exception e){
                    //notify("Error");
                }
        }
    }

    private void login() {
        events.notify(RICHIESTA_LOGIN, null);
    }

    private void register() {

    }

    /// DA qui la roba ha un senso (Forse)
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


}
