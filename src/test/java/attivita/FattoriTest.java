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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void calcolaEInserisciFattoriDiConversioneTest(){
        FattoriModel fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("fattoriTest.json", new JsonSerializerFactory().createSerializer()));


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

    }

    @Test
    void aggiungiListDiFattoriTest(){

    }

    @Test
    void inserisciSingolaFogliaNellaHashmap(){
        FattoriModel fattoriModel = new FattoriModel(new FattoriDiConversioneRepository("fattoriTest.json", new JsonSerializerFactory().createSerializer()));


        fattoriModel.setHashMapFattori(new HashMap<>());

        String radice = "radice";
        Categoria c = new Categoria("nome1");
        List<Categoria> listaCategorie = new ArrayList<>();
        listaCategorie.add(c);

        fattoriModel.inserisciSingolaFogliaNellaHashmap(radice,listaCategorie);


        assert(fattoriModel.esisteCategoria(Utilitas.factorNameBuilder("radice", "nome1")));
    }
}