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
public abstract class Utente {

    @JsonProperty
    protected String username;
    @JsonProperty
    protected String password;

    protected Utente() {
    }

    protected Utente(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public void cambioCredenziali(String newUsername, String newPassword) {
        this.setUsername(newUsername);
        this.setPassword(newPassword);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        assert username != null : "lo username non può essere null";
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        assert password != null : "la password non può essere null";
        this.password = password;
    }

    public String getType(){
        return "Utente";
    }

}
