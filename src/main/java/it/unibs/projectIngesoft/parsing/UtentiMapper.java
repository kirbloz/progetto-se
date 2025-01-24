package it.unibs.projectIngesoft.parsing;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.main.IIOList;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;

public class UtentiMapper implements IIOList<Utente> {

    private final String filePath;
    private final String defaultCredentialsFilePath;

    public UtentiMapper(String filePath, String defaultCredentialsFilePath) {
        this.filePath = filePath;
        this.defaultCredentialsFilePath = defaultCredentialsFilePath;
        //readList(); o set ListaUtenti
    }

    /**
     * Sfrutto l'implementazione statica della classe Serializer per implementare l'interfaccia IIOList.
     */
    @Override
    public void write(List<Utente> utenti) {
        assert utenti != null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, utenti);
    }

    // DA RIMUOVERE!
    /*public void write(Utente defaultUtente) {
        assert defaultUtente != null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, defaultUtente);

    }*/

    /**
     * Sfrutto l'implementazione statica della classe Serializer per implementare l'interfaccia IIOList.
     */
    @Override
    public List<Utente> read() {
        assert this.filePath != null;
        assert this.defaultCredentialsFilePath != null;

        List<Utente> listaUtenti = Serializer.deserialize(new TypeReference<List<Utente>>() {
        }, this.filePath);
        //utenti.clear();


        //valutare se sollevare un eccezione/non tollerare la lettura di file vuoti
        if (listaUtenti == null)
            return new ArrayList<>();
        else
            return listaUtenti;
    }

    public Utente readDefaultUtente() {
        assert this.defaultCredentialsFilePath != null;
        Utente defaultUtente = Serializer.deserialize(new TypeReference<Configuratore>() {
        }, this.defaultCredentialsFilePath);

        //assert utenti != null : "deve essere almeno inizializzato!";
        assert defaultUtente != null : "l'utente default non può essere null, deve esistere";

        return defaultUtente;
    }


}
