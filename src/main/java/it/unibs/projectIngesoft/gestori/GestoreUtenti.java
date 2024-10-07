package gestori;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.fp.myutils.InputDati;
import utente.Configuratore;
import utente.Utente;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreUtenti {

    public static final String defaultAdminUsr = "admin";
    public static final String defaultAdminPsw = "1234";
    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    private final String filePath;
    private ArrayList<Utente> utenti;

    //public static File fileCredenziali = new File("fileCredenziali"); // file contenente gli utenti registrati

    public GestoreUtenti(String filePath) {
        this.filePath = filePath;
        this.utenti = new ArrayList<>();
        serializeXML();
        deserializeXML(); // load dati
    }

    public String getFilePath() {
        return filePath;
    }

    public ArrayList<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(ArrayList<Utente> utenti) {
        this.utenti = utenti;
    }


    public void addUtente(Utente utente) {
        if (!this.utenti.contains(utente)) {
            // inserisce l'utente se e solo se non esiste già nell'arraylist
            this.utenti.add(utente);
        }
    }

    /**
     * Serve per il login.
     */
    public void login() {
        String username;
        String password;

        // da ciclare i guess -m
        username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

        if (username.equals(defaultAdminUsr) && password.equals(defaultAdminPsw)) {
            Configuratore C1 = new Configuratore(); // qui creiamo l'oggetto nuovo configuratore con nome C1, ma il nome
            // deve cambiare per ogni istanza eventuale (o sono scemo?) (sono
            // scemo -m)
            C1.cambioCredenziali();
            this.scritturaCredenzialiSuFile();
        } else if (true/* nome inserito esiste && password corretta */) {
            // ok

        } else
            System.out.println("errore");// dai errore
    }

    public void checkUtente() { // Ricerca all'interno dell'ArrayList (dopo averlo popolato leggendo il
        // fileCredenziali) l'esistenza della coppia username e password data in input

    }

    public void scritturaCredenzialiSuFile() { // Crea il fileCredenziali a partire dall'ArrayList listaUtenti

    }

    public void letturaFileCredenziali() { // Inserisce gli utenti e le password (che si trovano dentro al file)
        // nell'ArrayList listaUtenti

    }

    /**
     * Per ogni chiave nell'HashMap fattori, si estrae il suo ArrayList e lo si serializza in XML.
     */
    public void serializeXML() {

        try {

            boolean debug = false;

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(this.filePath);
            // se il file non esiste, lo si crea
            if (file.createNewFile()) {
                if (debug)
                    System.out.println("FILE CREATO");
            }

            this.utenti.add(new Utente("test", "testpwd"));


            xmlMapper.writeValue(file, this.utenti);


        } catch (JsonProcessingException e) {
            // handle exception
            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    public void deserializeXML() {
        boolean debug = true;

        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(this.filePath);

            if (!file.exists()) {
                if (debug)
                    System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return;
            }

            List<Utente> listaUtenti = xmlMapper.readValue(file, new TypeReference<>() {
            });

            for (Utente utente : listaUtenti) {
                this.addUtente(utente);
            }

            if (debug)
                for (Utente obj : listaUtenti) {
                    System.out.println(obj.toString());
                }
        } catch (IOException e) {
            // handle the exception
            System.out.println(e.getMessage());
        }
    }


}
