package it.unibs.projectIngesoft.persistence.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.persistence.SerializerBasedRepository;
import it.unibs.projectIngesoft.persistence.serialization.Serializer;

import java.util.ArrayList;
import java.util.List;

public class CategorieRepository extends SerializerBasedRepository<List<Categoria>> {

    public CategorieRepository(String filePath, Serializer<List<Categoria>> serializer) {
        super(filePath, serializer);
    }

    public void save(List<Categoria> list) {
        assert this.filePath != null;
        if (list == null) list = new ArrayList<>();
        serializer.serialize(this.filePath, list);
    }

    public List<Categoria> load() {
        assert this.filePath != null;
        List<Categoria> data = this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        return data == null ? new ArrayList<>() : data;
    }

}
