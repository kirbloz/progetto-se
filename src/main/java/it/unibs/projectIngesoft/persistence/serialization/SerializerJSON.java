package it.unibs.projectIngesoft.persistence.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Utility per serializzare oggetti su file XML tramite Jackson, libreria maven.
 */
public class SerializerJSON<T> implements Serializer<T> {

    @Override
    public void serialize(String filePath, Object obj) {
        try {
            File file = new File(filePath);
            file.createNewFile();

            JsonFactory factory = JsonFactory.builder()
                    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                    .build();
            ObjectMapper objectMapper = new ObjectMapper(factory);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            objectMapper.writeValue(file, obj);
        } catch (JsonProcessingException e) {
            System.out.println("Errore di serializzazione: " + e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public T deserialize(TypeReference<T> type, String filePath) {
        T data;
        try {
            File file = new File(filePath);

            if (!file.exists() || file.length() == 0) {
                return null;
            }

            JsonFactory factory = JsonFactory.builder()
                    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper(factory);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            data = objectMapper.readValue(file, type);

        } catch (JsonMappingException e){
            return null;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }





}
