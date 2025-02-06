package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.parsing.Serializer;

import java.util.List;
import java.util.Map;

public class FattoriDiConversioneRepository implements Repository<Map<String, List<FattoreDiConversione>>> {

    private final String filePath;
    private final Serializer<Map<String, List<FattoreDiConversione>>> serializer;

    public FattoriDiConversioneRepository(String filePath,
                                          Serializer<Map<String, List<FattoreDiConversione>>> serializer) {
        this.filePath = filePath;
        this.serializer = serializer;
    }

    public void save(Map<String, List<FattoreDiConversione>> hashListaFdC) {
        assert hashListaFdC != null;
        assert this.filePath != null;
        serializer.serialize(this.filePath, hashListaFdC);
    }

    public Map<String, List<FattoreDiConversione>> load() {
        assert this.filePath != null;
        return this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

}
