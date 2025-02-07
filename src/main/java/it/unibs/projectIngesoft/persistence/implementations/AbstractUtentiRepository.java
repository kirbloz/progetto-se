package it.unibs.projectIngesoft.persistence.implementations;

import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;
import it.unibs.projectIngesoft.persistence.SerializerBasedRepository;
import it.unibs.projectIngesoft.persistence.serialization.Serializer;

import java.util.List;

public abstract class AbstractUtentiRepository extends SerializerBasedRepository<List<Utente>> {

    protected AbstractUtentiRepository(String filePath, Serializer<List<Utente>> serializer) {
        super(filePath, serializer);
    }

    public abstract Utente loadDefaultUtente();
}
