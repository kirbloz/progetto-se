package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;

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
    @JacksonXmlProperty(localName = "Autore")
    private Fruitore autore;

    public Proposta(){

    }

    public Proposta(String richiesta, String offerta, int oreRichiesta, int oreOfferta, Fruitore autore) {
        this.richiesta = richiesta;
        this.offerta = offerta;
        this.oreRichiesta = oreRichiesta;
        this.oreOfferta = oreOfferta;
        this.autore = autore;

        this.stato = StatiProposta.APERTA;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Richiesta:\t[ ").append(richiesta.split(":")[1]).append(", ").append(oreRichiesta).append(" ore ]\n");
        sb.append("Offerta:\t[ ").append(offerta.split(":")[1]).append(", ").append(oreOfferta).append(" ore ]\n");

        return sb.toString();
    }

    public boolean isCompatibile(Proposta p){
        if(this.richiesta == p.offerta && this.oreRichiesta == p.oreOfferta) {
            return true;
        } else return false;
    }

    //Getters & Setters

    public Fruitore getAutore() {
        return autore;
    }

    public StatiProposta getStato() {
        return stato;
    }

    public void setClosed(){
        this.stato = StatiProposta.CHIUSA;
    }

    public void setOpen(){
        this.stato = StatiProposta.APERTA;
    }

    public void setRetired(){
        this.stato = StatiProposta.RITIRATA;
    }

    public String getComprensorio(){
        return this.autore.getComprensorioDiAppartenenza();
    }

    public String getRichiesta() {
        return richiesta;
    }

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

