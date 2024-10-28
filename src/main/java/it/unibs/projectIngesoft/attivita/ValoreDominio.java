package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * ValoreDominio è una classe che rappresenta i singoli valori che esistono all'interno di un "dominio concettuale".
 * Può essere ripetuto all'interno di più domini ed ha opzionalmente una descrizione.
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
        this.setNome(nome);
        this.setDescrizione("");
    }

    public ValoreDominio(String nome, String descrizione) {
        this.setNome(nome);
        this.setDescrizione(descrizione);
    }

    /**
     * Da non usare! Solo per Jackson
     */
    public ValoreDominio() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        assert nome != null && !nome.trim().isEmpty() : "Il nome non deve essere null o vuoto";
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        assert descrizione != null : "La descrizione non deve essere null";
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getNome());
        if (!this.getDescrizione().trim().isEmpty())
            sb.append(", '").append(this.getDescrizione()).append("'");
        return sb.toString();
    }
}
