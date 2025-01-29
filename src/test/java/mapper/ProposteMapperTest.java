package mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.model.ProposteModel;
import it.unibs.projectIngesoft.mappers.ProposteMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.parsing.SerializerXML;
import it.unibs.projectIngesoft.utente.Configuratore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ProposteMapperTest {
    private ProposteMapper mapper;
    private ProposteModel model;
    boolean xml;

    @BeforeEach
    void prepareTest() {

        xml = false;

        if (!xml) {
            this.mapper = new ProposteMapper("proposte.json",
                    new SerializerJSON<Map<String, List<Proposta>>>());
        } else {
            this.mapper = new ProposteMapper("proposte.xml",
                    new SerializerXML<Map<String, List<Proposta>>>());
        }

        model = new ProposteModel(new Configuratore("pippo", "pluto"),
                mapper);

        System.out.println(model.getHashListaProposte());
    }

    @Test
    void writeTest() throws JsonProcessingException {
        if (xml)
            return;

        /*ProposteModel model = new ProposteModel("proposte.json",
                "fattori.json",
                new Configuratore("admin", "pwd"));

        model.addProposta(new Proposta("richiesta",
                "offerta",
                1,
                1,
                new Fruitore("fruitore",
                        "aaa",
                        "email@valid.com",
                        "comprensorio")
        ));

        model.addProposta(
                new Proposta("offerta",
                        "richiesta",
                        1,
                        1,
                        new Fruitore("fruitoreDue", "aaa", "email@valid.com", "comprensorio"))
        );
        model.addProposta(
                new Proposta("offerta2",
                        "richiesta2",
                        1,
                        1,
                        new Fruitore("fruitoreTre", "aaa", "email@valid.com", "comprensorioDue"))
        );

        mapper.write(model.getHashListaProposte());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String jacksonData = objectMapper.writeValueAsString(model.getHashListaProposte());
        System.out.println(jacksonData);*/
    }

    @Test
    void readTest() {
        Map<String, List<Proposta>> hashListaProposte;
        hashListaProposte = mapper.read();
        //System.out.println(hashListaProposte);
        assert model.getHashListaProposte() != null;


    }

}
