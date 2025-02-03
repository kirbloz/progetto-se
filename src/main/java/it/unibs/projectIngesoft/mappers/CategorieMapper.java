package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;

import java.util.ArrayList;
import java.util.List;

public class CategorieMapper implements Mapper<List<Categoria>> {

    private final String filePath;
    private final JacksonSerializer<List<Categoria>> jacksonSerializer;

    public CategorieMapper(String filePath, JacksonSerializer<List<Categoria>> jacksonSerializer) {
        this.filePath = filePath;
        this.jacksonSerializer = jacksonSerializer;
    }

    public void write(List<Categoria> list) {
        assert list!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, list);
    }

    public List<Categoria> read() {
        assert this.filePath != null;
        List<Categoria> data = this.jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        return data == null ? new ArrayList<>() : data;
    }

}
