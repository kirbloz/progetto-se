package utente;

import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.utente.Configuratore;
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


    void primoAccessoConfiguratore() {
        //todo
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml
        // ok apposto la lettura ma serve l'input dati -> per ora è da terminale
    }

    @Test
    void testStoCazzoMiUccido() throws Exception {
        //System.out.println(reader.read());
        assert null != this.model.verificaCredenziali(new String[]{"tizianoFerro", "LoStadio2015"});
        model.addUtente(this.configuratore);
        assert null != this.model.verificaCredenziali(new String[]{"admin", "pwd"});

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
