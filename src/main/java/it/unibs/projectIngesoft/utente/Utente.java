package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.fp.myutils.InputDati;

@JsonRootName("")
public class Utente {

    @JacksonXmlProperty (localName = "username")
    protected String username;
    @JacksonXmlProperty(localName = "password")
    protected String password;

    public static final String MSG_RICHIESTA_NUSERNAME = "Inserisci il nuovo username: "; //per cambio username
    public static final String MSG_RICHIESTA_NPASSWORD = "Inserisci la nuova password: "; //per cambio password


    public Utente(){
        // Usato solo nel caso del primo accesso del configuratore, per generare un utente nuovo da registrare (scelta di progettazione)
    }

    public Utente(String username, String password) { //per la creazione (futura) dei fruitori
        this.username = username;
        this.password = password;
    }

    public void cambioCredenziali() { // per cambiare le credenziali di un utente (funziona in tutti i casi, ma se in una versione futura il fruitore può cambiare anche mail bisognerà fare un distinguo polimorfico)
        String newUsername;
        String newPassword;
        newUsername = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NUSERNAME);
        newPassword = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NPASSWORD);
        this.username = newUsername;
        this.password = newPassword;
        // TODO SALVARE NEL FILE
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ").append(this.username).append("\n");
        sb.append("Password: ").append(this.password).append("\n");
        return sb.toString();
    }
    
}
