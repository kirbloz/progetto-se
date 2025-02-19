package utente;

import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;
import it.unibs.projectIngesoft.core.domain.model.ComprensorioGeograficoModel;
import it.unibs.projectIngesoft.core.domain.model.UtentiModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.implementations.AbstractUtentiRepository;
import it.unibs.projectIngesoft.persistence.implementations.CompGeoRepository;
import it.unibs.projectIngesoft.persistence.implementations.UtentiRepository;
import it.unibs.projectIngesoft.persistence.serialization.SerializerJSON;
import it.unibs.projectIngesoft.presentation.controllers.AccessoController;
import it.unibs.projectIngesoft.presentation.controllers.FruitoreController;
import it.unibs.projectIngesoft.presentation.view.FruitoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FruitoreTest {

    private Fruitore fruitoreTest;
    private UtentiModel utentiModel;

    private AbstractUtentiRepository repository;
    private List<Utente> cleanTestData;


    @BeforeEach
    void prepareTest() {
        repository = new UtentiRepository("mock.json",
                "defaultCredentials.json",
                new SerializerJSON<>(),
                new SerializerJSON<>()
        );

        cleanTestData = new ArrayList<>();
        cleanTestData = repository.load();

        this.utentiModel = new UtentiModel(repository);
        this.fruitoreTest = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");
    }


    @AfterEach
    void tearDown() {
        repository.save(cleanTestData);
    }

    @Test
    void primoAccessoFruitore_Registrazione() {
        ComprensorioGeograficoModel compGeoModel = new ComprensorioGeograficoModel(
                new CompGeoRepository("mockfile.json", new SerializerJSON<>()));
        AccessoController accessoController = new AccessoController(utentiModel, compGeoModel);

        compGeoModel.aggiungiComprensorio("Brescia", List.of(new String[]{"test"}));
        String data = "2\nadmin\n1234\nBrescia\nvalid@email.com\n";
        InputInjector.inject(data);

        Fruitore utenteAttivo = (Fruitore) accessoController.run();
        assertEquals("admin", utenteAttivo.getUsername());
        assertEquals("1234", utenteAttivo.getPassword());
        assertEquals("Brescia", utenteAttivo.getComprensorioDiAppartenenza());
        assertEquals("valid@email.com", utenteAttivo.getEmail());

    }

    @Test
    void cambioCredenzialiFruitore_UsernameEPassword() {

        utentiModel.addUtente(fruitoreTest);
        utentiModel.cambioCredenziali(fruitoreTest, "fruitoreTest", "pwd1");

        assertEquals("fruitoreTest", fruitoreTest.getUsername());
        assertEquals("pwd1", fruitoreTest.getPassword());
    }

    @Test
    void cambioCredenzialiFruitore_SoloPassword() {
        utentiModel.addUtente(fruitoreTest);
        utentiModel.cambioCredenziali(fruitoreTest, fruitoreTest.getUsername(), "pwd1");

        assertEquals("pwd1",fruitoreTest.getPassword());
    }

    @Test
    void cambioCredenzialiFruitore_SoloUsername() {
        fruitoreTest.cambioCredenziali("newUser", "pwd");
        assertEquals("newUser", fruitoreTest.getUsername());
        assertEquals("pwd", fruitoreTest.getPassword());
    }


    @Test
    void cambioCredenzialiUsernameEsisteGia() {

        utentiModel.addUtente(fruitoreTest);
        utentiModel.addUtente(new Fruitore("user2", "pwd", "valid@email.com", "Brescia"));

       String data = "1\nuser2\nuser3\npwd\n0";
        InputInjector.inject(data);

        FruitoreController fruitCont = new FruitoreController(
               new FruitoreView(),
                null, null, null, null, utentiModel, fruitoreTest);

        fruitCont.run();

        assertEquals("user3", fruitoreTest.getUsername());
        assertTrue(utentiModel.existsUsername("user2"));
    }


}