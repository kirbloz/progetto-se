package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FattoriMapper {

    private final String filePath;
    private final JacksonSerializer<Map<String, List<FattoreDiConversione>>> jacksonSerializer;

    public FattoriMapper(String filePath,
                         JacksonSerializer<Map<String, List<FattoreDiConversione>>>  jacksonSerializer) {
        this.filePath = filePath;
        this.jacksonSerializer = jacksonSerializer;
    }

    public void write(Map<String, List<FattoreDiConversione>> hashListaFdC) {
        assert hashListaFdC!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, hashListaFdC);
    }

    public Map<String, List<FattoreDiConversione>>  read() {
        assert this.filePath != null;
        return this.jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
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
        private List<FattoreDiConversione> value;

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
        public Entry(String key, List<FattoreDiConversione> value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public ArrayList<FattoreDiConversione> getValue() {
            return new ArrayList<>(value);
        }
    }

}
