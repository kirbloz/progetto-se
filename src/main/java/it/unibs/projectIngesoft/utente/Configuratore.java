package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Configuratore extends Utente{

    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private String type = "Configuratore";

    public Configuratore() {
        super();
    }

    public Configuratore(String username, String password) {       // Caso utente //??? a cosa serve il costruttore con username e password? -m
        super(username, password);
    }

}
