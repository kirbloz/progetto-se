package main;

import attivita.*;

/* Classe main.Main.

 */
public class Main {
    
    public static void main(String[] args) {
        // L'app si avvia con il login
        GestoreUtenti.login();
        // Mettiamo qualcosa da fare se il login va male? (un try catch e un throw in "login"?)

        //dopo il login come amministratore deve essere possibile scegliere che caso d'uso effettuare
        // (Classe per la gestione di questa scelta? o si mette tutto in configuratore?)

    }
}


