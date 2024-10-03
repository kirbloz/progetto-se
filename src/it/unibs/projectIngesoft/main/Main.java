package main;

import attivita.*;
import it.unibs.fp.myutils.MyMenu;

/* Classe main.Main.

 */
public class Main {
    


    private static final String TITOLO_MENU_CONFIGURAZIONE = "OPZIONI DI CONFIGURAZIONE";
    private static final String[] OPZIONI_CONFIGURAZIONE = {"Cambio Password", "Crea Categoria", "Crea Comprensorio Geografico"};

    public static void main(String[] args) {
        // L'app si avvia con il login
        GestoreUtenti.login();
        // Mettiamo qualcosa da fare se il login va male? (un try catch e un throw in "login"?)

        //dopo il login come amministratore deve essere possibile scegliere che caso d'uso effettuare
        // (Classe per la gestione di questa scelta? o si mette tutto in configuratore?)
        MyMenu opzioniConfigurazioneMenu = new MyMenu(TITOLO_MENU_CONFIGURAZIONE, OPZIONI_CONFIGURAZIONE);
        opzioniConfigurazioneMenu.stampaMenu();
        //DaFare: non mi ricordo come fare il case con MyMenu
    }
}


