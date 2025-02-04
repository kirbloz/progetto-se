package utente;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfiguratoreTest {

    private UtentiModel model;
    private Configuratore configuratore;

    @BeforeEach
    void prepareTest(){
        UtentiMapper mapper = new UtentiMapper("usersJSONTEST.json",
                "defaultCredentials.json",
                new SerializerJSON<>(),
                new SerializerJSON<>());
        this.configuratore = new Configuratore("admin", "pwd");
        this.model = new UtentiModel(mapper);
    }

    @Test
    void primoAccessoConfiguratore() {
        AccessoController accessoController = new AccessoController(model, null);
        String simulatedInput = "admin\n1234\n";
        InputInjector.inject(simulatedInput);

        Configuratore utenteAttivo = (Configuratore) accessoController.login();
        assert utenteAttivo.isFirstAccess();
    }

    @Test
    void primoAccessoConfiguratore_E_CambioPassword() {
        Configuratore utenteAttivo = new Configuratore("admin", "1234");
        utenteAttivo.setFirstAccess(true);

        ConfiguratoreController controller = new ConfiguratoreController(
        new ConfiguratoreView(),
        null,
        null,
        null,
                null,
                model,
                utenteAttivo
        );
        InputInjector.inject("unique\npassword\n0");
        controller.run();

        assert !utenteAttivo.isFirstAccess();
    }

    @Test
    void cambioCredenzialiConfiguratore_UsernameEPassword() {
        configuratore.cambioCredenziali("newAdmin", "newPwd");
        assert configuratore.getUsername().equals("newAdmin");
        assert configuratore.getPassword().equals("newPwd");
    }


    @Test
    void cambioCredenzialiConfiguratore_SoloPassword(){
        configuratore.cambioCredenziali("admin", "newPwd");
        assert configuratore.getUsername().equals("admin");
        assert configuratore.getPassword().equals("newPwd");
    }

    @Test
    void cambioCredenzialiConfiguratore_SoloUsername(){
        configuratore.cambioCredenziali("newAdmin", "pwd");
        assert configuratore.getUsername().equals("newAdmin");
        assert configuratore.getPassword().equals("pwd");
    }


}
