package mapper;

import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.gestori.FattoriModel;
import it.unibs.projectIngesoft.mappers.FattoriMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class FattoriMapperTest {

    FattoriModel model;

    @BeforeEach
    void prepareTests(){
        FattoriMapper mapper = new FattoriMapper("fattoriTest.json",
                new SerializerJSON<>());
        model = new FattoriModel(mapper);

    }


    @Test
    void write(){
        List<FattoreDiConversione> tempList = new ArrayList<>();
        tempList.add(new FattoreDiConversione("catUno",
                "catDue",
                1));
        tempList.add(new FattoreDiConversione("catTre",
                "catDue",
                2));

        model.aggiungiArrayListDiFattori(
                tempList);
    }

    @Test
    void read(){
        assert !model.getHashListaFattori().get("catUno").isEmpty();

    }
}
