package attivita;

import java.io.Serializable;
import java.util.ArrayList;

import utente.Utente;

public class BloccoDiCredenziali implements Serializable{
    
    public static ArrayList<Utente> listaUtenti = new ArrayList<>();//la lista degli utenti registrati (da salvare in fileCredenziali)

    public BloccoDiCredenziali(ArrayList<Utente> listaUtenti){
        this.listaUtenti = listaUtenti;
    }

    public BloccoDiCredenziali(){
        this.listaUtenti = new ArrayList<Utente>();
    }

    public static ArrayList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public static void setListaUtenti(ArrayList<Utente> listaUtenti) {
        BloccoDiCredenziali.listaUtenti = listaUtenti;
    }

    
}
