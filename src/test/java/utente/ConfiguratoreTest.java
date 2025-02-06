package utente;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfiguratoreTest {

    private UtentiModel utentiModel;

    private Configuratore configuratoreTest;

    @BeforeEach
    void prepareTest() {
        UtentiMapper mapper = new UtentiMapper("usersJSONTEST.json",
                "defaultCredentials.json",
                new SerializerJSON<>(),
                new SerializerJSON<>());

        this.configuratoreTest = new Configuratore("admin", "pwd");

        this.utentiModel = new UtentiModel(mapper);
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
