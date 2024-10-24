package it.unibs.projectIngesoft.libraries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.projectIngesoft.attivita.CategoriaNonFoglia;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Utility per serializzare oggetti su file XML tramite Jackson, libreria maven.
 */
public class Serializer {

    // TODO
    //testato per categorie, funziona
    //fattori?
    //utenti?
    //comprensori?
    public static void serialize(String filePath, Object obj) {
        try {

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            //xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);


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
        T data = null;
        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return null;
            }

            data = xmlMapper.readValue(file, typeref);


        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return data;

    }


}
