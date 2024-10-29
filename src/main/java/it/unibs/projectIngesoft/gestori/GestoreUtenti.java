package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.fp.myutils.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;

public class GestoreUtenti {

    // da levare l'hardcoding
    public static final String defaultAdminUsr = "admin";
    public static final String defaultAdminPsw = "1234";

    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";

    public static final String MSG_RICHIESTA_NUSERNAME = "Inserisci il nuovo username: "; //per cambio username
    public static final String MSG_RICHIESTA_NPASSWORD = "Inserisci la nuova password: "; //per cambio password

    private final String filePath;
    private ArrayList<Utente> utenti;

    public GestoreUtenti(String filePath) {
        this.filePath = filePath;
        this.utenti = new ArrayList<>();
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
        serializeXML();
    }

    /**
     * inserisce l'utente nell'arraylist se e solo se non esiste già
     *
     * @param utente
     */
    public void addUtente(Utente utente) {
        if (!this.utenti.contains(utente)) {
            this.utenti.add(utente);
            serializeXML();
        }
    }

    /**
     * Serve per il login.
     */
    public Utente login() {
        String username;
        String password;
        int indice;
        // TODO
        // spostare le credenziali da hardcoding a un file credenzialiDefault.xml

        do {
            // Richiesta Input per il login
            username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
            password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

            // controllo se credenziali default e in caso creo il nuovo configuratore
            if (username.equals(defaultAdminUsr) && password.equals(defaultAdminPsw)) {
                Configuratore C1 = new Configuratore();
                this.cambioCredenziali(C1);
                addUtente(C1);
                return C1;
            }

            // ricerca
            indice = ricercaUtente(username, password);
            if (indice == -1)
                System.out.println("Credenziali errate.");

        } while (indice == -1);

        return utenti.get(indice);
    }

    /**
     * effettua il controllo sulle nuove credenziali (per mantenere l'unicità dell'username) e poi chiama la funzione dell'utente per modificarle
     *
     * @param C1
     */
    public void cambioCredenziali(Utente C1) {
        boolean exists = false;
        String newUsername;
        String newPassword;
        String oldUsername = C1.getUsername();

        do {
            newUsername = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NUSERNAME);
            newPassword = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NPASSWORD);
            if (!(oldUsername == null)) {
                if (!oldUsername.equals(newUsername)) {
                    for (Utente usr : this.utenti) {
                        if (usr.getUsername().equals(newUsername)) {
                            exists = true;
                            System.out.println("Nome utente gia' esistente");
                            break;
                        }
                        exists = false; // Nel caso si entrasse nell'if sopra almeno una volta
                    }
                }
            }
        } while (exists);

        C1.cambioCredenziali(newUsername, newPassword);
        serializeXML();
    }

    /**
     * Ricerca all'interno dell'ArrayList (dopo averlo popolato leggendo il
     * fileCredenziali) l'esistenza della coppia username e password data in input
     *
     * @param username
     * @param password
     * @return int indice dell'oggetto, altrimenti "-1" se
     */
    public int ricercaUtente(String username, String password) {
        for (Utente utente : utenti) {
            if (utente.getUsername().equals(username) && utente.getPassword().equals(password)) {
                return utenti.indexOf(utente);
            }
        }
        return -1;
    }

    /**
     * Sfrutto l'implementazione statica della classe Serializer.
     * Questo metodo esiste da prima di Serializer, per non cambiare ogni call a questo ho semplicemente sostituito il corpo.
     */
    public void serializeXML() {

        assert this.utenti != null;
        assert this.filePath != null;

        Serializer.serialize(this.filePath, this.utenti);

    }

    /**
     * Sfrutto l'implementazione statica della classe Serializer.
     * Questo metodo esiste da prima di Serializer, per non cambiare ogni call a questo ho semplicemente sostituito il corpo.
     */
    public void deserializeXML() {
        assert this.filePath != null;

        List<Utente> listaUtenti = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        /* for debugging */
        for (Utente obj : listaUtenti) {
            System.out.println(obj.toString());
        }

        this.utenti.clear();
        if (listaUtenti != null)
            this.utenti.addAll(listaUtenti);

        assert this.utenti != null : "deve essere almeno inizializzato!";
    }


}
