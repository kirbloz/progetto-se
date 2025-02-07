package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.ValoreDominio;
import it.unibs.projectIngesoft.core.domain.model.CategorieModel;
import it.unibs.projectIngesoft.core.domain.model.FattoriModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.CategorieRepository;
import it.unibs.projectIngesoft.persistence.implementations.FattoriDiConversioneRepository;
import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.controllers.FruitoreController;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import it.unibs.projectIngesoft.presentation.view.FruitoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategorieTest {

    private CategorieModel categorieModel;
    private Repository<List<Categoria>> categorieRepository;
    private List<Categoria> cleanData;

    @BeforeEach
    void prepareTest() {

        categorieRepository = new CategorieRepository("mock.json",
                new JsonSerializerFactory().createSerializer()
        );
        categorieModel = new CategorieModel(categorieRepository);
        saveData();
    }

    void saveData() {
        cleanData = new ArrayList<>();
        cleanData = categorieRepository.load();
    }

    @AfterEach
    void tearDown() {
        categorieRepository.save(cleanData);
    }

    @Test
    void aggiungiUnaRadice() {
        assertFalse(categorieModel.esisteRadice("nomeTest"));
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                categorieModel,
                null,
                null,
                null,
                null,
                null);

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        assertTrue(categorieModel.esisteRadice("nomeTest"));
    }

    @Test
    void aggiungiGerarchia_RadiceNomeUnivoco_RadiceFoglia_FattoriInseritiCorrettamente() {
        Categoria radiceFoglia = new Categoria("radiceTest", "testing");
        FattoriModel fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("fattoriTest.json", new JsonSerializerFactory().createSerializer()));
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                categorieModel, fattoriModel,
                null,
                null,
                null,
                null);

        this.categorieModel.setRadici(new ArrayList<>());
        fattoriModel.setHashMapFattori(new HashMap<>());

        // riproduzione di aggiungi gerarchia
        categorieModel.aggiungiCategoriaRadice(radiceFoglia.getNome(), radiceFoglia.getCampoFiglie());
        radiceFoglia.impostaCategorieFoglia();
        fattoriModel.inserisciSingolaFogliaNellaHashmap(radiceFoglia.getNome(), radiceFoglia.getFoglie());

        String data = "radiceTest2\ncampoTest\n" +
                "1\nfigliaTest\nradice\nradiceTest2\nvaloreFiglia\nN\nS\n" +
                "1\nfigliaTest2\nfigliaTest\nradiceTest2\nvaloreFiglia2\nS" +
                "\ndescrizione\nN\ndominioFake\n" +
                "0\n0.5\nradiceTest\nradiceTest\nradiceTest2\nfigliaTest\n1";

        InputInjector.inject(data);
        controller.aggiungiGerarchia();

        assertTrue(categorieModel.esisteRadice("radiceTest2"));
        assertTrue(categorieModel.esisteRadice("radiceTest"));
        assertNull(categorieModel.getRadice("figliaTest"));
        assertNull(categorieModel.getRadice("figliaTest2"));
        assertTrue(categorieModel.esisteCategoriaNellaGerarchia("figliaTest", "radiceTest2"));
        assertTrue(categorieModel.esisteCategoriaNellaGerarchia("figliaTest2", "radiceTest2"));
    }

    @Test
    void aggiungiDescrizioneValoreDominio() {
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                categorieModel,
                null,
                null,
                null,
                null,
                null);

        InputInjector.inject("nomeTest\ncampoTest\n");
        controller.aggiungiRadice();

        Categoria categoria = new Categoria("test",
                categorieModel.getRadici().getLast(),
                new ValoreDominio("nomeValoreTest"));

        assertTrue(categoria.getValoreDominio().getDescrizione().isEmpty());
        categoria.getValoreDominio().setDescrizione("test");
        assertEquals("test", categoria.getValoreDominio().getDescrizione());
    }

    @Test
    void esploraGerarchie_PassoSceltaNuovoLivelloTest() {

        Categoria radice = new Categoria("radiceTest", "tipo");
        Categoria figlia1 = new Categoria("figlia1", "box", radice, new ValoreDominio("test"));
        Categoria figlia2 = new Categoria("figlia2", radice, new ValoreDominio("black"));
        Categoria figlia3 = new Categoria("figlia2", radice, new ValoreDominio("black"));

        radice.aggiungiCategoriaFiglia(figlia1);
        figlia1.aggiungiCategoriaFiglia(figlia2);
        figlia1.aggiungiCategoriaFiglia(figlia3);

        FruitoreController controller = new FruitoreController(new FruitoreView(),
                categorieModel,
                null,
                null,
                null,
                null,
                null);

        InputInjector.inject("test\n");
        assertEquals(figlia1, controller.selezionaNuovaMadreLivello(1, radice, radice));
    }

    @Test
    void esploraGerarchie_PassoSceltaNuovoLivelloTest_TornaIndietro() {

        Categoria radice = new Categoria("radiceTest", "tipo");
        Categoria figlia1 = new Categoria("figlia1", "box", radice, new ValoreDominio("test"));
        Categoria figlia2 = new Categoria("figlia2", radice, new ValoreDominio("black"));
        Categoria figlia3 = new Categoria("figlia2", radice, new ValoreDominio("black"));

        radice.aggiungiCategoriaFiglia(figlia1);
        figlia1.aggiungiCategoriaFiglia(figlia2);
        figlia1.aggiungiCategoriaFiglia(figlia3);

        FruitoreController controller = new FruitoreController(new FruitoreView(),
                categorieModel,
                null,
                null,
                null,
                null,
                null);

        InputInjector.inject("test\n");
        assertEquals(radice, controller.selezionaNuovaMadreLivello(2, radice, radice));
        assertEquals(radice, controller.selezionaNuovaMadreLivello(2, figlia1, radice));
    }

}
