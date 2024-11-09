package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import it.unibs.projectIngesoft.utente.Fruitore;

import java.util.Stack;

@JacksonXmlRootElement(localName = "Proposta")
public class Proposta {

    @JacksonXmlProperty(localName = "richiesta")
    private String richiesta;
    @JacksonXmlProperty(localName = "offerta")
    private String offerta;
    @JacksonXmlProperty(localName = "oreRichiesta")
    private int oreRichiesta;
    @JacksonXmlProperty(localName = "oreOfferta")
    private int oreOfferta;
    @JacksonXmlProperty(localName = "stato")
    private StatiProposta stato;
    @JacksonXmlProperty(localName = "autore")
    private String autore;
    @JacksonXmlProperty(localName = "comprensorioDiAppartenenza")
    private String comprensorioDiAppartenenza;

    private Stack<StatiProposta> cronologiaStati = new Stack<>();    //TODO Wade salva sta roba con xml :D

    public Proposta(){

    }

    public Proposta(String richiesta, String offerta, int oreRichiesta, int oreOfferta, Fruitore autore) {
        this.richiesta = richiesta;
        this.offerta = offerta;
        this.oreRichiesta = oreRichiesta;
        this.oreOfferta = oreOfferta;
        this.autore = autore.getUsername();
        this.comprensorioDiAppartenenza = autore.getComprensorioDiAppartenenza();

        this.stato = StatiProposta.APERTA;
        cronologiaStati.push(this.stato);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Richiesta:\t[ ").append(richiesta.split(":")[1]).append(", ").append(oreRichiesta).append(" ore ]\n");
        sb.append("Offerta:\t[ ").append(offerta.split(":")[1]).append(", ").append(oreOfferta).append(" ore ]\n");
        return sb.toString();
    }

    public boolean isCompatibile(Proposta p){
        return this.richiesta == p.offerta && this.oreRichiesta == p.oreOfferta;
    }

    //Getters & Setters

    @JsonIgnore
    public String getAutore() {
        return autore;
    }
    @JsonIgnore
    public StatiProposta getStato() {
        return stato;
    }

    public void setChiusa(){
        this.stato = StatiProposta.CHIUSA;
        cronologiaStati.push(this.stato);
    }

    public void setAperta(){
        this.stato = StatiProposta.APERTA;
        cronologiaStati.push(this.stato);
    }

    public void setRitirata(){
        this.stato = StatiProposta.RITIRATA;
        cronologiaStati.push(this.stato);
    }

    @JsonIgnore
    public String getComprensorio(){
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

    public int getOreRichiesta() {
        return oreRichiesta;
    }

    public int getOreOfferta() {
        return oreOfferta;
    }
}

