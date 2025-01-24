package mapper;

import it.unibs.projectIngesoft.parsing.UtentiMapper;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Fruitore;
import it.unibs.projectIngesoft.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class UtentiMapperTest {

    private UtentiMapper mapper;

    @BeforeEach
    void prepareTest(){
        this.mapper = new UtentiMapper("usersTest.json", "defaultCredentials.json");
        //ObjectMapper objectMapper = new ObjectMapper();
    }

    @Test
    void writeTest() throws IOException {
        // Creazione di una lista di esempio di oggetti Utente
        List<Utente> utenti = new ArrayList<>();
                utenti.add(new Configuratore("tizianoFerro", "LoStadio2015"));
                utenti.add(new Configuratore("robertoBenigni", "LaVitaèBella97"));
                utenti.add(new Fruitore("Gabbayyyy", "wasd1234", "gabriele.baiguini@gmail.com", "Bergamo"));
                utenti.add(new Fruitore("martinomaff", "martino123", "martino.bariselli@gmail.com", "Brescia"));
                utenti.add(new Fruitore("WadeGiovanniBaiguini", "fourmis7", "wade.baiguini@gmail.com", "Brescia"));
                utenti.add(new Fruitore("LucaMaff", "luca1234", "luca.barismaff@gmail.com", "Brescia"));
                utenti.add(new Fruitore("GiorgioVanni", "gormiti1", "giorgio.baigua@tiscali.it", "Bergamo"));

        // Serializzazione in formato JSON
        //objectMapper.writerFor(type).writeValue(new File("utentiTest.json"), utenti);
        //String jsonString = objectMapper.writerFor(type).writeValueAsString(utenti);
        mapper.write(utenti);
        assert true;
        //assert jsonString != null;
    }

    @Test
    void readTest() throws IOException {
        List<Utente> utenti = mapper.read();

        assert utenti != null;
        assert utenti.get(0).getUsername().equals("tizianoFerro");
    }



    @Test
    void readDefaultTest() {
        //Configuratore deff = new Configuratore("admin", "1234");
        //mapper.write(deff);

        //List<Utente> utenti = mapper.read();
        Utente def = mapper.readDefaultUtente();
        assert def.getUsername().equals("admin");
    }
}