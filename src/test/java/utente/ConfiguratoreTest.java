package utente;


import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;
import it.unibs.projectIngesoft.core.domain.model.UtentiModel;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.persistence.Repository;
import it.unibs.projectIngesoft.persistence.implementations.AbstractUtentiRepository;
import it.unibs.projectIngesoft.persistence.implementations.UtentiRepository;
import it.unibs.projectIngesoft.persistence.serialization.SerializerJSON;
import it.unibs.projectIngesoft.presentation.controllers.AccessoController;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ConfiguratoreTest {

    private UtentiModel utentiModel;
    private Configuratore configuratoreTest;
    private AbstractUtentiRepository repository;
    private List<Utente> cleanData;

    @BeforeEach
    void prepareTest() {
        repository= new UtentiRepository("mock.json",
                "defaultCredentials.json",
                new SerializerJSON<>(),
                new SerializerJSON<>());

        this.configuratoreTest = new Configuratore("admin", "pwd");
        this.utentiModel = new UtentiModel(repository);
        saveData();
    }

    void saveData(){
        cleanData = new ArrayList<>();
        cleanData = repository.load();
    }

    @AfterEach
    void tearDown() {
        repository.save(new ArrayList<>(cleanData));
    }


    @Test
    void primoAccessoConfiguratore() {
        AccessoController accessoController = new AccessoController(utentiModel, null);
        String data = "admin\n1234\n";
        InputInjector.inject(data);

        Configuratore utenteAttivo = (Configuratore) accessoController.login();
        assert utenteAttivo.isFirstAccess();
    }

    @Test
    void primoAccessoConfiguratore_E_CambioPassword() {
        Configuratore utenteAttivo = new Configuratore("admin", "1234");
        utenteAttivo.setFirstAccess(true);

        ConfiguratoreController controller = new ConfiguratoreController(
                new ConfiguratoreView(), null, null, null, null,
                utentiModel, utenteAttivo
        );

        String data = "unique\npassword\n0";
        InputInjector.inject(data);
        controller.run();

        assert !utenteAttivo.isFirstAccess();
    }

    @Test
    void cambioCredenzialiConfiguratore_UsernameEPassword() {
        configuratoreTest.cambioCredenziali("newAdmin", "newPwd");
        assert configuratoreTest.getUsername().equals("newAdmin");
        assert configuratoreTest.getPassword().equals("newPwd");
    }


    @Test
    void cambioCredenzialiConfiguratore_SoloPassword() {
        configuratoreTest.cambioCredenziali("admin", "newPwd");
        assert configuratoreTest.getUsername().equals("admin");
        assert configuratoreTest.getPassword().equals("newPwd");
    }

    @Test
    void cambioCredenzialiConfiguratore_SoloUsername() {
        configuratoreTest.cambioCredenziali("newAdmin", "pwd");
        assert configuratoreTest.getUsername().equals("newAdmin");
        assert configuratoreTest.getPassword().equals("pwd");
    }


}
