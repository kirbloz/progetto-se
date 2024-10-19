package it.unibs.projectIngesoft.attivita;

import java.util.ArrayList;

public class ComprensorioGeografico {

    private String nomeComprensorio;
    private ArrayList<String> ListaComuni;

    public ComprensorioGeografico(String nomeComprensorio, ArrayList<String> listaComuni) {
        this.nomeComprensorio = nomeComprensorio;
        this.ListaComuni = listaComuni;
    }

    public void addComune(String comune){
        this.ListaComuni.add(comune);
    }

    @Override
    public String toString(){
        StringBuilder comprensorioStringato = new StringBuilder();
        comprensorioStringato.append(nomeComprensorio).append("\n\n");
        for (String comune : ListaComuni){
            comprensorioStringato.append(comune).append("\n");
        }
        return comprensorioStringato.toString();
    }

    public String getNomeComprensorio() {
        return nomeComprensorio;
    }

    // Wade Bullshit: keep reading at your own risk

    public ArrayList<String> getListaComuni() {
        return ListaComuni;
    }



    public boolean removeComune(String comune){
        return this.ListaComuni.remove(comune);
    }

    public void setListaComuni(ArrayList<String> listaComuni) {
        ListaComuni = listaComuni;
    }
}
