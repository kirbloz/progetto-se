package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("")
public class Fruitore extends Utente {

    private String email;
    private String comprensorioDiAppartenenza;

    public Fruitore(String username, String password, String email, String comprensorioDiAppartenenza) {
        super();
        this.email = email;
        this.comprensorioDiAppartenenza = comprensorioDiAppartenenza;
    }

}
