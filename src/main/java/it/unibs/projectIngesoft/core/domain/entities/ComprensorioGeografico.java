package it.unibs.projectIngesoft.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un insieme di Comuni limitrofi, tra cui effettuare scambi di prestazioni.
 */
public class ComprensorioGeografico {

    @JsonProperty
    private String nomeComprensorio;
    @JsonProperty
    private List<String> listaComuni;

    /**
     * Costruttore di default necessario per la deserializzazione di Jackson
     */
    public ComprensorioGeografico() {
        this.nomeComprensorio = ""; this.listaComuni = new ArrayList<>();
    }

    /**
     * Costruttore con parametri per la classe.
     *
     * @param nomeComprensorio, nome del comprensorio geografico
     * @param listaComuni,      lista dei comuni appartenenti al comprensorio
     */
    public ComprensorioGeografico(String nomeComprensorio, List<String> listaComuni) {
        this.setNomeComprensorio(nomeComprensorio);
        this.setListaComuni(listaComuni);
    }

    public void setNomeComprensorio(String nomeComprensorio) {
        assert nomeComprensorio != null
                && !nomeComprensorio.trim().isEmpty() : "Il nome del comprensorio non deve essere null o vuoto";
        this.nomeComprensorio = nomeComprensorio;
    }

    public String getNomeComprensorio() {
        return nomeComprensorio;
    }

    public List<String> getListaComuni() {
        return listaComuni;
    }

    public void setListaComuni(List<String> listaComuni) {
        assert listaComuni != null : "La lista dei comuni non deve essere null";
        this.listaComuni = listaComuni;
    }
}
