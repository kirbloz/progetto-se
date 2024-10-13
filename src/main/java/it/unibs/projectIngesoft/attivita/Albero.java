package it.unibs.projectIngesoft.attivita;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe creata per facilitare la serializzazione XML dei dati relativi alle categorie gestite da GestoreCategorie.
 */
@JacksonXmlRootElement(localName = "Albero")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("Albero")
public class Albero {

    /*
     * Queste annotazioni permettono di evitare la serializzazione come "<ArrayList><item>.." che poi causa
     * problemi durante la de-serializzazione.
     */
    @JacksonXmlProperty(localName = "radice")
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<CategoriaNonFoglia> radici;

    public Albero() {
        this.radici = new ArrayList<>();
    }
}
