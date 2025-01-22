package utente;

import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfiguratoreTest {

    private Configuratore configuratore;

    @BeforeEach
    void prepareTest(){
        this.configuratore = new Configuratore("admin", "pwd");
    }

    void primoAccessoConfiguratore() {
        //todo
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml
    }

    @Test
    void cambioCredenzialiConfiguratore_UsernameEPassword() {
       // Utente configuratore = new Configuratore("admin", "pwd");
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
