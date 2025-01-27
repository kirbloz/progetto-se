package it.unibs.projectIngesoft.parsing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class SerializerXML<T> implements JacksonSerializer<T> {

    @Override
    public void serialize(String filePath, T obj) {
        System.out.println("Serializing " + obj.getClass().getSimpleName());
    }

    @Override
    public T deserialize(TypeReference<T> type, String filePath) {
        T data;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(filePath);

            if (!file.exists() || file.length() == 0) {
                return null;
            }
            //xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            // legge i dati dal file con un mapper
            //data = xmlMapper.readValue(file, type);

            // Configura JsonFactory per abilitare StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION
            /*JsonFactory factory = JsonFactory.builder()
                    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper(factory);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);*/
            // Prova a deserializzare il contenuto del file JSON
            //data = objectMapper.readValue(file, type);

           // xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);


            data = xmlMapper.readValue(file, type);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

}
