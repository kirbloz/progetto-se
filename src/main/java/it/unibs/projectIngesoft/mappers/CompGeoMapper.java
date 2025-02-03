package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;
import java.util.List;

public class CompGeoMapper implements Mapper<List<ComprensorioGeografico>> {

    private final String filePath;
    private final JacksonSerializer<List<ComprensorioGeografico>> jacksonSerializer;

    public CompGeoMapper(String filePath, JacksonSerializer<List<ComprensorioGeografico>>  jacksonSerializer) {
        this.filePath = filePath;
        this.jacksonSerializer = jacksonSerializer;
    }

    public void write(List<ComprensorioGeografico> listaComprensori) {
        assert listaComprensori!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, listaComprensori);
    }

    public List<ComprensorioGeografico> read() {
        assert this.filePath != null;
        return this.jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

}
