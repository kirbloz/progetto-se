package it.unibs.projectIngesoft.libraries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

/**
 * Utility per serializzare oggetti su file XML tramite Jackson, libreria maven.
 */
public class Serializer {

    public static void serialize(String filePath, Object obj) {
        try {

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

            File file = new File(filePath);
            // se il file non esiste, lo si crea
            file.createNewFile();
            xmlMapper.writeValue(file, obj);

        } catch (JsonProcessingException e) {
            System.out.println("Errore di serializzazione: " + e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    public static <T> T deserialize(TypeReference<T> typeref, String filePath) {
        T data;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return null;
            }
            // legge i dati dal file con un mapper
            data = xmlMapper.readValue(file, typeref);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;

    }


}
