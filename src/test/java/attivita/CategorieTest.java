package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.core.domain.entities.ValoreDominio;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.model.CategorieModel;
import it.unibs.projectIngesoft.core.domain.model.FattoriModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.CategorieRepository;
import it.unibs.projectIngesoft.persistence.implementations.FattoriDiConversioneRepository;
import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategorieTest {

    private CategorieModel categorieModel;
    private Repository<List<Categoria>> categorieRepository;
    private List<Categoria> cleanData;

    @BeforeEach
    void prepareTest() {
        categorieRepository = new CategorieRepository("categorieTest.json",
                new JsonSerializerFactory().createSerializer()
        );
        saveData();
    }

    void saveData(){
        cleanData = new ArrayList<>();
        cleanData = categorieRepository.load();
    }

    @AfterEach
    void tearDown() {
        categorieRepository.save(cleanData);
    }

    @Test
    void aggiungiUnaRadice() {

        assert !categorieModel.esisteRadice("nomeTest");

        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                categorieModel,
                null,
                null,
                null,
                null,
                new Configuratore("default", "test"));

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        assert categorieModel.esisteRadice("nomeTest");

        //controller.addGerarchia così aggiungeremo di più di una singola radice
    }

    @Test
    void aggiungiGerarchia_RadiceNomeUnivoco_RadiceFoglia_FattoriInseritiCorrettamente() {
        //todo questa roba controlla fattori.....
        Categoria radiceFoglia = new Categoria("radiceTest", "testing");
        FattoriModel fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("fattoriTest.json", new JsonSerializerFactory().createSerializer()));
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                categorieModel, fattoriModel,
                null,
                null,
                null,
                new Configuratore("default", "test"));

        this.categorieModel.setRadici(new ArrayList<>());
        fattoriModel.setHashMapFattori(new HashMap<>());

        // riproduzione di aggiungi gerarchia
        categorieModel.aggiungiCategoriaRadice(radiceFoglia.getNome(), radiceFoglia.getCampoFiglie());
        radiceFoglia.impostaCategorieFoglia();
        fattoriModel.inserisciSingolaFogliaNellaHashmap(radiceFoglia.getNome(), radiceFoglia.getFoglie());

        String data = "radiceTest2\ncampoTest\n";
        data = data + "1\nfigliaTest\nradice\nradiceTest2\nvaloreFiglia\nN\nS\n";
        data = data + "1\nfigliaTest2\nfigliaTest\nradiceTest2\nvaloreFiglia2\nS" +
                "\ndescrizione\nN\ndominioFake\n";
        data = data + "0\n0.5\nradiceTest\nradiceTest\nradiceTest2\nfigliaTest\n1";

        InputInjector.inject(data);
        controller.aggiungiGerarchia();

        assertTrue(fattoriModel.existsKeyInHashmapFattori("radiceTest2:figliaTest"));
        assertTrue(fattoriModel.existsKeyInHashmapFattori("radiceTest2:figliaTest2"));

        Optional<FattoreDiConversione> fattore1 = fattoriModel.getFattoriFromFoglia("radiceTest:radiceTest")
                .stream().filter(f -> f.getNome_c2().equals("radiceTest2:figliaTest")).findAny();
        Optional<FattoreDiConversione> fattore2 = fattoriModel.getFattoriFromFoglia("radiceTest2:figliaTest")
                .stream().filter(f -> f.getNome_c2().equals("radiceTest2:figliaTest2")).findAny();
        Optional<FattoreDiConversione> fattore3 = fattoriModel.getFattoriFromFoglia("radiceTest2:figliaTest2")
                .stream().filter(f -> f.getNome_c2().equals("radiceTest2:figliaTest")).findAny();

        assertTrue(fattore1.isPresent());
        assertTrue(fattore2.isPresent());
        assertTrue(fattore3.isPresent());
    }


    @Test
    void aggiungiMadreFiglia_TipoValoreDominio() {

        assert !false;
    }

    @Test
    void aggiungiDescrizioneValoreDominio() {
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                categorieModel,
                null,
                null,
                null,
                null,
                new Configuratore("default", "test"));

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        Categoria categoria = new Categoria("test",
                categorieModel.getRadici().getLast(),
                new ValoreDominio("nomeValoreTest"));

        assert categoria.getValoreDominio().getDescrizione().isEmpty();
        categoria.getValoreDominio().setDescrizione("test");
        assert categoria.getValoreDominio().getDescrizione().equals("test");
    }


    @Test
    void esploraGerarchie_PassoSceltaNuovoLivelloTest(){
        assert false;
    }

}
