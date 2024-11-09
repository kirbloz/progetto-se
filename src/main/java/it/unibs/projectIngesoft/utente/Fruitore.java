package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Fruitore extends Utente {

    @JacksonXmlProperty(localName = "email")
    private String email;
    @JacksonXmlProperty(localName = "comprensorioDiAppartenenza")
    private String comprensorioDiAppartenenza;

    /*@JacksonXmlProperty(isAttribute = true, localName = "type")
    private String type = "Fruitore";*/

    public Fruitore () {
        super();
    }

    public Fruitore(String username, String password, String email, String comprensorioDiAppartenenza) {
        super(username, password);
        this.email = email;
        this.comprensorioDiAppartenenza = comprensorioDiAppartenenza;
    }

    //Getters & Setters
    public String getComprensorioDiAppartenenza() {
        return comprensorioDiAppartenenza;
    }

    public void setComprensorioDiAppartenenza(String comprensorioDiAppartenenza) {
        this.comprensorioDiAppartenenza = comprensorioDiAppartenenza;
    }
}
