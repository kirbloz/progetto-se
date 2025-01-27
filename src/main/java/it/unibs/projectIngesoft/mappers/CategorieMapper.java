package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;
import java.util.List;

public class CategorieMapper{

    private final String filePath;

    private final JacksonSerializer<List<Categoria>> jacksonSerializer;
    // TODO
    /*
        IMPLEMENTARE CATEGORIE WRAPPER.
        PER ORA SCRIVE SOLO LE CATEGORIE RADICE MA C'E' NECESSITA DI MEMORIZZARE
        L'INTERA GERARCHIA. PORCA PUPAZZA!
     */

    public CategorieMapper(String filePath, JacksonSerializer<List<Categoria>> jacksonSerializer) {
        this.filePath = filePath;
        //readList(); o set ListaUtenti
        this.jacksonSerializer = jacksonSerializer;
    }

    public void write(List<Categoria> list) {
        assert list!= null;
        assert this.filePath != null;
        jacksonSerializer.serialize(this.filePath, list);
    }

    public List<Categoria> read() {
        assert this.filePath != null;
        return this.jacksonSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

}
