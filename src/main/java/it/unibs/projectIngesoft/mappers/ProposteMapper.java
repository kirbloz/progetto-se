package it.unibs.projectIngesoft.mappers;


import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;
import java.util.List;
import java.util.Map;

public class ProposteMapper implements Mapper<Map<String, List<Proposta>>>{

    private final String filePath;
    private final JacksonSerializer<Map<String, List<Proposta>>> jacksonSerializer;

    public ProposteMapper(String filePath, JacksonSerializer<Map<String, List<Proposta>>>  jacksonSerializer) {
        this.filePath = filePath;
        this.jacksonSerializer = jacksonSerializer;
    }

    public void write(Map<String, List<Proposta>> listaProposte) {
        assert listaProposte!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, listaProposte);
    }

    public Map<String, List<Proposta>>  read() {
        assert this.filePath != null;
        return this.jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

}
