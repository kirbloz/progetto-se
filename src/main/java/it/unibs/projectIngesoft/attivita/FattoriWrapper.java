package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.util.HashMap;
import java.util.ArrayList;
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

    // costruttore di default per Jackson
    public FattoriWrapper() {
        this.fattori = new ArrayList<>();
    }

    public FattoriWrapper(HashMap<String, ArrayList<FattoreDiConversione>> map) {
        this.fattori = new ArrayList<>();
        for (Map.Entry<String, ArrayList<FattoreDiConversione>> entry : map.entrySet()) {
            this.fattori.add(new Entry(entry.getKey(), entry.getValue()));
        }
    }

    // metodo per convertire la lista di entry in una hashmap
    public HashMap<String, ArrayList<FattoreDiConversione>> toHashMap() {
        HashMap<String, ArrayList<FattoreDiConversione>> map = new HashMap<>();
        for (Entry entry : this.fattori) {
            String keyParsed = entry.getKey().split(" ")[0];
            map.put(keyParsed, entry.getValue());
        }
        return map;
    }

    public static class Entry {

        // Annotazione per specificare il nome del tag XML per la chiave
        @JacksonXmlProperty(localName = "key")
        private String key;

        // annotazione per racchiudere la lista 'values' in un singolo tag XML
        @JacksonXmlElementWrapper(localName = "values")

        // annotazione per specificare il nome del tag per ogni valore nella lista
        @JacksonXmlProperty(localName = "value")
        private ArrayList<FattoreDiConversione> value;

        public Entry() {
        }

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
