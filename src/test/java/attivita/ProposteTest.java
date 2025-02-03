package attivita;

import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputInjector;
import it.unibs.projectIngesoft.mappers.ProposteMapper;
import it.unibs.projectIngesoft.model.ProposteModel;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Fruitore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProposteTest {

    private ProposteModel model;

    private ProposteMapper mapper;
    private Map<String, List<Proposta>> cleanTestData;


    @BeforeEach
    void prepareTest() {
        mapper = new ProposteMapper("proposteTest.json",
                new SerializerJSON<Map<String, List<Proposta>>>()
        );

        cleanTestData = new HashMap<>();
        cleanTestData = mapper.read();

        // creare i test con configuratore E quelli con utenteAttivo
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        this.model = new ProposteModel(utenteAttivo, mapper);
    }

    @AfterEach
    void tearDown() {
        mapper.write(new HashMap<>(cleanTestData));
    }

    @Test
    void aggiungiProposta(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        model.addProposta(P);
        assert model.getListaProposteComprensorio(utenteAttivo.getComprensorioDiAppartenenza()).contains(P);
    }

    @Test
    void cambiaStatoProposta(){
        assert false;
    }


    @Test
    void controllaProposteSoddisfatte_Due(){
        assert false;
    }

    @Test
    void controllaProposteSoddisfatte_Tre_A_Catena(){
        assert false;
    }



}
