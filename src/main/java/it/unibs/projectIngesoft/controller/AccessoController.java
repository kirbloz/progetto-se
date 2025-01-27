package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.gestori.UtentiModel;
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
}
