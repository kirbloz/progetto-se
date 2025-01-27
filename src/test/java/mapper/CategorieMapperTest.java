package mapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.parsing.CategorieMapper;
import it.unibs.projectIngesoft.parsing.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CategorieMapperTest {

    private CategorieMapper mapper;

    @BeforeEach
    void prepareTest() {
        this.mapper = new CategorieMapper("categorieTest.json");
    }

    @Test
    void writeTest() {

        Albero tree = new Albero();
        Categoria radiceUno = new Categoria("provaRadice", "campoFiglieRadice");
        radiceUno.addCategoriaFiglia(new Categoria("provaFiglia",
                radiceUno,
                new ValoreDominio("valoreTest")));
        tree.aggiungiRadice(
                radiceUno
        );

        Serializer.serialize("categorieTest.json", tree.getRadici());

        // non so come controllare???
        //assert false;
    }


    void readXMLtest(){
        List<Categoria> tempCat;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File("categorie.xml");
            xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            assert file.exists() && file.length() != 0;

            // legge i dati dal file con un mapper
            tempCat = xmlMapper.readValue(file, new TypeReference<>(){});

            //tree.getRadici().clear();
            //if (tempCat != null) assert true;
            //data = xmlMapper.readValue(file, typeref);

            assert tempCat != null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            assert false;
        }




    }

    @Test
    void readTest() {
        /*assert this.tree != null;
        List<Categoria> tempCat = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        tree.getRadici().clear();
        if (tempCat != null) tree.getRadici().addAll(tempCat);*/

        List<Categoria> categorie = mapper.read();

        //Albero albero = Serializer.deserialize(new TypeReference<Albero>() {},"categorie.json");

        //assert albero != null;
        //assert !albero.getRadici().isEmpty();
        //CategorieModel model = new CategorieModel("categorie.xml", "fattori.xml");
        Albero tree = new Albero();
        categorie.forEach(tree::aggiungiRadice);

        System.out.println(tree.getRadici().toString());
        assert tree.getRadici() != null;
    }

}
