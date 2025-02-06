package attivita;


import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.core.domain.entities.StatiProposta;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.core.domain.model.ProposteModel;
import it.unibs.projectIngesoft.persistence.implementations.ProposteRepository;
import it.unibs.projectIngesoft.persistence.serialization.SerializerJSON;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProposteTest {

    private ProposteModel model;

    private ProposteRepository mapper;
    private Map<String, List<Proposta>> cleanTestData;


    @BeforeEach
    void prepareTest() {
        mapper = new ProposteRepository("proposteTest.json",
                new SerializerJSON<Map<String, List<Proposta>>>()
        );


        cleanTestData = mapper.load();
        if(cleanTestData == null)
            cleanTestData = new HashMap<>();

        // creare i test con configuratore E quelli con utenteAttivo
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        this.model = new ProposteModel(mapper);
    }

    @AfterEach
    void tearDown() {
        mapper.save(new HashMap<>(cleanTestData));
    }

    @Test
    void aggiungiProposta(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        model.addProposta(P);
        assert model.getListaProposteComprensorio(utenteAttivo.getComprensorioDiAppartenenza()).contains(P);
    }

    @Test
    void cambiaStatoPropostaRitirata(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        P.setRitirata();
        assert P.getStato().equals(StatiProposta.RITIRATA);
    }

    @Test
    void cambiaStatoPropostaChiusa(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        P.setChiusa();
        assert P.getStato().equals(StatiProposta.CHIUSA);
    }


    @Test
    void controllaProposteSoddisfatte_Due(){
        Fruitore utenteAttivo = new Fruitore("user", "pwd", "valid@email.com", "comprensorio");

        Proposta P1 = new Proposta("Matematica", "Fisica", 10, 10, utenteAttivo);
        Proposta P2 = new Proposta("Fisica", "Matematica", 10, 10, utenteAttivo);
        model.addProposta(P1);
        model.addProposta(P2);

        model.cercaProposteDaChiudere(P1);

        assert P1.getStato().equals(StatiProposta.CHIUSA) && P2.getStato().equals(StatiProposta.CHIUSA);
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

        assert P1.getStato().equals(StatiProposta.CHIUSA) && P2.getStato().equals(StatiProposta.CHIUSA) && P3.getStato().equals(StatiProposta.CHIUSA);
    }
}
