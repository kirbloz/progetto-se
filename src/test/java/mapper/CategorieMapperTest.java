package mapper;

import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.parsing.CategorieMapper;
import it.unibs.projectIngesoft.parsing.Serializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
