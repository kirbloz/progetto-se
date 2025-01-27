package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.ProposteWrapper;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProposteMapper {

    private final String filePath;
    private final JacksonSerializer<Map<String, List<Proposta>>> jacksonSerializer;


    public ProposteMapper(String filePath, JacksonSerializer<Map<String, List<Proposta>>>  jacksonSerializer) {
        this.filePath = filePath;
        this.jacksonSerializer = jacksonSerializer;
    }

    public void write(HashMap<String, List<Proposta>> listaProposte) {
        assert listaProposte!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, listaProposte);
    }

    public Map<String, List<Proposta>>  read() {
        assert this.filePath != null;
        return this.jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }


    /*public void write(Map<String, ArrayList<Proposta>> map) {
        assert map!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, toEntryList(map));
    }

    public HashMap<String, ArrayList<Proposta>> read() {
        assert this.filePath != null;

        List<Entry> entryList = jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        return toHashMap(entryList);
    }



    private HashMap<String, ArrayList<Proposta>> toHashMap(List<Entry> entryList){
        HashMap<String, ArrayList<Proposta>> map = new HashMap<>();
        for (Entry entry : entryList) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    private List<Entry> toEntryList(Map<String, ArrayList<Proposta>> map) {
        List<Entry> entryList = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Proposta>> entry : map.entrySet()) {
            entryList.add(new Entry(entry.getKey(), entry.getValue()));
        }
        return entryList;
    }*/


    /**
     * Classe interna, statica per rappresentare un singolo elemento della mappa.
     */
    public static class Entry {

        // Annotazione per specificare il nome del tag XML per la chiave
        @JsonProperty("key")
        @JacksonXmlProperty(localName = "key")
        private String key;

        // annotazione per racchiudere la lista 'values' in un singolo tag XML
        @JacksonXmlElementWrapper(localName = "values")
        // annotazione per specificare il nome del tag per ogni valore nella lista
        @JacksonXmlProperty(localName = "value")
        private ArrayList<Proposta> value;

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
        public Entry(String key, ArrayList<Proposta> value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public ArrayList<Proposta> getValue() {
            return value;
        }
    }


}
