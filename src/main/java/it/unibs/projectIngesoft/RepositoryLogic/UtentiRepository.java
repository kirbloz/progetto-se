package it.unibs.projectIngesoft.RepositoryLogic;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.parsing.Serializer;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;

public class UtentiRepository implements Repository<List<Utente>> {

    private final String filePath;
    private final String defaultCredentialsFilePath;

    private final Serializer<List<Utente>> listUtentiSerializer;
    private final Serializer<Utente> utenteSerializer;

    public UtentiRepository(String filePath, String defaultCredentialsFilePath, Serializer<List<Utente>> listUtentiSerializer, Serializer<Utente> utenteSerializer) {
        this.filePath = filePath;
        this.defaultCredentialsFilePath = defaultCredentialsFilePath;
        this.listUtentiSerializer = listUtentiSerializer;
        this.utenteSerializer = utenteSerializer;
    }

    public void save(List<Utente> utenti) {
        if (utenti == null) utenti = new ArrayList<>();
        this.listUtentiSerializer.serialize(this.filePath, utenti);
    }

    public List<Utente> load() {
        assert this.filePath != null;
        List<Utente> data = this.listUtentiSerializer.deserialize(new TypeReference<>() {
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
