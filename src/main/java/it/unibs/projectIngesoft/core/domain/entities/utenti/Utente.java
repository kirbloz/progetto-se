package it.unibs.projectIngesoft.core.domain.entities.utenti;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Fruitore.class, name = "Fruitore"),
        @JsonSubTypes.Type(value = Configuratore.class, name = "Configuratore")
})
@JsonRootName("")
public class Utente {

    @JsonProperty
    protected String username;
    @JsonProperty("password")
    protected String password;

    public Utente() {
        // Usato solo nel caso del primo accesso del configuratore, per generare un utente nuovo da registrare (scelta di progettazione)
    }

    public Utente(String username, String password) { //per la creazione (futura) dei fruitori
        this.setUsername(username);
        this.setPassword(password);
    }

    public void cambioCredenziali(String newUsername, String newPassword) {
        // per cambiare le credenziali di un utente (funziona in tutti i casi, ma se in una versione futura il fruitore può cambiare anche mail bisognerà fare un distinguo polimorfico)
        this.setUsername(newUsername);
        this.setPassword(newPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        assert username != null : "lo username non può essere null";
                //&& !username.trim().isEmpty() : "lo username non deve essere null o vuoto";
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        assert password != null : "la password non può essere null";
               // && !password.trim().isEmpty() : "la password non deve essere null o vuoto";
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
