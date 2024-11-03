package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe di appoggio per la serializzazione dell'attributo HashMap fattori in GestoreFattori.
 */
@JacksonXmlRootElement(localName = "FattoriWrapper")
public class FattoriWrapper {

    // annotazione per racchiudere la lista "fattori" in un solo tag XML
    @JacksonXmlElementWrapper(localName = "fattori")
    // annotazione per racchiudere ogni elemento della lista in una tag "entry"
    @JacksonXmlProperty(localName = "entry")
    private List<Entry> fattori;

    /**
     * Costruttore di default necessario per la deserializzazione di Jackson
     */
    public FattoriWrapper() {
        this.fattori = new ArrayList<>();
    }

    /**
     * Costruttore con parametro per inizializzare e popolare l'attributo fattori.
     * Converte ogni entry della HashMap in ingresso in un oggetto Entry e lo aggiunge alla lista
     * attributo fattori.
     *
     * @param map, mappa per costruire l'attributo fattori
     */
    public FattoriWrapper(Map<String, ArrayList<FattoreDiConversione>> map) {
        this.fattori = new ArrayList<>();
        for (Map.Entry<String, ArrayList<FattoreDiConversione>> entry : map.entrySet()) {
            this.fattori.add(new Entry(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Converte la lista di Entry in una HashMap.
     * Serve durante la deserializzazione: per ogni Entry della lista, si estraggono la chiave
     * e il valore tramite split, poi li si inserisce nella Map che si sta costruendo
     *
     * @return map, HashMap costruita
     */
    public HashMap<String, ArrayList<FattoreDiConversione>> toHashMap() {
        HashMap<String, ArrayList<FattoreDiConversione>> map = new HashMap<>();
        for (Entry entry : this.fattori) {
            String keyParsed = entry.getKey().split(" ")[0];
            map.put(keyParsed, entry.getValue());
        }
        return map;
    }

    /**
     * Classe interna, statica per rappresentare un singolo elemento della mappa.
     */
    public static class Entry {

        // Annotazione per specificare il nome del tag XML per la chiave
        @JacksonXmlProperty(localName = "key")
        private String key;

        // annotazione per racchiudere la lista 'values' in un singolo tag XML
        @JacksonXmlElementWrapper(localName = "values")
        // annotazione per specificare il nome del tag per ogni valore nella lista
        @JacksonXmlProperty(localName = "value")
        private ArrayList<FattoreDiConversione> value;

        /**
         * Costruttore default necessario per la deserializzazione di Jackson
         */
        public Entry() {
        }

        /**
         * Costruttore con parametri per inizializzare e impostare gli attributi.
         * @param key, chiave
         * @param value, valore
         */
        public Entry(String key, ArrayList<FattoreDiConversione> value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public ArrayList<FattoreDiConversione> getValue() {
            return value;
        }
    }
}
