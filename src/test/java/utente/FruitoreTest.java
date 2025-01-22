package utente;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.utente.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class FruitoreTest {

    private ComprensorioGeografico comprensorio;
    private Utente fruitore;

    @BeforeEach
    void prepareTest(){
        this.comprensorio = new ComprensorioGeografico("comprensorio", List.of("comune1"));
        this.fruitore = new Fruitore("user", "pwd", "valid@email.address", "comprensorio");
    }

    void primoAccessoFruitore_Registrazione(){
        // todo
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml
    }

    @Test
    void cambioCredenzialiFruitore_UsernameEPassword() {
        fruitore.cambioCredenziali("newUser", "newPwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("newPwd");
    }

    @Test
    void cambioCredenzialiFruitore_SoloPassword(){
        fruitore.cambioCredenziali("user", "newPwd");
        assert fruitore.getUsername().equals("user");
        assert fruitore.getPassword().equals("newPwd");
    }

    @Test
    void cambioCredenzialiFruitore_SoloUsername(){
        fruitore.cambioCredenziali("newUser", "pwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("pwd");
    }


    @Test
    void cambioCredenzialiFruitore_SoloUsername_EsisteGia(){
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml

        /*Fruitore fruitoreDue = new Fruitore("user2", "pwd", "valid2@email.address", "comprensorio");
        fruitore.cambioCredenziali("newUser", "pwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("pwd");*/
    }




}