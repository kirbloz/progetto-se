package it.unibs.projectIngesoft.attivita;

import java.util.ArrayList;

public class ComprensorioGeografico {
    ArrayList<String> ListaComuni;

    public ComprensorioGeografico(ArrayList<String> listaComuni) {
        ListaComuni = listaComuni;
    }

    public ComprensorioGeografico() {
        ListaComuni = new ArrayList<>();
    }

    public ArrayList<String> getListaComuni() {
        return ListaComuni;
    }

    public boolean addComune(String comune){
        return this.ListaComuni.add(comune);
    }

    public boolean removeComune(String comune){
        return this.ListaComuni.remove(comune);
    }

    public void setListaComuni(ArrayList<String> listaComuni) {
        ListaComuni = listaComuni;
    }
}
