package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe creata per facilitare la serializzazione XML dei dati relativi alle categorie gestite da GestoreCategorie.
 */
@JacksonXmlRootElement(localName = "Albero")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("Albero")
public class Albero {

    /*
     * Queste annotazioni permettono di evitare la serializzazione come "<ArrayList><item>.." che poi causa
     * problemi durante la de-serializzazione.
     */
    @JacksonXmlProperty(localName = "radice")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Categoria> radici;

    @JsonIgnore
    //@JacksonXmlProperty(localName = "numFoglie")
    private int numFoglie;

    public Albero() {
        this.radici = new ArrayList<>();
    }

    public boolean contains(String nomeRadice){
        for(Categoria tempRadice: this.radici){
            if(tempRadice.getNome().equals(nomeRadice))
                return true;
        }
        return false;
    }

    public List<Categoria> getRadici() {
        return radici;
    }

    public void setRadici(List<Categoria> radici) {
        this.radici = radici;
    }

    public void aggiungiRadice(Categoria radici){
        this.radici.add(radici);
    }

    public void rimouviRadice(Categoria radici){
        this.radici.remove(radici);
    }

    /*
    public int getNumFoglie() {
        return numFoglie;
    }

    public void incrementNumFoglie() {
        this.numFoglie++;
    }
    */

    public Categoria getRadice (String nomeRadice){
        for(Categoria tempRadice: this.radici){
            if(tempRadice.getNome().equals(nomeRadice))
                return tempRadice;
        }
        return null;
    }

}
