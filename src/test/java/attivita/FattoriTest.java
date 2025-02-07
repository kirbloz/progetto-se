package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.model.CategorieModel;
import it.unibs.projectIngesoft.core.domain.model.FattoriModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.libraries.Utilitas;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FattoriTest {

    private FattoriModel fattoriModel;
    private Repository<List<Categoria>> repositoryCategorie;
    private List<Categoria> cleanData;

    @BeforeEach
    void prepareTest() {
        fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("fattoriTest.json", new JsonSerializerFactory().createSerializer()));

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
        fattoriModel.aggiungiListDiFattori(listFattori);

        assertTrue(fattoriModel.esisteCategoria("radice:cat1"));
        assertTrue(fattoriModel.esisteCategoria("radice:cat2"));
        assertEquals(1, fattoriModel.getFattoriFromFoglia("radice:cat1"));
        assertEquals(1, fattoriModel.getFattoriFromFoglia("radice:cat2"));
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