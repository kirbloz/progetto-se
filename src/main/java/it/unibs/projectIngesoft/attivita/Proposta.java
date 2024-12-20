package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import it.unibs.projectIngesoft.gestori.GestoreUtenti;
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
    @JacksonXmlProperty(localName = "cronologiaStati")
    private Stack<StatiProposta> cronologiaStati;
    @JacksonXmlProperty(localName = "daNotificare")
    private boolean daNotificare;

    public Proposta() {

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
     * @param p, proposta da controllare
     * @return true se l'offerta di this può soddisfare la richiesta di p (param)
     */
    public boolean isOffertaCompatibile(Proposta p) {
        //return this.richiesta.equals(p.offerta) && this.oreRichiesta == p.oreOfferta;
        //return this.getOfferta().equals(p.getRichiesta());
        return this.getOfferta().equals(p.getRichiesta())
                && this.getOreOfferta() == p.getOreRichiesta();
    }

    //Getters & Setters

    @JsonIgnore
    public String getAutoreUsername() {
        return autore;
    }

    @JsonIgnore
    public Fruitore getAutore() {
        return GestoreUtenti.getInformazioniFruitore(this.getAutoreUsername());
    }

    @JsonIgnore
    public StatiProposta getStato() {
        return stato;
    }

    @JsonIgnore
    public boolean isDaNotificare() {
        return daNotificare;
    }

    public void notificata(){
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

}

