package it.unibs.projectIngesoft.persistence.implementations;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.persistence.SerializerBasedRepository;
import it.unibs.projectIngesoft.persistence.serialization.Serializer;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;

import java.util.ArrayList;
import java.util.List;

public class UtentiRepository extends SerializerBasedRepository<List<Utente>> {


    private final String defaultCredentialsFilePath;

    private final Serializer<Utente> utenteSerializer;

    public UtentiRepository(String filePath, String defaultCredentialsFilePath, Serializer<List<Utente>> listUtentiSerializer, Serializer<Utente> utenteSerializer) {
        super(filePath, listUtentiSerializer);
        this.defaultCredentialsFilePath = defaultCredentialsFilePath;
        this.utenteSerializer = utenteSerializer;
    }

    public void save(List<Utente> utenti) {
        if (utenti == null) utenti = new ArrayList<>();
        this.serializer.serialize(this.filePath, utenti);
    }

    public List<Utente> load() {
        assert this.filePath != null;
        List<Utente> data = this.serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        return data == null ? new ArrayList<>() : data;
    }

    public Utente loadDefaultUtente() {
        assert this.defaultCredentialsFilePath != null;
        Utente defaultUtente = utenteSerializer.deserialize(new TypeReference<>() {
        }, this.defaultCredentialsFilePath);
        assert defaultUtente != null : "l'utente default non può essere null, deve esistere";
        return defaultUtente;
    }

}
