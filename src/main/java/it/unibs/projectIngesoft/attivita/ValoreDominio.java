package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 *  ValoreDominio è una classe che rappresenta i singoli valori che esistono all'interno di un "dominio concettuale".
 *  Può essere ripetuto all'interno di più domini ed ha opzionalmente una descrizione.
 */

@JsonRootName("ValoreDominio")
public class ValoreDominio {

    // obbligatorio
    @JacksonXmlProperty(localName = "nome")
    private String nome;
    // opzionale
    @JacksonXmlProperty(localName = "descrizione")
    private String descrizione;

    public ValoreDominio(String nome) {
        this.nome = nome;
    }

    public ValoreDominio(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public ValoreDominio (){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
