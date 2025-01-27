package it.unibs.projectIngesoft.XMLparsing;

import com.fasterxml.jackson.core.type.TypeReference;
import it.unibs.projectIngesoft.parsing.Serializer;
import it.unibs.projectIngesoft.utente.Utente;

import java.util.ArrayList;
import java.util.List;

public class XMLMapper {

    private String utentiFilePath;
    private String defaultCredentialsFilePath;


    public void writeList(List<Utente> utenti) {
        assert utenti != null;
        assert this.utentiFilePath != null;
        Serializer.serialize(this.utentiFilePath, utenti);
    }

    /*public void setListaUtenti(List<Utente> list){
        this.utentiFilePath = list;
    }*/

    /**
     * Sfrutto l'implementazione statica della classe Serializer per implementare l'interfaccia IIOList.
     */
    public List<Utente> readList() {
        assert this.utentiFilePath != null;
        assert this.defaultCredentialsFilePath != null;

        List<Utente> listaUtenti = Serializer.deserialize(new TypeReference<>() {
        }, this.utentiFilePath);
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
