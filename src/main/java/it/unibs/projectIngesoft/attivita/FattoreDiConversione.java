package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * FattoreDiConversione rappresenta un fattore di conversione tra due categorie.
 * Viene utilizzata per calcolare il fattore di conversione delle ore da una categoria ad un'altra.
 */
@JacksonXmlRootElement(localName = "FattoreDiConversione")
public class FattoreDiConversione {

    @JsonProperty("nome_c1")
    private String nome_c1;
    @JsonProperty("nome_c2")
    private String nome_c2;
    @JsonProperty("fattore")
    private double fattore;

    // costruttore default per Jackson
    public FattoreDiConversione() {

    }

    /**
     * Costruttore per un FattoreDiConversione.
     *
     * @param nome_c1, nome della prima categoria
     * @param nome_c2, nome della seconda categoria
     * @param fattore, valore del fattore di conversione, dalle ore della prima categoria alle ore della seconda
     */
    public FattoreDiConversione(String nome_c1, String nome_c2, double fattore) {
        this.setNome_c1(nome_c1);
        this.setNome_c2(nome_c2);
        this.setFattore(fattore);
    }

    public String getNome_c1() {
        return nome_c1;
    }

    public void setNome_c1(String nome_c1) {
        assert nome_c1 != null
                && !nome_c1.trim().isEmpty() : "Il nome della prima categoria non deve essere null o vuoto";
        this.nome_c1 = nome_c1;
    }

    public String getNome_c2() {
        return nome_c2;
    }

    public void setNome_c2(String nome_c2) {
        assert nome_c2 != null
                && !nome_c2.trim().isEmpty() : "Il nome della seconda categoria non deve essere null o vuoto";
        this.nome_c2 = nome_c2;
    }

    public double getFattore() {
        return fattore;
    }

    public void setFattore(double fattore) {
        assert fattore >= 0.5
                && fattore <= 2 : "Il fattore di conversione deve stare tra 0.5 e 2";
        this.fattore = fattore;
    }

    /**
     * UTILIZZATO DAL GESTORE DI FATTORI.
     * FARE ATTENZIONE SE SI MODIFICA
     * TODO
     * PREDISPORRE UN ALTRO "TOSTRING" PER IL GESTORE MAGARI FATTO AD HOC
     *
     * @return
     */
    public String toString() {
        return nome_c1 + " " + nome_c2 + " " + fattore;
    }
}
