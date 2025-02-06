package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.parsing.Serializer;
import java.util.List;

public class CompGeoRepository implements Repository<List<ComprensorioGeografico>> {

    private final String filePath;
    private final Serializer<List<ComprensorioGeografico>> serializer;

    public CompGeoRepository(String filePath, Serializer<List<ComprensorioGeografico>> serializer) {
        this.filePath = filePath;
        this.serializer = serializer;
    }

    public void save(List<ComprensorioGeografico> listaComprensori) {
        assert listaComprensori!= null;
        assert this.filePath != null;
        serializer.serialize(this.filePath, listaComprensori);
    }

    public List<ComprensorioGeografico> load() {
        assert this.filePath != null;
        return this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

}
