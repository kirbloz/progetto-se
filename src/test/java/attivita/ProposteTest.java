package attivita;

import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.StatiProposta;
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

        this.model = new ProposteModel(mapper);
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
