package it.unibs.projectIngesoft.gestori;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.fp.myutils.InputDati;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Utente;

import java.io.File;
import java.io.IOException;
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
     * @param utente
     */
    public void addUtente(Utente utente) {
        if (!this.utenti.contains(utente)) {//TODO scusa wade ma scrivo sta cosa per ricordarmi di chiedertelo:
            // questo check lavora sull'utente come oggetto, quindi se gli arriva un utente che è un oggetto diverso ma è comunque con lo stesso nome (che non dovrebbe essere possibile però boh) non ritorna sempre true?
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
        int indice = -1;

        do {
            // Richiesta Input per il login
            username = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
            password = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);

            // ricerca
            indice = ricercaUtente(username, password);


            if (username.equals(defaultAdminUsr) && password.equals(defaultAdminPsw)) {
                Configuratore C1 = new Configuratore();
                this.cambioCredenziali(C1);
                addUtente(C1);
            } else if (indice == -1) {
                System.out.println("Credenziali errate.");
            }

        } while (indice == -1);

        return utenti.get(indice);
    }

    /**
     * effettua il controllo sulle nuove credenziali (per mantenere l'unicità dell'username) e poi chiama la funzione dell'utente per modificarle
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
            if (!oldUsername.equals(newUsername)) {
                for (Utente usr : this.utenti) {
                    if (usr.getUsername().equals(newUsername)) {
                        exists = true;
                        System.out.println("Nome utente gia' esistente");
                        break;
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
     * TODO questo va commentato a dovere
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
            xmlMapper.writeValue(file, this.utenti);


        } catch (JsonProcessingException e) {
            // handle exception
            System.out.println(e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * TODO questo va commentato a dovere
     */
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
