package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.model.CategorieModel;
import it.unibs.projectIngesoft.core.domain.model.FattoriModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.CategorieRepository;
import it.unibs.projectIngesoft.persistence.implementations.FattoriDiConversioneRepository;
import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;
import it.unibs.projectIngesoft.persistence.serialization.SerializerJSON;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FattoriTest {


    private CategorieModel categorieModel;

    private Repository<List<Categoria>> repositoryCategorie;
    private List<Categoria> cleanData;

    @BeforeEach
    void prepareTest() {
        repositoryCategorie = new CategorieRepository("categorieTest.json",
                new JsonSerializerFactory().createSerializer()
        );
        saveData();
    }

    void saveData(){
        cleanData = new ArrayList<>();
        cleanData = repositoryCategorie.load();
    }

    @AfterEach
    void tearDown() {
        repositoryCategorie.save(cleanData);
    }

    @Test
    void generaFattori_HashmapVuota_UnaNuova() {
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

        controller.generaEMemorizzaNuoviFattori("radiceTest", radiceFoglia.getFoglie());
    }
}