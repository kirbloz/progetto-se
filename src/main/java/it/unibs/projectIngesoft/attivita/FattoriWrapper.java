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
    @JacksonXmlElementWrapper(localName = "fattori")
    @JacksonXmlProperty(localName = "entry")
    private List<Entry> fattori;

    public FattoriWrapper() {
        this.fattori = new ArrayList<>();
    }

    public FattoriWrapper(HashMap<String, ArrayList<FattoreDiConversione>> map) {
        this.fattori = new ArrayList<>();
        for (Map.Entry<String, ArrayList<FattoreDiConversione>> entry : map.entrySet()) {
            this.fattori.add(new Entry(entry.getKey(), entry.getValue()));
        }
    }

    public HashMap<String, ArrayList<FattoreDiConversione>> toHashMap() {
        HashMap<String, ArrayList<FattoreDiConversione>> map = new HashMap<>();
        for (Entry entry : this.fattori) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static class Entry {
        @JacksonXmlProperty(localName = "key")
        private String key;

        @JacksonXmlElementWrapper(localName = "values")
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
