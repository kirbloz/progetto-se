package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.CategoriaFoglia;
import it.unibs.projectIngesoft.attivita.CategoriaNonFoglia;
import it.unibs.projectIngesoft.attivita.ValoreDominio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreCategorie {
    // non sono sicuro di quale struttura dati utilizzare
    private Albero tree;

    private final String filePath;


    public GestoreCategorie(String filePath) {
        /* legge da file memorizza le radici già presenti
        prepara tutto apposto */
        this.filePath = filePath;
        this.tree = new Albero();
        //deserializeXML(); // load dati

        boolean debug_data = false;
        if (debug_data) {
            ArrayList<ValoreDominio> valori = new ArrayList<>();
            ArrayList<ValoreDominio> valori2 = new ArrayList<>();
            valori.add(new ValoreDominio("valore1", "desc1"));
            valori.add(new ValoreDominio("valore2", "desc2"));
            valori2.add(new ValoreDominio("valore3", "desc3"));
            CategoriaNonFoglia radice1 = new CategoriaNonFoglia("cat1 radice", "materia", valori);
            CategoriaNonFoglia radice2 = new CategoriaNonFoglia("cat2 radice", "lezione", valori);
            tree.radici.add(radice1);
            tree.radici.add(radice2);
            radice1.addCategoriaFiglia(new CategoriaFoglia("figlia1", radice1, "matematica"));
            radice1.addCategoriaFiglia(new CategoriaFoglia("figlia2", radice1, "italiano"));
            radice2.addCategoriaFiglia(new CategoriaFoglia("figlia3", radice2, "online"));


            serializeXML();
        } else {
            deserializeXML();
        }

        //serializeXML();
    }


    /**
     * TODO questo va commentato a dovere
     */
    public void serializeXML() {

        try {

            boolean debug = true;

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
           xmlMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);


            File file = new File(this.filePath);

            // se il file non esiste, lo si crea
            if (file.createNewFile()) {
                if (debug)
                    System.out.println("FILE CREATO");
            }

            xmlMapper.writeValue(file, tree);


        } catch (JsonProcessingException e) {
            // handle exception
            System.out.println("Errore di serializzazione: " + e.getMessage());

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * TODO questo va commentato a dovere
     */
    public void deserializeXML() {
        boolean debug = true;

        try {
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(this.filePath);

            if (!file.exists()) {
                if (debug)
                    System.out.println("FILE NON ESISTE. NON CARICO NIENTE.");
                return;
            }

            List<CategoriaNonFoglia> tempCat = xmlMapper.readValue(file, new TypeReference<>() {
            });
            tree.radici.clear();
            tree.radici.addAll(tempCat);

            //TODO

            // c'è u bug incredibile cioè: ho ignorato il campo "madre" in CategoriaFoglia per evitare di
            // scrivere ricorsivamente all'infinito le figlie
            // però così durante la deserializzazione le figlie avranno solo il campo nomeMadre popolato e non
            // potranno conoscere un tubo del dominio etc etc
            // quindi l'opzione scimmia: durante la creazione trascrivo i campi da madre a figlia
            // oppure creo un metodo che fixa questo più tardi
            // scelgo la modalità scimmia

        } catch (IOException e) {
            // handle the exception
            System.out.println(e.getMessage());
        }
    }


    /**
     * Richiama il metodo necessario in base alla selezione dal menu.
     *
     * @param scelta, selezione dal menu
     */
    public void entryPoint(int scelta) {
        //TODO
        switch (scelta) {
            case 1:
                //aggiungi cat nf
                aggiungiCategoriaNF();
                break;
            case 2:
                //aggiungi cat f
                aggiungiCategoriaF();
                break;
            case 3:
                //aggiungi gerarchia
                aggiungiGerarchia();
                break;
            case 4:
                //descrizione
                aggiungiDescrizioneValoreDominio();
                break;
            case 5:
                //visualizza
                visualizzaGerarchia();
                break;
            default:
                System.out.println("Nulla da mostrare");
        }
    }

    public void aggiungiCategoriaNF() {
        //TODO
        serializeXML();
    }

    public void aggiungiCategoriaF() {
        //TODO
        serializeXML();
    }

    /**
     * Permette la creazione di una categoria radice.
     */
    public void aggiungiGerarchia() {
        //TODO
        serializeXML();
    }

    /**
     * Aggiunge la descrizione al valore di dominio di una qualche categoria.
     */
    public void aggiungiDescrizioneValoreDominio() {
        //TODO
        serializeXML();
    }

    /**
     * Stampa a video la struttura tree-like delle gerarchie di radici presenti nel programma.
     */
    public void visualizzaGerarchia() {
        //TODO
        System.out.println("Visualizza gerarchie di categorie\n");
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (CategoriaNonFoglia tempNF : tree.radici) {
            // questo toString non piace molto... per quale cazzo di motivo??
            sb.append(tempNF).append("\n\n");

        }
        return sb.toString();
    }

}
