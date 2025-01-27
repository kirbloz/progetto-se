package mapper;

import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.gestori.CategorieModel;
import it.unibs.projectIngesoft.gestori.UtentiModel;
import it.unibs.projectIngesoft.mappers.CategorieMapper;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.parsing.SerializerXML;
import it.unibs.projectIngesoft.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CategorieMapperTest {

    private CategorieMapper mapper;
    boolean xml;
    private CategorieModel model;

    @BeforeEach
    void prepareTest() {

        CategorieMapper mapper = new CategorieMapper("categorieTest.json",
                new SerializerJSON<List<Categoria>>());

        this.model = new CategorieModel("fakefattoristringpath",
                mapper);

         /*xml = true;

        if(!xml){
            this.mapper = new CategorieMapper("categorieTest.json",
                    new SerializerJSON<List<Categoria>>());
        }else{
            this.mapper = new CategorieMapper("categorie.xml",
                    new SerializerXML<List<Categoria>>());
        }*/
        //this.mapper = new CategorieMapper("categorieTest.json");
    }

    @Test
    void writeTest() {
        if(xml)
            return;

        /*Albero tree = new Albero();
        Categoria radiceUno = new Categoria("Ripetizioni Scolastiche",
                "tipo");
        radiceUno.addCategoriaFiglia(new Categoria("Ripetizioni di Materie Umanistiche",
                radiceUno,
                new ValoreDominio("valoreTest")));
        tree.aggiungiRadice(
                radiceUno
        );*/

        Categoria radice = new Categoria("radicePORCAPUPAZZA");
        model.getRadici().add(radice);
        assert model.getRadici().contains(radice);

        //SerializerJSON.serialize("categorieTest.json", tree.getRadici());

        // non so come controllare???
        //assert false;
        //mapper.write(tree.getRadici());
    }


    /*void readXMLtest(){
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
    }*/

    @Test
    void readTest() {
        /*assert this.tree != null;
        List<Categoria> tempCat = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);

        tree.getRadici().clear();
        if (tempCat != null) tree.getRadici().addAll(tempCat);*/

        //List<Categoria> categorie = mapper.read();

        //Albero albero = Serializer.deserialize(new TypeReference<Albero>() {},"categorie.json");

        //assert albero != null;
        //assert !albero.getRadici().isEmpty();
        //CategorieModel model = new CategorieModel("categorie.xml", "fattori.xml");
        //Albero tree = new Albero();
        //categorie.forEach(tree::aggiungiRadice);

       // System.out.println(tree.getRadici().toString());

        model.visualizzaGerarchie();

        assert model.getRadici() != null;
    }

}
