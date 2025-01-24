package it.unibs.projectIngesoft.parsing;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.main.IIOList;

import java.util.ArrayList;
import java.util.List;

public class CategorieMapper implements IIOList<Categoria> {

    private final String filePath;

    // TODO
    /*
        IMPLEMENTARE CATEGORIE WRAPPER.
        PER ORA SCRIVE SOLO LE CATEGORIE RADICE MA C'E' NECESSITA DI MEMORIZZARE
        L'INTERA GERARCHIA. PORCA PUPAZZA!
     */






    public CategorieMapper(String filePath) {
        this.filePath = filePath;
        //readList(); o set ListaUtenti
    }

    @Override
    public void write(List<Categoria> list) {
        assert list!= null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, list);
    }

    @Override
    public List<Categoria> read() {
        assert this.filePath != null;

        List<Categoria> tempCat = Serializer.deserialize(new TypeReference<List<Categoria>>() {
        }, this.filePath);

        return (tempCat != null) ?  tempCat : new ArrayList<>();
    }
}
