package it.unibs.projectIngesoft.attivita;

/**
 *  ValoreDominio è una classe che rappresenta i singoli valori che esistono all'interno di un "dominio concettuale".
 *  Può essere ripetuto all'interno di più domini ed ha opzionalmente una descrizione.
 */
public class ValoreDominio {

    // obbligatorio
    private String nome;
    // opzionale
    private String descrizione;

    public ValoreDominio(String nome) {
        this.nome = nome;
    }

    public ValoreDominio(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
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
