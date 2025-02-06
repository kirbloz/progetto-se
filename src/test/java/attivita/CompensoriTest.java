package attivita;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.RepositoryLogic.CompGeoRepository;
import it.unibs.projectIngesoft.model.ComprensorioGeograficoModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CompensoriTest {

    private ComprensorioGeograficoModel model;

    private CompGeoRepository mapper;
    private List<ComprensorioGeografico> cleanTestData;

    @BeforeEach
    void prepareTest() {
        mapper = new CompGeoRepository("comprensoriGeograficiTest.json",
                new SerializerJSON<List<ComprensorioGeografico>>()
        );

        cleanTestData = new ArrayList<>();
        cleanTestData = mapper.load();

        this.model = new ComprensorioGeograficoModel(mapper);

    }

    @AfterEach
    void tearDown() {
        mapper.save(cleanTestData);
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
