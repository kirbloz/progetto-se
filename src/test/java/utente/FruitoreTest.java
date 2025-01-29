package utente;

import it.unibs.projectIngesoft.attivita.ComprensorioGeografico;
import it.unibs.projectIngesoft.gestori.UtentiModel;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
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

    private final static InputStream systemIn = System.in;
    private ByteArrayInputStream typeIn;

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
        System.setIn(systemIn);
        mapper.write(cleanTestData);
        //System.setOut(systemOut);
    }

    void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
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

        provideInput(simulatedUserInput);
        //System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        int result = InputDatiTerminale.leggiIntero("");

        assert result == 1;

        // tear down
        System.setIn(systemIn);
        //System.setOut(systemOut);


    }

    void primoAccessoFruitore_Registrazione() {
        // todo
        // qui c'è il problema di avere uno stub che simuli il check del "database utenti"
        // si attende di fare il refactoring che astragga il meccanismo di serializzazione xml
    }

    @Test
    void cambioCredenzialiFruitore_UsernameEPassword() throws Exception {

        //check funzione interna
        /*model.addUtente(fruitore);
        if (model.ricercaUtente("user", "pwd") >= 0)
            fruitore.cambioCredenziali("newUser", "newPwd");
        assert fruitore.getUsername().equals("newUser");
        assert fruitore.getPassword().equals("newPwd");
        //check check esterno
        assert null != model.verificaCredenziali(new String[]{"newUser", "newPwd"});*/

        model.addUtente(fruitore);
        //System.out.println(UtentiModel.getListaUtenti());
        provideInput("fruitore\npwd1\n");
        model.cambioCredenziali(fruitore);

        assert fruitore.getUsername().equals("fruitore")
                && fruitore.getPassword().equals("pwd1");
    }

    @Test
    void cambioCredenzialiFruitore_SoloPassword() {
        fruitore.cambioCredenziali("user", "newPwd");
        assert fruitore.getUsername().equals("user");
        assert fruitore.getPassword().equals("newPwd");
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