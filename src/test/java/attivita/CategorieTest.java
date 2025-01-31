package attivita;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.mappers.CategorieMapper;
import it.unibs.projectIngesoft.mappers.FattoriMapper;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.model.FattoriModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CategorieTest {

    private CategorieModel model;
    //private CategorieController controller;

    private CategorieMapper mapper;
    private List<Categoria> cleanTestData;


    @BeforeEach
    void prepareTest() {
        mapper = new CategorieMapper("categorieTest1.json",
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
    void aggiungiUnaRadice(){

        assert !model.esisteRadice("nomeTest");

        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model,
                null,
                null,
                null,
                null);

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        assert model.esisteRadice("nomeTest");

        //controller.addGerarchia così aggiungeremo di più di una singola radice
    }

    @Test
    void aggiungiGerarchiaTest_RadiceUnivoca_NomeUnivoco_Foglia() {
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model,
                new FattoriModel(new FattoriMapper(
                        "fattoriTest1.json",
                        new SerializerJSON<>()
                )),
                null,
                null,
                null);

        String data = "radiceTest\ncampoTest\n";
        data = data + "0\n";

        InputInjector.inject(data);
        controller.aggiungiGerarchia();

    }


    @Test
    void aggiungiMadreFiglia_TipoValoreDominio() {

        assert !false;
    }

    @Test
    void aggiungiDescrizioneValoreDominio() {
        //aggiungiUnaRadiceDiTest();
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model,
                null,
                null,
                null,
                null);

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        Categoria categoria = new Categoria("test",
                model.getRadici().getLast(),
                new ValoreDominio("nomeValoreTest"));

        assert categoria.getValoreDominio().getDescrizione().isEmpty();
        categoria.getValoreDominio().setDescrizione("test");
        assert categoria.getValoreDominio().getDescrizione().equals("test");
        //questa concatenazione è nauseabonda, non abbiamo un metodo di controller vero?
    }

    @Test
    void checkCalcoloFattoriIntermedi() {
        assert !false;
        //servirà capire con il fattori model
        //martino ti invoco @martibarimaff
    }

}
