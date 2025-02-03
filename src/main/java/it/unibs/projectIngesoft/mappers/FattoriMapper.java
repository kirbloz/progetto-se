package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;

import java.util.List;
import java.util.Map;

public class FattoriMapper implements Mapper<Map<String, List<FattoreDiConversione>>>{

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

}
