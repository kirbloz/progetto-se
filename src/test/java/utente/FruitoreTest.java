package utente;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.gestori.UtentiModel;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.parsing.SerializerXML;
import it.unibs.projectIngesoft.utente.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FruitoreTest {

    private ComprensorioGeografico comprensorio;
    private Utente fruitore;
    private UtentiModel model;

    @BeforeEach
    void prepareTest(){

        UtentiMapper mapper = new UtentiMapper("users.json",
                "defaultCredentials.json",
                new SerializerJSON<List<Utente>>(),
                new SerializerJSON<Utente>()
                );
        this.model = new UtentiModel(mapper);

        this.comprensorio = new ComprensorioGeografico("comprensorio", List.of("comune1"));
        this.fruitore = new Fruitore("user", "pwd", "valid@email.address", "comprensorio");
    }

    //@AfterEach
    @Test
    void cleanTest(){

           /*String input = "5";
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);  // Redirect System.in to use our simulated input
            // Assert that the square of 5 is 25
            assertEquals(5, Integer.parseInt(input));

*/
        System.setIn(System.in);
    }

    void primoAccessoFruitore_Registrazione(){
        // todo
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml
    }

    @Test
    void cambioCredenzialiFruitore_UsernameEPassword() throws Exception {

        //check funzione interna
        model.addUtente(fruitore);
        if(model.ricercaUtente("user", "pwd") >= 0)
            fruitore.cambioCredenziali("newUser", "newPwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("newPwd");
        //check check esterno
        assert null != model.verificaCredenziali(new String[]{"newUser", "newPwd"});
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

        String newUsername="tizianoFerro";

        assert model.existsUsername(newUsername);


        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml

        /*Fruitore fruitoreDue = new Fruitore("user2", "pwd", "valid2@email.address", "comprensorio");
        fruitore.cambioCredenziali("newUser", "pwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("pwd");*/
    }




}