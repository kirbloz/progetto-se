package attivita;

import it.unibs.fp.myutils.*;
import utente.*; //non so se è una roba che serve -m
import java.io.File;
import java.util.ArrayList;

public class GestoreUtenti {
    public static final String defaultAdminUsr = "admin";
    public static final String defaultAdminPsw = "1234";
    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    public ArrayList<Utente> listaUtenti; //la lista degli utenti registrati (da salvare in fileCredenziali)
    public File fileCredenziali; //file contenente gli utenti registrati
    
    public static void login(){
        String username, password;

        //da ciclare i guess -m
        username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

        if(username == defaultAdminUsr && password == defaultAdminPsw){
            Configuratore C1 = new Configuratore(); //qui creiamo l'oggetto nuovo configuratore con nome C1, ma il nome deve cambiare per ogni istanza eventuale (o sono scemo?) (sono scemo -m)
            C1.cambioCredenziali();
        }
        else if(true/*nome inserito esiste && password corretta */){
            //ok
        }
        else System.out.println("errore");//dai errore
    }

    public void letturaCredenziali(){
        //per leggere la roba dell'array caricato o check l'esistenza dell'utente o forse no, l'ha fatto gabbay -m
    }

    public void caricamentoFileCredenziali(){
        //todo servirà per caricare (o creare il file contenente i vari utenti registrati)
    }


}
