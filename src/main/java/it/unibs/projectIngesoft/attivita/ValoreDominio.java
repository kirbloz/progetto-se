package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * ValoreDominio è una classe che rappresenta i singoli valori che esistono all'interno di un "dominio concettuale".
 * Può essere ripetuto all'interno di più domini ed ha opzionalmente una descrizione.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("ValoreDominio")
public class ValoreDominio {

    // obbligatorio
    @JacksonXmlProperty
    private String nome;
    // opzionale
    @JacksonXmlProperty
    private String descrizione;

    /**
     * Da non usare! Solo per la deserializzazione di Jackson
     */
    public ValoreDominio() {
    }

    /**
     * Costruttore con nome
     *
     * @param nome, nome del valore
     */
    public ValoreDominio(String nome) {
        this.setNome(nome);
        this.setDescrizione("");
    }

    /**
     * Costruttore con nome e descrizione
     *
     * @param nome,       nome del valore del dominio
     * @param descrizione descrizione del valore del dominio
     */
    public ValoreDominio(String nome, String descrizione) {
        this.setNome(nome);
        this.setDescrizione(descrizione);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        assert nome != null
                && !nome.trim().isEmpty() : "Il nome non deve essere null o vuoto";
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        assert descrizione != null
                /*&& !descrizione.trim().isEmpty()*/ : "La descrizione non deve essere null";
        this.descrizione = descrizione;
    }

    /**
     * Rappresenta sotto forma di stringa formattata l'oggetto ValoreDominio
     *
     * @return stringa formattata
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getNome());
        if (!this.getDescrizione().trim().isEmpty())
            sb.append(", '").append(this.getDescrizione()).append("'");
        return sb.toString();
    }
}
