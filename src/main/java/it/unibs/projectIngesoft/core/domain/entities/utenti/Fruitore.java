package it.unibs.projectIngesoft.core.domain.entities.utenti;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Fruitore extends Utente{

    @JsonProperty
    private String email;
    @JsonProperty
    private String comprensorioDiAppartenenza;
    @JsonProperty("type")
    private String type = "Fruitore";

    public Fruitore () {
        super();
    }

    public Fruitore(String username, String password, String email, String comprensorioDiAppartenenza) {
        super(username, password);
        this.email = email;
        this.comprensorioDiAppartenenza = comprensorioDiAppartenenza;
    }

    public String getComprensorioDiAppartenenza() {
        return comprensorioDiAppartenenza;
    }

    public String getEmail(){
        return email;
    }

    @Override
    public String getType() {
        return type;
    }
}
