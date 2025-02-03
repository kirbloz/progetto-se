package it.unibs.projectIngesoft.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.parsing.JacksonSerializer;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.List;

public class UtentiMapper implements Mapper<List<Utente>> {

    private final String filePath;
    private final String defaultCredentialsFilePath;

    private final JacksonSerializer<List<Utente>> listUtentiSerializer;
    private final JacksonSerializer<Utente> utenteSerializer;

    public UtentiMapper(String filePath, String defaultCredentialsFilePath, JacksonSerializer<List<Utente>> listUtentiSerializer, JacksonSerializer<Utente> utenteSerializer) {
        this.filePath = filePath;
        this.defaultCredentialsFilePath = defaultCredentialsFilePath;
        this.listUtentiSerializer = listUtentiSerializer;
        this.utenteSerializer = utenteSerializer;
    }

    public void write(List<Utente> utenti) {
        assert utenti != null;
        assert this.filePath != null;
        this.listUtentiSerializer.serialize(this.filePath, utenti);
    }

    public List<Utente> read() {
        assert this.filePath != null;
        assert this.defaultCredentialsFilePath != null;

        return this.listUtentiSerializer.deserialize(new TypeReference<>() {
        }, this.filePath);
    }

    public Utente readDefaultUtente() {
        assert this.defaultCredentialsFilePath != null;
        Utente defaultUtente = utenteSerializer.deserialize(new TypeReference<>() {
        }, this.defaultCredentialsFilePath);
        assert defaultUtente != null : "l'utente default non può essere null, deve esistere";
        return defaultUtente;
    }

}
