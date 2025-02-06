package it.unibs.projectIngesoft.persistence.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.core.domain.entities.ComprensorioGeografico;
import it.unibs.projectIngesoft.persistence.SerializerBasedRepository;
import it.unibs.projectIngesoft.persistence.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

public class CompGeoRepository extends SerializerBasedRepository<List<ComprensorioGeografico>> {

    public CompGeoRepository(String filePath, Serializer<List<ComprensorioGeografico>> serializer) {
        super(filePath, serializer);
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
