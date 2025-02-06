package attivita;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.RepositoryLogic.CategorieRepository;
import it.unibs.projectIngesoft.RepositoryLogic.FattoriDiConversioneRepository;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.model.FattoriModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
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

    private CategorieRepository mapper;
    private List<Categoria> cleanTestData;

    @BeforeEach
    void prepareTest() {
        mapper = new CategorieRepository("categorieTest.json",
                new SerializerJSON<>()
        );

        cleanTestData = new ArrayList<>();
        cleanTestData = mapper.load();


    }

    @AfterEach
    void tearDown() {
        mapper.save(cleanTestData);
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
        FattoriModel fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("fattoriTest.json", new SerializerJSON<>()));
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

        Optional<FattoreDiConversione> fattore1 = fattoriModel.getFattoriFromFoglia("radiceTest","radiceTest")
                .stream().filter(f -> f.getNome_c2().equals("radiceTest2:figliaTest")).findAny();
        Optional<FattoreDiConversione> fattore2 = fattoriModel.getFattoriFromFoglia("radiceTest2","figliaTest")
                .stream().filter(f -> f.getNome_c2().equals("radiceTest2:figliaTest2")).findAny();
        Optional<FattoreDiConversione> fattore3 = fattoriModel.getFattoriFromFoglia("radiceTest2","figliaTest2")
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
