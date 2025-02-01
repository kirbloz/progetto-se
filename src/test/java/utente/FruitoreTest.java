package utente;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.model.UtentiModel;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.mappers.UtentiMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class FruitoreTest {

    private ComprensorioGeografico comprensorio;
    private Utente fruitore;
    private UtentiModel model;

    private UtentiMapper mapper;
    private List<Utente> cleanTestData;


    @BeforeEach
    void prepareTest() {
        mapper = new UtentiMapper("usersTest.json",
                "defaultCredentials.json",
                new SerializerJSON<List<Utente>>(),
                new SerializerJSON<Utente>()
        );

        cleanTestData = new ArrayList<>();
        cleanTestData = mapper.read();

        this.model = new UtentiModel(mapper);

        this.comprensorio = new ComprensorioGeografico("comprensorio", List.of("comune1"));
        this.fruitore = new Fruitore("user", "pwd", "valid@email.address", "comprensorio");
    }


    @AfterEach
    void tearDown() {
        mapper.write(cleanTestData);
    }


    @Test
    void testInputInjectionSystemIN() {

        //String user = InputDatiTerminale.leggiStringa(">> INPUT: ");

        /*InputStream systemIn = System.in;
        PrintStream systemOut = System.out;
        ByteArrayInputStream typeIn;
        ByteArrayOutputStream typeOut;*/

        // set up
        /*typeOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(typeOut));*/

        String simulatedUserInput = "1";

        InputInjector.inject(simulatedUserInput);
        //System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        int result = InputDatiTerminale.leggiIntero("");

        assert result == 1;


    }

    void primoAccessoFruitore_Registrazione() {
        // todo
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml
    }

    @Test
    void cambioCredenzialiFruitore_UsernameEPassword() throws Exception {

        model.addUtente(fruitore);
        //InputInjector.inject("fruitore\npwd1\n");
        model.cambioCredenziali(fruitore, "fruitore", "pwd1");

        assert fruitore.getUsername().equals("fruitore")
                && fruitore.getPassword().equals("pwd1");
    }

    @Test
    void cambioCredenzialiFruitore_SoloPassword() {
        model.addUtente(fruitore);
        //InputInjector.inject(fruitore.getUsername() + "\npwd1\n");
        model.cambioCredenziali(fruitore, fruitore.getUsername(), "pwd1");

        assert fruitore.getPassword().equals("pwd1");
    }

    @Test
    void cambioCredenzialiFruitore_SoloUsername() {
        fruitore.cambioCredenziali("newUser", "pwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("pwd");
    }


    @Test
    void cambioCredenzialiFruitore_SoloUsername_EsisteGia() {

        String newUsername = "tizianoFerro";

        assert model.existsUsername(newUsername);


        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml

        /*Fruitore fruitoreDue = new Fruitore("user2", "pwd", "valid2@email.address", "comprensorio");
        fruitore.cambioCredenziali("newUser", "pwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("pwd");*/
    }




}