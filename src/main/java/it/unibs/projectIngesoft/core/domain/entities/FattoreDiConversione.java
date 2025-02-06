package it.unibs.projectIngesoft.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * FattoreDiConversione rappresenta un fattore di conversione tra due categorie.
 * Viene utilizzata per calcolare il fattore di conversione delle ore da una categoria a un'altra.
 */
public class FattoreDiConversione {

    @JsonProperty("nome_c1")
    private String nome_c1;
    @JsonProperty("nome_c2")
    private String nome_c2;
    @JsonProperty("fattore")
    private double fattore;

    /**
     * Costruttore di default necessario per la deserializzazione di Jackson
     */
    public FattoreDiConversione() {

    }

    /**
     * Costruttore per un FattoreDiConversione.
     *
     * @param catUno,  nome della prima categoria
     * @param catDue,  nome della seconda categoria
     * @param fattore, valore del fattore di conversione, dalle ore della prima categoria alle ore della seconda
     */
    public FattoreDiConversione(String catUno, String catDue, double fattore) {
        this.setNome_c1(catUno);
        this.setNome_c2(catDue);
        this.setFattore(fattore);
    }

    public String getNome_c1() {
        return nome_c1;
    }

    public void setNome_c1(String catUno) {
        assert catUno != null
                && !catUno.trim().isEmpty() : "Il nome della prima categoria non deve essere null o vuoto";
        this.nome_c1 = catUno;
    }

    public String getNome_c2() {
        return nome_c2;
    }

    public void setNome_c2(String catDue) {
        assert catDue != null
                && !catDue.trim().isEmpty() : "Il nome della seconda categoria non deve essere null o vuoto";
        this.nome_c2 = catDue;
    }

    public double getFattore() {
        return fattore;
    }

    /**
     * Imposta il valore del fattore di conversione controllando per un max di 2 e un min di 0.5
     *
     * @param fattore, valore a cui impostare l'attributo
     */
    public void setFattore(double fattore) {
        assert fattore >= 0.5
                && fattore <= 2 : "Il fattore di conversione deve stare tra 0.5 e 2";
        this.fattore = fattore;
    }


}
