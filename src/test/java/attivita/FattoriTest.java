package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.FattoreDiConversione;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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

    /*
      fattoriModel.calcolaEInserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneTraEsternaEInterna, nuoviFattoriTraTutteLeFoglieDellaNuovaRadice);
        }
            nuoviFattoriTraTutteLeFoglieDellaNuovaRadice.addAll(fattoriModel.calcolaInversi(nuoviFattoriTraTutteLeFoglieDellaNuovaRadice));

            fattoriModel.aggiungiListDiFattori(nuoviFattoriTraTutteLeFoglieDellaNuovaRadice);

            fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
     */

    @Test
    void calcolaEInserisciFattoriDiConversioneTest(){

    }

    @Test
    void calcolaInversiTest(){
        Repository<Map<String, List<FattoreDiConversione>>> fattoriRepository;
        fattoriRepository = new FattoriDiConversioneRepository("mock.json",
                new JsonSerializerFactory().createSerializer());
        FattoriModel fattoriModel = new FattoriModel(fattoriRepository);

        List<FattoreDiConversione> listFattori = new ArrayList<>();
        FattoreDiConversione F1 = new FattoreDiConversione("radice:cat1", "radice:cat2", 1.5);
        listFattori.add(F1);
        listFattori.addAll(fattoriModel.calcolaInversi(listFattori));
        fattoriModel.aggiungiArrayListDiFattori(listFattori);

        assertTrue(fattoriModel.esisteCategoria("radice:cat1"));
        assertTrue(fattoriModel.esisteCategoria("radice:cat2"));
        assertEquals(1.5, fattoriModel.getFattoriFromFoglia("radice:cat1").getFirst().getFattore());
        assertNotNull(fattoriModel.getFattoriFromFoglia("radice:cat2"));
    }

    @Test
    void aggiungiListDiFattoriTest(){

    }

    @Test
    void inserisciSingolaFogliaNellaHashmap(){

    }
}