package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.parsing.Serializer;

import java.util.ArrayList;
import java.util.List;

public class CategorieRepository implements Repository<List<Categoria>> {

    private final String filePath;
    private final Serializer<List<Categoria>> serializer;

    public CategorieRepository(String filePath, Serializer<List<Categoria>> serializer) {
        this.filePath = filePath;
        this.serializer = serializer;
    }

    public void save(List<Categoria> list) {
        assert list!= null;
        assert this.filePath != null;
        serializer.serialize(this.filePath, list);
    }

    public List<Categoria> load() {
        assert this.filePath != null;

            List<Categoria> data = this.serializer.deserialize(new TypeReference<>() {
            }, this.filePath);

        return data == null ? new ArrayList<>() : data;
    }

}
