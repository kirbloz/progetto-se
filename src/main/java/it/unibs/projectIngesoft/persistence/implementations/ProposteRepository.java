package it.unibs.projectIngesoft.persistence.implementations;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.persistence.SerializerBasedRepository;
import it.unibs.projectIngesoft.persistence.serialization.Serializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProposteRepository extends SerializerBasedRepository<Map<String, List<Proposta>>> {

    public ProposteRepository(String filePath, Serializer<Map<String, List<Proposta>>> serializer) {
        super(filePath, serializer);
    }

    public void save(Map<String, List<Proposta>> listaProposte) {
        assert this.filePath != null;
        if (listaProposte == null) listaProposte = new HashMap<>();
        serializer.serialize(this.filePath, listaProposte);
    }

    public Map<String, List<Proposta>> load() {
        assert this.filePath != null;
        Map<String, List<Proposta>> data = this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        return data == null ? new HashMap<>() : data;
    }

}
