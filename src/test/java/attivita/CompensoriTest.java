package attivita;


import it.unibs.projectIngesoft.core.domain.entities.ComprensorioGeografico;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.model.ComprensorioGeograficoModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.CompGeoRepository;
import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CompensoriTest {

    private ComprensorioGeograficoModel model;

    private Repository<List<ComprensorioGeografico>> repositoryComprensori;
    private List<ComprensorioGeografico> cleanData;

    @BeforeEach
    void prepareTest() {
        repositoryComprensori = new CompGeoRepository("mock.json",
                new JsonSerializerFactory().createSerializer()
        );
        saveData();
        this.model = new ComprensorioGeograficoModel(repositoryComprensori);
    }

    void saveData() {
        cleanData = new ArrayList<>();
        cleanData = repositoryComprensori.load();
    }

    @AfterEach
    void tearDown() {
        repositoryComprensori.save(cleanData);
    }

    @Test
    void aggiuntaComprensorioConComuni() {
        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                null,
                null,
                null,
                model,
                null,
                new Configuratore("default", "test"));

        InputInjector.inject("comprensorioTest\ntest1\nS\ntest2\nN\n");
        controller.aggiungiComprensorio();

        assert model.getListaNomiComprensoriGeografici().contains("comprensorioTest");
        assert model.getStringComuniByComprensorioName("comprensorioTest").contains("test1");
        assert model.getStringComuniByComprensorioName("comprensorioTest").contains("test2");

    }

    @Test
    void aggiuntaComprensorioUnivoco() {
        model.aggiungiComprensorio("comprensorioTest", List.of("test"));
        model.aggiungiComprensorio("comprensorioTest", List.of("test", "test2"));

        List<String> listaComprensori = model.getListaNomiComprensoriGeografici();

        assert listaComprensori.contains("comprensorioTest");

        assert listaComprensori.stream().filter(c -> c.equals("comprensorioTest")).count() == 1;

    }
}
