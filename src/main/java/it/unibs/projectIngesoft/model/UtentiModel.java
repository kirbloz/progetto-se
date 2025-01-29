package it.unibs.projectIngesoft.model;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UtentiModel {

    // da rimuovere e spostare nella view
    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";
    public static final String MSG_RICHIESTA_EMAIL = "Inserisci la tua email: ";
    public static final String MSG_RICHIESTA_COMPRENSORIO = "Inserisci il comprensorio di appartenenza: ";

    public static final String MSG_RICHIESTA_NEW_USERNAME = "Inserisci il nuovo username: "; //per cambio username
    public static final String MSG_RICHIESTA_NEW_PASSWORD = "Inserisci la nuova password: "; //per cambio password

    public static final String MSG_UTENTE_ESISTENTE = "L'username e' gia' in uso";
    public static final String MSG_COMPRENSORIO_NEXIST = "Il comprensorio non esiste!";
    //


    private static List<Utente> utenti;
    private Utente defaultUtente;
    private UtentiMapper mapper;

    public UtentiModel(UtentiMapper mapper) {
        this.mapper = mapper;
        utenti = mapper.read();
        this.defaultUtente =  mapper.readDefaultUtente();
    }

    private static List<Utente> getListaUtenti() {
        if (utenti == null) {
            utenti = new ArrayList<>();
        }
        return new ArrayList<>(utenti);
    }

    public static Fruitore getInformazioniFruitore(String username) {
        List<Utente> tempUtenti = getListaUtenti();
        return tempUtenti.stream()
                .filter(u -> u instanceof Fruitore && u.getUsername().equals(username))
                .map(u -> (Fruitore) u)
                .findFirst()
                .orElse(null);
    }

    /**
     * Inserisce l'utente nell'attributo utenti se e solo se non esiste già
     *
     * @param utente, utente da inserire
     */
    public void addUtente(Utente utente) {
        assert utenti != null;
        if (!utenti.contains(utente)) {
            utenti.add(utente);
            mapper.write(utenti);
        }
    }

    public Utente register(List<String> possibiliComprensori) {
        String comprensorio;
        String username;
        String password;
        String email;
        Fruitore fruitore;

        for (String tmp : possibiliComprensori) {
            System.out.println(tmp);
        }

        comprensorio = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_COMPRENSORIO);
        while (!possibiliComprensori.contains(comprensorio)) {
            System.out.println(MSG_COMPRENSORIO_NEXIST);
            comprensorio = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_COMPRENSORIO);
        }

        username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        while ((cercaConUsername(username) != null)) {
            System.out.println(MSG_UTENTE_ESISTENTE);
            username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        }

        password = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

        email = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
        while (!isValidEmail(email)) {
            System.out.println(">> (!!) Formato email non valido.");
            email = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
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
    public Utente verificaCredenziali(String[] credenziali) throws Exception{
        String username = credenziali[0];
        String password = credenziali[1];
        int indice;
            // controllo se credenziali default e in caso creo il nuovo configuratore
            if (username.equals(defaultUtente.getUsername()) && password.equals(defaultUtente.getPassword())) {
                Configuratore C1 = new Configuratore();
                C1.setFirstAccess(true);
                return C1;
            }
            // ricerca
            indice = ricercaUtente(username, password);
            //todo queste prossime righe sono terribili
        Exception userNotFound;
        if (indice == -1)
                throw userNotFound = new Exception() ;

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
            newUsername = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_NEW_USERNAME);
            newPassword = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_NEW_PASSWORD);
        } while (!existsUsername(newUsername));
        C1.cambioCredenziali(newUsername, newPassword);
        mapper.write(utenti);
    }

    /**
     * Verifica se lo username esiste già.
     *
     * @param username lo username da verificare
     * @return oggetto utente corrispondente se lo username esiste, null altrimenti
     */
    private Utente cercaConUsername(String username) {
        assert utenti != null;
        return utenti.stream()
                .filter(usr -> usr.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean existsUsername(String username) {
        assert utenti != null;
        return utenti.stream()
                .anyMatch(usr -> usr.getUsername().equals(username));
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
