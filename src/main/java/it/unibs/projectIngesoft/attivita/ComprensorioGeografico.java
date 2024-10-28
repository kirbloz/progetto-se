package it.unibs.projectIngesoft.attivita;

import java.util.ArrayList;

/**
 * Rappresenta un insieme di Comuni limitrofi, tra cui effettuare scambi di prestazioni.
 */
public class ComprensorioGeografico {


    private String nomeComprensorio;
    private ArrayList<String> listaComuni;

    public ComprensorioGeografico (){
        this.listaComuni = new ArrayList<>();
    }

    /**
     * Costruttore con parametri per la classe.
     *
     * @param nomeComprensorio, nome del comprensorio geografico
     * @param listaComuni, lista dei comuni appartenenti al comprensorio
     */
    public ComprensorioGeografico(String nomeComprensorio, ArrayList<String> listaComuni) {
        this.setNomeComprensorio(nomeComprensorio);
        this.setListaComuni(listaComuni);
    }

    /**
     * Aggiunge un comune alla lista nel Comprensorio.
     * @param comune, da aggiungere
     */
    public void addComune(String comune){
        assert comune != null && !comune.trim().isEmpty() : "Il nome del comune non deve essere null o vuoto";
        this.listaComuni.add(comune);
    }

    /**
     * Rappresenta il comprensorio geografico come stringa.
     * @return la stringa formattata
     */
    @Override
    public String toString(){
        StringBuilder comprensorioStringato = new StringBuilder();
        comprensorioStringato.append(nomeComprensorio).append("\n");
        for (String comune : listaComuni){
            comprensorioStringato.append("[").append(listaComuni.indexOf(comune) + 1).append("] ").append(comune).append("\n");
        }
        return comprensorioStringato.toString();
    }

    public void setNomeComprensorio(String nomeComprensorio) {
        assert nomeComprensorio != null && !nomeComprensorio.trim().isEmpty() : "Il nome del comprensorio non deve essere null o vuoto";
        this.nomeComprensorio = nomeComprensorio;
    }

    public String getNomeComprensorio() {
        return nomeComprensorio;
    }

    // Wade Bullshit: keep reading at your own risk

    public ArrayList<String> getListaComuni() {
        return listaComuni;
    }

    public void setListaComuni(ArrayList<String> listaComuni) {
        assert listaComuni != null : "La lista dei comuni non deve essere null";
        this.listaComuni = listaComuni;
    }
}
