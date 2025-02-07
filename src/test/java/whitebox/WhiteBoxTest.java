package whitebox;

import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.core.domain.model.FattoriModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.Repository;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WhiteBoxTest {

    ConfiguratoreController controller;
    private Repository<Map<String, List<FattoreDiConversione>>> fattoriRepository;
    private Map<String, List<FattoreDiConversione>> cleanData;
    private FattoriModel fattoriModel;

    @BeforeEach
    void prepareTest() {
        fattoriRepository = new FattoriDiConversioneRepository("mock.json",
                new JsonSerializerFactory().createSerializer());
        fattoriModel = new FattoriModel(fattoriRepository);
        controller = new ConfiguratoreController(new ConfiguratoreView(),
                null,
                fattoriModel,
                null,
                null, null, null
        );
        saveData();
    }

    void saveData() {
        cleanData = new HashMap<>();
        cleanData = fattoriRepository.load();
    }

    @AfterEach
    void tearDown() {
        fattoriRepository.save(cleanData);
    }

    @Test
    void primoCamminoWhiteBoxTesting_GeneraEMemorizzaNuoviFattori() {
        // 1 2 3,4 9,10,11,12 13 15
        List<FattoreDiConversione> listaFattoriGiaEsistenti = new ArrayList<>();
        FattoreDiConversione fattore1 = new FattoreDiConversione("radice:categoriaUno", "radice:categoriaDue", 2.0);
        listaFattoriGiaEsistenti.add(fattore1);
        listaFattoriGiaEsistenti.addAll(fattoriModel.calcolaInversi(listaFattoriGiaEsistenti));

        fattoriModel.aggiungiListDiFattori(listaFattoriGiaEsistenti);

        Categoria categoria = new Categoria("radice2");
        InputInjector.inject("radice\ncategoriaUno\nradice2\nradice2\n1\n");
        controller.generaEMemorizzaNuoviFattori("radice2", List.of(categoria));

        assertTrue(fattoriModel.esisteCategoria("radice2:radice2"));
        assertEquals(2, fattoriModel.getFattoriFromFoglia("radice2:radice2").size());
        assertEquals(2, fattoriModel.getFattoriFromFoglia("radice:categoriaUno").size());
        assertEquals(2, fattoriModel.getFattoriFromFoglia("radice:categoriaDue").size());
    }

    @Test
    void secondoCamminoWhiteBoxTesting_GeneraEMemorizzaNuoviFattori(){
        // 1 2 3,4 5 6 8 13 15
        List<Categoria> categorie = new ArrayList<>();
        categorie.add(new Categoria("radice"));
        categorie.add(new Categoria("radice2"));

        InputInjector.inject("1.3\n");

        controller.generaEMemorizzaNuoviFattori("radice", categorie);
        double inverso = fattoriModel.calcolaInversi(List.of(fattoriModel.getFattoriFromFoglia("radice:radice").getFirst())).getFirst().getFattore();

        assertTrue(fattoriModel.esisteCategoria("radice:radice"));
        assertTrue(fattoriModel.esisteCategoria("radice:radice2"));
        assertEquals(1, fattoriModel.getFattoriFromFoglia("radice:radice").size());
        assertEquals(1, fattoriModel.getFattoriFromFoglia("radice:radice2").size());
        assertEquals(1.3, fattoriModel.getFattoriFromFoglia("radice:radice").getFirst().getFattore());
        assertEquals(inverso, fattoriModel.getFattoriFromFoglia("radice:radice2").getFirst().getFattore());
    }

    @Test
    void terzoCamminoWhiteBoxTesting_GeneraEMemorizzaNuoviFattori(){
        // 1 2 3,4 5 7 8 13 15

        List<Categoria> categorie = new ArrayList<>();
        categorie.add(new Categoria("radice"));
        controller.generaEMemorizzaNuoviFattori("radice", categorie);

        assertTrue(fattoriModel.esisteCategoria("radice:radice"));
        assertEquals(0, fattoriModel.getFattoriFromFoglia("radice:radice").size());
    }

    @Test
    void quartoCamminoWhiteBoxTesting_GeneraEMemorizzaNuoviFattori(){
        // 1 2 14 15
        List<FattoreDiConversione> listaFattoriGiaEsistenti = new ArrayList<>();
        FattoreDiConversione fattore1 = new FattoreDiConversione("radice:categoriaUno", "radice:categoriaDue", 2.0);
        FattoreDiConversione fattore2 = fattoriModel.generaInverso(fattore1);
        listaFattoriGiaEsistenti.add(fattore1);
        listaFattoriGiaEsistenti.add(fattore2);

        fattoriModel.aggiungiListDiFattori(listaFattoriGiaEsistenti);

        controller.generaEMemorizzaNuoviFattori("radice2", new ArrayList<>());

        assertEquals(fattore1.getNome_c1(), fattoriModel.getFattoriFromFoglia("radice:categoriaUno").getFirst().getNome_c1());
        assertEquals(fattore1.getNome_c2(), fattoriModel.getFattoriFromFoglia("radice:categoriaUno").getFirst().getNome_c2());
        assertEquals(fattore1.getFattore(), fattoriModel.getFattoriFromFoglia("radice:categoriaUno").getFirst().getFattore());
        assertEquals(fattore2.getNome_c1(), fattoriModel.getFattoriFromFoglia("radice:categoriaDue").getFirst().getNome_c1());
        assertEquals(fattore2.getNome_c2(), fattoriModel.getFattoriFromFoglia("radice:categoriaDue").getFirst().getNome_c2());
        assertEquals(fattore2.getFattore(), fattoriModel.getFattoriFromFoglia("radice:categoriaDue").getFirst().getFattore());
    }


}
