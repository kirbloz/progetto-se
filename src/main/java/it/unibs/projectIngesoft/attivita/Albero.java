package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe creata per facilitare la serializzazione XML dei dati relativi alle categorie gestite da GestoreCategorie.
 */
@JacksonXmlRootElement(localName = "Albero")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("Albero")
public class Albero {

    /*
     * Queste annotazioni permettono di evitare la serializzazione come "<ArrayList><item>..." che poi causa
     * problemi durante la de-serializzazione.
     */
    @JacksonXmlProperty(localName = "radice")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Categoria> radici;

    /**
     * Costruttore default che inizializza la lista delle radici.
     */
    public Albero() {
        this.radici = new ArrayList<>();
    }

    /**
     * Verifica se esiste una radice con il nome specificato.
     *
     * @param nomeRadice il nome della radice da cercare
     * @return true se esiste una radice con il nome specificato, false altrimenti
     */
    public boolean contains(String nomeRadice) {
        assert nomeRadice != null
                && !nomeRadice.trim().isEmpty() : "Il nome della radice non deve essere null o vuoto";
        if (radici == null || radici.isEmpty()) {
            return false;
        }

        for (Categoria tempRadice : this.radici) {
            if (tempRadice.getNome().equals(nomeRadice))
                return true;
        }
        return false;
    }

    /**
     * Restituisce la lista delle radici.
     *
     * @return la lista delle radici
     */
    public List<Categoria> getRadici() {
        return radici;
    }

    public void setRadici(List<Categoria> radici) {
        this.radici = Objects.requireNonNullElseGet(radici, ArrayList::new);
    }

    /**
     * Aggiunge una nuova radice alla lista delle radici.
     *
     * @param radice la radice da aggiungere
     */
    public void aggiungiRadice(Categoria radice) {
        assert radice != null : "La radice da aggiungere non deve essere null";
        this.radici.add(radice);
    }

    /**
     * Restituisce la radice con il nome specificato.
     *
     * @param nomeRadice il nome della radice da cercare
     * @return la radice con il nome specificato, null se non esiste
     */
    public Categoria getRadice(String nomeRadice) {
        assert nomeRadice != null
                && !nomeRadice.trim().isEmpty() : "Il nome della radice non deve essere null o vuoto";
        for (Categoria tempRadice : this.radici) {
            if (tempRadice.getNome().equals(nomeRadice))
                return tempRadice;
        }
        return null;
    }

    /**
     * Punto di partenza per la ricorsione e ricerca di foglie all'interno di una sola gerarchia.
     *
     * @param nomeRadice, radice di riferimento della gerarchia.
     * @return lista delle foglie.
     */
    public List<Categoria> getFoglie(String nomeRadice) {
        return this.getRadice(nomeRadice).getFoglie();
    }

}
