package it.unibs.projectIngesoft.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibs.projectIngesoft.core.domain.model.UtentiModel;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;

import java.util.Stack;

public class Proposta {

    @JsonProperty
    private String richiesta;
    @JsonProperty
    private String offerta;
    @JsonProperty
    private int oreRichiesta;
    @JsonProperty
    private int oreOfferta;
    @JsonProperty
    private StatiProposta stato;
    @JsonProperty
    private String autore;
    @JsonProperty
    private String comprensorioDiAppartenenza;
    @JsonProperty
    private Stack<StatiProposta> cronologiaStati;
    @JsonProperty
    private boolean daNotificare;

    protected Proposta() {

    }

    public Proposta(String richiesta, String offerta, int oreRichiesta, int oreOfferta, Fruitore autore) {
        this.richiesta = richiesta;
        this.offerta = offerta;
        this.oreRichiesta = oreRichiesta;
        this.oreOfferta = oreOfferta;
        this.autore = autore.getUsername();
        this.comprensorioDiAppartenenza = autore.getComprensorioDiAppartenenza();

        this.cronologiaStati = new Stack<>();
        this.stato = StatiProposta.APERTA;
        aggiornaStoricoStati();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Richiesta:\t[ ").append(richiesta).append(", ").append(oreRichiesta).append(" ore ]\n");
        sb.append("Offerta:\t[ ").append(offerta).append(", ").append(oreOfferta).append(" ore ]\n");
        return sb.toString();
    }

    /**
     * La compatibilità vuole che la richiesta della proposta passata sia uguale all'offerta della proposta corrente.
     *
     * @param p, proposta da controllare
     * @return true se l'offerta di this può soddisfare la richiesta di p (param)
     */
    public boolean isOffertaCompatibile(Proposta p) {
        return this.getStato() == StatiProposta.APERTA && p.getStato() == StatiProposta.APERTA && this.getOfferta().equals(p.getRichiesta()) && this.getOreOfferta() == p.getOreRichiesta();
    }

    //Getters & Setters

    @JsonIgnore
    public String getAutoreUsername() {
        return autore;
    }

    /*@JsonIgnore
    public Fruitore getAutore() {
        return UtentiModel.getInformazioniFruitore(this.getAutoreUsername());
    }*/

    @JsonIgnore
    public StatiProposta getStato() {
        return stato;
    }

    @JsonIgnore
    public String getComprensorio() {
        return this.comprensorioDiAppartenenza;
    }

    @JsonIgnore
    public String getRichiesta() {
        return richiesta;
    }

    @JsonIgnore
    public String getOfferta() {
        return offerta;
    }

    @JsonIgnore
    public int getOreOfferta() {
        return oreOfferta;
    }

    @JsonIgnore
    public int getOreRichiesta() {
        return oreRichiesta;
    }

    @JsonIgnore
    public boolean isDaNotificare() {
        return daNotificare;
    }

    public void notificata() {
        this.daNotificare = false;
    }

    public void setChiusa() {
        this.stato = StatiProposta.CHIUSA;
        this.daNotificare = true;
        aggiornaStoricoStati();
    }

    public void setAperta() {
        this.stato = StatiProposta.APERTA;
        this.daNotificare = false;
        aggiornaStoricoStati();
    }

    public void setRitirata() {
        this.stato = StatiProposta.RITIRATA;
        this.daNotificare = false;
        aggiornaStoricoStati();
    }

    public void aggiornaStoricoStati() {
        cronologiaStati.push(this.stato);
    }

}

