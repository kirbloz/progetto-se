package it.unibs.projectIngesoft.XMLparsing;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.main.IIOList;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;

public class UtentiMapper implements IIOList<Utente> {

    private final String filePath;
    private final String defaultCredentialsFilePath;
    private List<Utente> utenti;

    public UtentiMapper(String filePath, String defaultCredentialsFilePath) {
        this.filePath = filePath;
        this.defaultCredentialsFilePath = defaultCredentialsFilePath;
        //readList(); o set ListaUtenti
    }

    /*@Override
    public void write(List<?> list) {
        assert list != null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, list);
    }

    @Override
    public List<Utente> read() {
        assert this.filePath != null;
        assert this.defaultCredentialsFilePath != null;

        List<Utente> listaUtenti = Serializer.deserialize(new TypeReference<>() {
        }, this.filePath);
        //utenti.clear();


        //valutare se sollevare un eccezione/non tollerare la lettura di file vuoti
        if (listaUtenti == null)
            return new ArrayList<>();
        else
            return listaUtenti;


    }*/


    /**
     * Sfrutto l'implementazione statica della classe Serializer per implementare l'interfaccia IIOList.
     */
    @Override
    public void write(List<Utente> utenti) {
        assert utenti != null;
        assert this.filePath != null;
        Serializer.serialize(this.filePath, utenti);
    }

    public void setListaUtenti(List<Utente> list){
        this.utenti = list;
    }

    /**
     * Sfrutto l'implementazione statica della classe Serializer per implementare l'interfaccia IIOList.
     */
    @Override
    public List<Utente> read() {
        assert this.filePath != null;
        assert this.defaultCredentialsFilePath != null;

        List<Utente> listaUtenti = Serializer.deserialize(new TypeReference<>() {
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
        Utente defaultUtente = Serializer.deserialize(new TypeReference<>() {
        }, this.defaultCredentialsFilePath);

        //assert utenti != null : "deve essere almeno inizializzato!";
        assert defaultUtente != null : "l'utente default non può essere null, deve esistere";

        return defaultUtente;
    }


}
