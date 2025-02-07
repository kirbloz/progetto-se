package it.unibs.projectIngesoft.persistence.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.persistence.SerializerBasedRepository;
import it.unibs.projectIngesoft.persistence.serialization.Serializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FattoriDiConversioneRepository extends SerializerBasedRepository<Map<String, List<FattoreDiConversione>>> {

    public FattoriDiConversioneRepository(String filePath,
                                          Serializer<Map<String, List<FattoreDiConversione>>> serializer) {
        super(filePath, serializer);
    }

    public void save(Map<String, List<FattoreDiConversione>> hashListaFdC) {
        assert this.filePath != null;
        if (hashListaFdC == null) hashListaFdC = new HashMap<>();
        serializer.serialize(this.filePath, hashListaFdC);
    }

    public Map<String, List<FattoreDiConversione>> load() {
        assert this.filePath != null;
        Map<String, List<FattoreDiConversione>> data = this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        return data == null ? new HashMap<>() : data;
    }

}
