package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonRootName("")
public class Utente {

    @JacksonXmlProperty(localName = "username")
    protected String username;
    @JacksonXmlProperty(localName = "password")
    protected String password;


    public Utente() {
        // Usato solo nel caso del primo accesso del configuratore, per generare un utente nuovo da registrare (scelta di progettazione)
    }

    public Utente(String username, String password) { //per la creazione (futura) dei fruitori
        this.setUsername(username);
        this.setPassword(password);
    }

    public void cambioCredenziali(String newUsername, String newPassword) { // per cambiare le credenziali di un utente (funziona in tutti i casi, ma se in una versione futura il fruitore può cambiare anche mail bisognerà fare un distinguo polimorfico)
        this.setUsername(newUsername);
        this.setPassword(newPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        assert username != null
                && !username.trim().isEmpty() : "lo username non deve essere null o vuoto";
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        assert password != null
                && !password.trim().isEmpty() : "la password non deve essere null o vuoto";
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Username: ").append(this.username).append("\n");
        sb.append("Password: ").append(this.password).append("\n");
        return sb.toString();
    }

}
