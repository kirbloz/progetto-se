package mapper;

import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.mappers.CategorieMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


        Albero tree = new Albero();

        Categoria radice1 = new Categoria("Ripetizioni Scolastiche",
                "tipo");

        Categoria figlia11 = new Categoria("Ripetizioni di Materie Umanistiche",
                "materia",
                radice1,
                new ValoreDominio("umanistiche", "Materie umanistiche"));

        radice1.addCategoriaFiglia(figlia11);

        figlia11.addCategoriaFiglia(new Categoria("Ripetizioni di Letteratura Italiana",
            figlia11, new ValoreDominio("italiano")));
        figlia11.addCategoriaFiglia(new Categoria("Ripetizioni di Filosofia",
                figlia11, new ValoreDominio("filosofia")));
        figlia11.addCategoriaFiglia(new Categoria("Ripetizioni di Storia",
                figlia11, new ValoreDominio("storia", "Lezioni di racconti vari")));
        figlia11.addCategoriaFiglia(new Categoria("Ripetizioni di Storia dell'Arte",
                figlia11, new ValoreDominio("storia dell'arte")));

        Categoria figlia12 = new Categoria("Ripetizioni di Materie Scientifiche",
                "materia",
                radice1,
                new ValoreDominio("scientifiche", "Materie Scientifiche"));

        radice1.addCategoriaFiglia(figlia12);

        figlia12.addCategoriaFiglia(new Categoria("Ripetizioni di Matematica",
                figlia12, new ValoreDominio("matematica")));
        figlia12.addCategoriaFiglia(new Categoria("Ripetizioni di Fisica",
                figlia12, new ValoreDominio("fisica")));
        figlia12.addCategoriaFiglia(new Categoria("Ripetizioni di Informatica",
                figlia12, new ValoreDominio("informatica")));
        figlia12.addCategoriaFiglia(new Categoria("Ripetizioni di Chimica",
                figlia12, new ValoreDominio("chimica")));
        figlia12.addCategoriaFiglia(new Categoria("Ripetizioni di Biologia",
                figlia12, new ValoreDominio("biologia")));
        figlia12.addCategoriaFiglia(new Categoria("Ripetizioni di Scienze della Terra",
                figlia12, new ValoreDominio("scienze della terra")));

        Categoria figlia13 = new Categoria("Ripetizioni di lingue",
                "materia",
                radice1,
                new ValoreDominio("lingue"));

        radice1.addCategoriaFiglia(figlia13);

        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Inglese",
                figlia13, new ValoreDominio("inglese")));
        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Spagnolo",
                figlia13, new ValoreDominio("spagnolo")));
        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Francese",
                figlia13, new ValoreDominio("francese")));
        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Tedesco",
                figlia13, new ValoreDominio("tedesco")));
        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Latino",
                figlia13, new ValoreDominio("latino")));
        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Greco",
                figlia13, new ValoreDominio("greco")));
        figlia13.addCategoriaFiglia(new Categoria("Ripetizioni di Arabo",
                figlia13, new ValoreDominio("arabo")));


        Categoria radice2 = new Categoria("Lezioni di musica",
                "tipo");

        Categoria figlia21 = new Categoria("Lezioni di teoria musicale",
                "materia",
                radice2,
                new ValoreDominio("teoria"));


        figlia21.addCategoriaFiglia(new Categoria("Lezioni di solfeggio",
                figlia21, new ValoreDominio("solfeggio")));
        figlia21.addCategoriaFiglia(new Categoria("Lezioni di storia della musica",
                figlia21, new ValoreDominio("storia della musica")));

        radice2.addCategoriaFiglia(figlia21);



        Categoria radice3 = new Categoria("Attivita di svago",
                "tipo");
        Categoria figlia31 = new Categoria("Giochi di Logica",
                "gioco",
                radice3,
                new ValoreDominio("logica"));

        figlia31.addCategoriaFiglia(new Categoria("Lezioni di Scacchi",
                figlia31, new ValoreDominio("scacchi")));
        figlia31.addCategoriaFiglia(new Categoria("Lezioni di Sudoku",
                figlia31, new ValoreDominio("sudoku")));

        radice3.addCategoriaFiglia(figlia31);


        tree.getRadici().add(radice1);
        tree.getRadici().add(radice2);
        tree.getRadici().add(radice3);




        System.out.println(tree.getRadici());
        //model.setRadici(tree.getRadici());
        model.getRadici();
        System.out.println(model.getRadici());

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