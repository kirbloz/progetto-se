package it.unibs.projectIngesoft.utente;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("")
public class Configuratore extends Utente{

    public Configuratore() {
        this.username = "admin";
        this.password = "1234";
    }

    public Configuratore(String username, String password) {       // Caso utente //??? a cosa serve il costruttore con username e password? -m
        this.username = username;
        this.password = password;
    }
    
}
