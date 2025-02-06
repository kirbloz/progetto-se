package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.parsing.Serializer;

import java.util.ArrayList;
import java.util.List;

public class CompGeoRepository implements Repository<List<ComprensorioGeografico>> {

    private final String filePath;
    private final Serializer<List<ComprensorioGeografico>> serializer;

    public CompGeoRepository(String filePath, Serializer<List<ComprensorioGeografico>> serializer) {
        this.filePath = filePath;
        this.serializer = serializer;
    }

    public void save(List<ComprensorioGeografico> listaComprensori) {
        assert this.filePath != null;
        if (listaComprensori == null) listaComprensori = new ArrayList<>();
        serializer.serialize(this.filePath, listaComprensori);
    }

    public List<ComprensorioGeografico> load() {
        assert this.filePath != null;
        List<ComprensorioGeografico> data = this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        return data == null ? new ArrayList<>() : data;
    }

}
