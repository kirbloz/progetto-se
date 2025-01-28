package mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.gestori.FattoriModel;
import it.unibs.projectIngesoft.mappers.FattoriMapper;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.parsing.SerializerXML;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FattoriMapperTest {

    FattoriModel model;
    FattoriMapper mapper;

    @BeforeEach
    void prepareTests(){
        this.mapper = new FattoriMapper("fattoriTest.json",
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

    @Test
    void readXML_andWriteJSON(){
        SerializerXML<FattoriWrapper> serializer = new SerializerXML();
        FattoriWrapper readData = serializer.deserialize(new TypeReference<>(){} , "fattori.xml");
        Map<String, List<FattoreDiConversione>> hashmpa = readData.toHashMap();
        System.out.println(hashmpa);
        for(String cat : readData.toHashMap().keySet()){
            System.out.println(cat);
            System.out.println(hashmpa.get(cat));
        }
        assert hashmpa != null && !hashmpa.isEmpty();


        mapper.write(hashmpa);
    }



}
