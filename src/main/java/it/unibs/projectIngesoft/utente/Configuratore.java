package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("")
public class Configuratore extends Utente{

    public Configuratore() {
        super();
    }

    public Configuratore(String username, String password) {       // Caso utente //??? a cosa serve il costruttore con username e password? -m
        super(username, password);
    }

}
