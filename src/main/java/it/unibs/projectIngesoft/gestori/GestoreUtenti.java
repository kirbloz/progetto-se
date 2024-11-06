package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.libraries.InputDati;
import it.unibs.projectIngesoft.libraries.Serializer;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GestoreUtenti {

    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";
    public static final String MSG_RICHIESTA_EMAIL = "Inserisci la tua email: ";
    public static final String MSG_RICHIESTA_COMPRENSORIO = "Inserisci il comprensorio di appartenenza: ";

    public static final String MSG_RICHIESTA_NEW_USERNAME = "Inserisci il nuovo username: "; //per cambio username
    public static final String MSG_RICHIESTA_NEW_PASSWORD = "Inserisci la nuova password: "; //per cambio password

    public static final String MSG_UTENTE_ESISTENTE = "L'username e' gia' in uso";
    public static final String MSG_COMPRENSORIO_NEXIST = "Il comprensorio non esiste!";

    private final String filePath;
    private final String defaultCredentialsFilePath;
    private ArrayList<Utente> utenti;
    private Utente defaultUtente;

    public GestoreUtenti(String filePath, String defaultCredentialsFilePath) {
        this.filePath = filePath;
        this.defaultCredentialsFilePath = defaultCredentialsFilePath;
        this.utenti = new ArrayList<>();
        deserializeXML(); // load dati
        serializeXML();
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
        assert this.defaultCredentialsFilePath != null;

        List<Utente> listaUtenti = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        this.utenti.clear();
        if (listaUtenti != null)
            this.utenti.addAll(listaUtenti);

        this.defaultUtente = Serializer.deserialize(new TypeReference<>() {
        }, this.defaultCredentialsFilePath);

        //this.utenti.add(new Configuratore("ciao", "1234"));
        //this.utenti.add(new Fruitore("0","0", "fake@mail", "fake-comprensorio"));

        assert this.utenti != null : "deve essere almeno inizializzato!";
        assert this.defaultUtente != null : "l'utente default non può essere null, deve esistere";
    }

    /**
     * Inserisce l'utente nell'attributo utenti se e solo se non esiste già
     *
     * @param utente, utente da inserire
     */
    public void addUtente(Utente utente) {
        if (!this.utenti.contains(utente)) {
            this.utenti.add(utente);
            serializeXML();
        }
    }

    public Utente register(ArrayList<String> possibiliComprensori) {
        String comprensorio;
        String username;
        String password;
        String email;
        Fruitore fruitore;

        for (String tmp : possibiliComprensori) {
            System.out.println(tmp);
        }

        comprensorio = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_COMPRENSORIO);
        while (!possibiliComprensori.contains(comprensorio)) {
            System.out.println(MSG_COMPRENSORIO_NEXIST);
            comprensorio = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_COMPRENSORIO);
        }

        username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        while (!(cercaConUsername(username) == null)) {
            System.out.println(MSG_UTENTE_ESISTENTE);
            username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        }

        password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

        email = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
        while (!isValidEmail(email)) {
            System.out.println(">> (!!) Formato email non valido.");
            email = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
        }

        fruitore = new Fruitore(username, password, email, comprensorio);
        this.addUtente(fruitore);
        return fruitore;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }

    /**
     * Serve per il login.
     */
    public Utente login() {
        String username;
        String password;
        int indice;
        do {
            // immissione credenziali per il login
            username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
            password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
            // controllo se credenziali default e in caso creo il nuovo configuratore
            if (username.equals(defaultUtente.getUsername()) && password.equals(defaultUtente.getPassword())) {
                Configuratore C1 = new Configuratore();
                cambioCredenziali(C1);
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
     * Effettua il controllo sulle nuove credenziali (per mantenere l'unicità dello username)
     * e poi chiama la funzione dell'utente per modificarle.
     *
     * @param C1, utente di cui cambiare le credenziali
     */
    public void cambioCredenziali(Utente C1) {
        String newUsername;
        String newPassword;
        do {
            newUsername = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NEW_USERNAME);
            newPassword = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NEW_PASSWORD);
        } while (cercaConUsername(newUsername) != null);
        C1.cambioCredenziali(newUsername, newPassword);
        serializeXML();
    }

    /**
     * Verifica se lo username esiste già.
     *
     * @param username lo username da verificare
     * @return oggetto utente corrispondente se lo username esiste, null altrimenti
     */
    private Utente cercaConUsername(String username) {
        assert this.utenti != null;
        return this.utenti.stream()
                .filter(usr -> usr.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Ricerca all'interno dell'attributo utenti 'esistenza della coppia username e password data in ingresso
     *
     * @param username, username dell'utente cercato
     * @param password, password dell'utente cercato
     * @return int indice dell'oggetto, altrimenti "-1" se
     */
    public int ricercaUtente(String username, String password) {
        Utente tempUtente = cercaConUsername(username);
        if (tempUtente == null) // se l'utente non esiste, ritorna -1
            return -1;
        // se l'utente esiste allora controllo che la password sia quella corretta
        if (tempUtente.getPassword().equals(password)) {
            return utenti.indexOf(tempUtente);
        }
        // se la password è errata, ritorna -1
        return -1;
    }
}
