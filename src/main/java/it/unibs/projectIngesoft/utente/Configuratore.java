package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Configuratore extends Utente{

    @JsonProperty("type")
    private String type = "Configuratore";

    boolean firstAccess;

    public Configuratore() {
        super("", "");
        //per evitare nullpointerexception durante il primo accesso
        firstAccess = false;
    }

    public Configuratore(String username, String password) {       // Caso utente //??? a cosa serve il costruttore con username e password? -m
        super(username, password);
    }

    public void setFirstAccess(boolean firstAccess) {
        this.firstAccess = firstAccess;
    }

	public boolean isFirstAccess() {
        return firstAccess;
	}
}
