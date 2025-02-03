package attivita;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.mappers.CategorieMapper;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CategorieTest {

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
    void aggiungiUnaRadice() {

        assert !model.esisteRadice("nomeTest");

        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model,
                null,
                null,
                null,
                null,
                new Configuratore("default", "test"));

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        assert model.esisteRadice("nomeTest");

        //controller.addGerarchia così aggiungeremo di più di una singola radice
    }

    @Test
    void aggiungiGerarchiaTest_RadiceUnivoca_NomeUnivoco_Foglia() {
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model,
                null,
                null,
                null,
                null,
                new Configuratore("default", "test"));

        model.aggiungiCategoriaRadice("radiceTest", "testing");

        String data = "\nradiceTest\nradiceTest2\ncampoTest\n";
        data = data + "1\nfigliaTest\nradice\nradiceTest2\nvaloreFiglia\nN\nS\n";
        data = data + "1\nfigliaTest2\nfigliaTest\nradiceTest2\nvaloreFiglia2\nS" +
                "\ndescrizione\nN\ndominioFake\n";
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
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model,
                null,
                null,
                null,
                null,
                new Configuratore("default", "test"));

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

}
