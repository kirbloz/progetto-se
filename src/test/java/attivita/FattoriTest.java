package attivita;

import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.core.domain.model.FattoriModel;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.CategorieRepository;
import it.unibs.projectIngesoft.persistence.implementations.FattoriDiConversioneRepository;
import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FattoriTest {

    private FattoriModel fattoriModel;
    private Repository<List<Categoria>> repositoryCategorie;
    private List<Categoria> cleanData;

    @BeforeEach
    void prepareTest() {
        fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("mock.json", new JsonSerializerFactory().createSerializer()));

        repositoryCategorie = new CategorieRepository("mock.json",
                new JsonSerializerFactory().createSerializer()
        );
        saveData();
        fattoriModel.setHashMapFattori(new HashMap<>());
    }

    void saveData(){
        cleanData = new ArrayList<>();
        cleanData = repositoryCategorie.load();
    }

    @AfterEach
    void tearDown() {
        repositoryCategorie.save(cleanData);
        fattoriModel.setHashMapFattori(new HashMap<>());
    }

    @Test
    void calcolaEInserisciFattoriDiConversioneTest(){
        fattoriModel.setHashMapFattori(new HashMap<>());
        fattoriModel.inserisciSingolaFogliaNellaHashmap("fogliaEsterna", List.of(new Categoria("fogliaEsterna")));

        List<FattoreDiConversione> listaFdC = new ArrayList<>();
        listaFdC.add(new FattoreDiConversione("radice:fogliaInterna", "radice:fogliaInterna1", 2.0));

        fattoriModel.calcolaEInserisciFattoriDiConversione(
                "fogliaEsterna:fogliaEsterna",
                "radice:fogliaInterna",
                1.0,
                listaFdC
        );

        assertTrue(fattoriModel.esisteCategoria("radice:fogliaInterna"));
        assertTrue(fattoriModel.esisteCategoria("radice:fogliaInterna1"));
        assertEquals(2, fattoriModel.getFattoriFromFoglia("radice:fogliaInterna").size());
    }

    @Test
    void calcolaInversiTest(){
        List<FattoreDiConversione> listFattori = new ArrayList<>();
        FattoreDiConversione F1 = new FattoreDiConversione("radice:cat1", "radice:cat2", 1.5);

        listFattori.add(F1);
        listFattori.addAll(fattoriModel.calcolaInversi(listFattori));
        fattoriModel.aggiungiListDiFattori(listFattori);

        assertTrue(fattoriModel.esisteCategoria("radice:cat1"));
        assertTrue(fattoriModel.esisteCategoria("radice:cat2"));
        assertEquals(1.5, fattoriModel.getFattoriFromFoglia("radice:cat1").getFirst().getFattore());
        assertNotNull(fattoriModel.getFattoriFromFoglia("radice:cat2"));
    }

    @Test
    void aggiungiListDiFattoriTest(){
        List<FattoreDiConversione> listFattori = new ArrayList<>();
        FattoreDiConversione F1 = new FattoreDiConversione("radice:cat1", "radice:cat2", 1.5);
        FattoreDiConversione F2 = new FattoreDiConversione("radice:cat2", "radice:cat3", 0.9);
        listFattori.add(F1);
        listFattori.add(F2);
        fattoriModel.aggiungiListDiFattori(listFattori);

        assertTrue(fattoriModel.esisteCategoria("radice:cat1"));
        assertTrue(fattoriModel.esisteCategoria("radice:cat2"));
        assertEquals(1, fattoriModel.getFattoriFromFoglia("radice:cat1").size());
        assertEquals(1, fattoriModel.getFattoriFromFoglia("radice:cat2").size());
    }

    @Test
    void inserisciSingolaFogliaNellaHashmap(){
        fattoriModel.setHashMapFattori(new HashMap<>());

        String radice = "radice";
        Categoria c = new Categoria("nome1");
        List<Categoria> listaCategorie = new ArrayList<>();
        listaCategorie.add(c);

        fattoriModel.inserisciSingolaFogliaNellaHashmap(radice,listaCategorie);
        assertTrue(fattoriModel.esisteCategoria(Utilitas.factorNameBuilder("radice", "nome1")));
    }
}