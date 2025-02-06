package it.unibs.projectIngesoft.mappers;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.parsing.Serializer;
import java.util.List;
import java.util.Map;

public class ProposteRepository implements Repository<Map<String, List<Proposta>>> {

    private final String filePath;
    private final Serializer<Map<String, List<Proposta>>> serializer;

    public ProposteRepository(String filePath, Serializer<Map<String, List<Proposta>>> serializer) {
        this.filePath = filePath;
        this.serializer = serializer;
    }

    public void save(Map<String, List<Proposta>> listaProposte) {
        assert listaProposte!= null;
        assert this.filePath != null;
        serializer.serialize(this.filePath, listaProposte);
    }

    public Map<String, List<Proposta>> load() {
        assert this.filePath != null;
        return this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

}
