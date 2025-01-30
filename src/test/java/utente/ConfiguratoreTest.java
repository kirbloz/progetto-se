package utente;

import it.unibs.projectIngesoft.controller.AccessoController;
import it.unibs.projectIngesoft.controller.UtentiController;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfiguratoreTest {

    private UtentiModel model;
    private Configuratore configuratore;
    private UtentiMapper reader;

    @BeforeEach
    void prepareTest(){
        this.reader = new UtentiMapper("usersTest.json",
                "defaultCredentials.json",
                new SerializerJSON<>(),
                new SerializerJSON<>());
        this.configuratore = new Configuratore("admin", "pwd");
        this.model = new UtentiModel(this.reader);
    }

    @Test
    void primoAccessoConfiguratore() {
        AccessoController accessoController = new AccessoController(model);
        String simulatedInput = "admin\n1234\n";
        InputInjector.inject(simulatedInput);

        Configuratore utenteAttivo = (Configuratore) accessoController.login();
        assert utenteAttivo.firstAccess();
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
