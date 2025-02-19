package it.unibs.projectIngesoft.core.domain.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ValoreDominio è una classe che rappresenta i singoli valori che esistono all'interno di un "dominio concettuale".
 * Può essere ripetuto all'interno di più domini ed ha opzionalmente una descrizione.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValoreDominio {

    // obbligatorio
    @JsonProperty
    private String nome;
    // opzionale
    @JsonProperty
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
        assert descrizione != null : "La descrizione non deve essere null";
        this.descrizione = descrizione;
    }
}
