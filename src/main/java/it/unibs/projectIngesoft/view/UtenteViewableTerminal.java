package it.unibs.projectIngesoft.view;


// INTERFACCIA PER LE DUE VIEW CONFIGURATORE E FRUITORE, MA ANCHE PER LA GENERICA
public interface UtenteViewableTerminal {

     void stampaMenu();
     int getUserSelection();
     String getUserInput();

}
