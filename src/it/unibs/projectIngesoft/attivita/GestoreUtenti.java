package attivita;

import it.unibs.fp.myutils.*;


import utente.*;

import java.io.File;
import java.util.ArrayList;

public class GestoreUtenti {
    public static final String defaultAdminUsr = "admin";
    public static final String defaultAdminPsw = "1234";
    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    public ArrayList<Utente> listaUtenti;
    public File fileCredenziali;
    
    public static void login(){
        String username, password;
        username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

        if(username == defaultAdminUsr && password == defaultAdminPsw){
            Configuratore C1 = new Configuratore();
            C1.cambioCredenziali();
        }
    }

    public void letturaCredenziali(){

    }

    public void caricamentoFileCredenziali(){
    // TODO
    }


}
