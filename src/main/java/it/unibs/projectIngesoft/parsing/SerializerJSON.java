package it.unibs.projectIngesoft.parsing;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Utility per serializzare oggetti su file XML tramite Jackson, libreria maven.
 */
public class SerializerJSON<T> implements JacksonSerializer<T>{

    // TODO tutti i system out diventeranno dei throw exception da far gestire al controller
    @Override
    public void serialize(String filePath, Object obj) {
        try {

            // creazione mapper e oggetto file

            File file = new File(filePath);
            // se il file non esiste, lo si crea
            file.createNewFile();

            JsonFactory factory = JsonFactory.builder()
                    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                    .build();
            ObjectMapper objectMapper = new ObjectMapper(factory);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // ROBA CON IL TYPE - NON DOVREBBE PIU SERVIRE
            //TypeFactory typeFactory = objectMapper.getTypeFactory();
            //JavaType type = typeFactory.constructCollectionType(List.class, listObjClass);
            //JavaType type;

            // Serializzazione in formato JSON
            //objectMapper.writerFor(type).writeValue(file, list);

            // Determina il tipo dell'oggetto
            /*if (obj instanceof Collection<?> collection) {
                if (collection.isEmpty()) {
                    throw new IllegalArgumentException("La collezione è vuota, impossibile determinare il tipo.");
                }
                // Estrai il tipo del primo elemento della collezione
                //Class<?> elementType = collection.iterator().next().getClass();
                //type = typeFactory.constructCollectionType(Collection.class, elementType);
            } else {
                type = typeFactory.constructType(obj.getClass());
            }*/

            // Serializzazione in formato JSON
            objectMapper.writeValue(file, obj);
            //objectMapper.writeValue(System.out, obj); per testing

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
            /*XmlMapper xmlMapper = new XmlMapper();*/
            File file = new File(filePath);

            if (!file.exists() || file.length() == 0) {
                return null;
            }
            //xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            // legge i dati dal file con un mapper
            //data = xmlMapper.readValue(file, type);

            // Configura JsonFactory per abilitare StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION
            JsonFactory factory = JsonFactory.builder()
                    .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                    .build();

            ObjectMapper objectMapper = new ObjectMapper(factory);
            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
            // Prova a deserializzare il contenuto del file JSON
            data = objectMapper.readValue(file, type);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }





}
