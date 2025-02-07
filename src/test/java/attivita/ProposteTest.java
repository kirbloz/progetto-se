package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.core.domain.entities.StatiProposta;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.core.domain.model.ProposteModel;
import it.unibs.projectIngesoft.persistence.implementations.ProposteRepository;
import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;
import it.unibs.projectIngesoft.persistence.serialization.SerializerJSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProposteTest {

    private ProposteModel model;

    private ProposteRepository proposteRepository;
    private Map<String, List<Proposta>> cleanData;


    @BeforeEach
    void prepareTest() {
        proposteRepository = new ProposteRepository("mock.json",
                new JsonSerializerFactory().createSerializer()
        );
        saveData();

        this.model = new ProposteModel(proposteRepository);
    }

    void saveData(){
        cleanData = new HashMap<>();
        cleanData = proposteRepository.load();
    }

    @AfterEach
    void tearDown() {
        proposteRepository.save(new HashMap<>(cleanData));
    }

    @Test
    void aggiungiProposta(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        model.addProposta(P);
        assertTrue(model.getListaProposteComprensorio(utenteAttivo.getComprensorioDiAppartenenza()).contains(P));
    }

    @Test
    void cambiaStatoPropostaRitirata(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        P.setRitirata();
        assertEquals(StatiProposta.RITIRATA, P.getStato());
    }

    @Test
    void cambiaStatoPropostaChiusa(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        P.setChiusa();
        assertEquals(StatiProposta.CHIUSA, P.getStato());
    }


    @Test
    void controllaProposteSoddisfatte_Due(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P1 = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        Proposta P2 = new Proposta("Fisica", "Matematica", 10, 10, utenteAttivo);
        model.addProposta(P1);
        model.addProposta(P2);

        model.cercaProposteDaChiudere(P1);

        assertEquals(StatiProposta.CHIUSA, P1.getStato());
        assertEquals(StatiProposta.CHIUSA, P2.getStato());
    }

    @Test
    void controllaProposteSoddisfatte_Tre_A_Catena(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P1 = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        Proposta P2 = new Proposta("Fisica", "Geografia", 10, 10, utenteAttivo);
        Proposta P3 = new Proposta("Geografia", "Matematica", 10, 10, utenteAttivo);
        model.addProposta(P1);
        model.addProposta(P2);
        model.addProposta(P3);

        model.cercaProposteDaChiudere(P1);

        assertEquals(StatiProposta.CHIUSA, P1.getStato());
        assertEquals(StatiProposta.CHIUSA, P2.getStato());
        assertEquals(StatiProposta.CHIUSA, P3.getStato());
    }
}
