package gestori;

import it.unibs.fp.myutils.*;
import utente.*; //non so se è una roba che serve -m
import java.io.File;

public class GestoreUtenti {
    public static final String defaultAdminUsr = "admin";
    public static final String defaultAdminPsw = "1234";
    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    public static File fileCredenziali = new File("fileCredenziali"); // file contenente gli utenti registrati

    
    /**
     * Serve per il login.
     */
    public static void login() {
        String username, password;

        // da ciclare i guess -m
        username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

        if (username.equals(defaultAdminUsr) && password.equals(defaultAdminPsw)) {
            Configuratore C1 = new Configuratore(); // qui creiamo l'oggetto nuovo configuratore con nome C1, ma il nome
                                                    // deve cambiare per ogni istanza eventuale (o sono scemo?) (sono
                                                    // scemo -m)
            C1.cambioCredenziali();
            scritturaCredenzialiSuFile();
        } else if (true/* nome inserito esiste && password corretta */) {
            // ok

        } else
            System.out.println("errore");// dai errore
    }

    public void checkUtente() { // Ricerca all'interno dell'ArrayList (dopo averlo popolato leggendo il
                                // fileCredenziali) l'esistenza della coppia username e password data in input

    }

    public static void scritturaCredenzialiSuFile() { // Crea il fileCredenziali a partire dall'ArrayList listaUtenti
       
    }

    public static void letturaFileCredenziali() { // Inserisce gli utenti e le password (che si trovano dentro al file)
                                                  // nell'ArrayList listaUtenti
        
    }

}
