package attivita;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.mappers.CategorieMapper;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CategorieTest {

    private CategorieModel model;

    private CategorieMapper mapper;
    private List<Categoria> cleanTestData;


    @BeforeEach
    void prepareTest() {
        mapper = new CategorieMapper("categorieTest.json",
                new SerializerJSON<List<Categoria>>()
        );

        cleanTestData = new ArrayList<>();
        cleanTestData = mapper.read();

        this.model = new CategorieModel(mapper);
    }

    @AfterEach
    void tearDown() {
        mapper.write(cleanTestData);
    }

    @Test
    void aggiungiGerarchiaTest_RadiceUnivoca_NomeUnivoco_Foglia(){
        assert false;
    }


    @Test
    void aggiungiMadreFiglia_TipoValoreDominio(){
        assert false;
    }

    @Test
    void aggiungiDescrizioneValoreDominio(){
        assert false;
    }

    @Test
    void checkCalcoloFattoriIntermedi(){
        assert false;
        //servirà capire con il fattori model
        //martino ti invoco
    }

}
