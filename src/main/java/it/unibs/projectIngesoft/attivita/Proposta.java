package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

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
    @JacksonXmlProperty(localName = "isAttiva")
    private boolean isAttiva;
    @JacksonXmlProperty(localName = "usernameAutore")
    private String usernameAutore;

    public Proposta(){

    }

    public Proposta(String richiesta, String offerta, int oreRichiesta, int oreOfferta, String usernameAutore) {
        this.richiesta = richiesta;
        this.offerta = offerta;
        this.oreRichiesta = oreRichiesta;
        this.oreOfferta = oreOfferta;
        this.usernameAutore = usernameAutore;

        this.isAttiva = true;
    }
}

