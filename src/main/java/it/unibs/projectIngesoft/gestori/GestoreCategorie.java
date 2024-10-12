package it.unibs.projectIngesoft.gestori;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.CategoriaFoglia;
import it.unibs.projectIngesoft.attivita.CategoriaNonFoglia;
import it.unibs.projectIngesoft.attivita.ValoreDominio;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestoreCategorie {
    // non sono sicuro di quale struttura dati utilizzare
    ArrayList<Categoria> radici;
    private final String filePath;


    public GestoreCategorie(String filePath) {
        /* legge da file memorizza le radici già presenti
        prepara tutto apposto */
        this.filePath = filePath;
        radici = new ArrayList<>();
        //deserializeXML(); // load dati


        boolean debug_data = true;
        if (debug_data) {
            ArrayList<ValoreDominio> valori = new ArrayList<>();
            valori.add(new ValoreDominio("valore1", "desc1"));
            CategoriaNonFoglia radice1 = new CategoriaNonFoglia("cat1 radice", "materia", valori);
            radici.add(radice1);
            radice1.addCategoriaFiglia(new CategoriaFoglia("figlia1", radice1, "matematica"));
            radice1.addCategoriaFiglia(new CategoriaFoglia("figlia2", radice1, "italiano"));
        }

        serializeXML();
    }


    /**
     * TODO questo va commentato a dovere
     */
    public void serializeXML() {

        try {

            boolean debug = true;

            // creazione mapper e oggetto file
            XmlMapper xmlMapper = new XmlMapper();
            File file = new File(this.filePath);
            // se il file non esiste, lo si crea
            if (file.createNewFile()) {
                if (debug)
                    System.out.println("FILE CREATO");
            }
            // PER QUALCHE MOTIVO
            // QUESTA COSA SCRIVE UN XML
            // CON PIU DI 1000 ELEMENTI NESTATI FRA LORO
            // TODO
            // DA SISTEMARE
            xmlMapper.writeValue(file, this.radici);


        } catch (JsonProcessingException e) {
            // handle exception
            System.out.println(e.getMessage());

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

            List<Categoria> tempCat = xmlMapper.readValue(file, new TypeReference<>() {
            });

            // scorro la lista tempCat e in base al tipo di categoria letta, la inserisco
            // come radice
            // o come figlia di altra
            for (int i = 0; i < tempCat.size(); i++) {

                /*
                 * TODO
                 *  SI POTREBBE OTTIMIZZARE MA MI PESA IL CULO
                 */

                if (tempCat.get(i) instanceof CategoriaNonFoglia tempNF) {

                    // se è una radice, la aggiungo all'arraylist del gestore e finita lì
                    if (tempNF.isRadice()) {
                        radici.add(tempNF);
                    } else {
                        // se non è una radice, allora ha una madre
                        // qui la cat figlia si aggiunge tra le figlie di una radice
                        CategoriaNonFoglia madre = tempNF.getMadre();

                        // se la madre esiste allora si aggiunge la figlia e basta
                        // altrimenti aggiungo la madre
                        if (!this.radici.contains(madre)) {
                            this.radici.add(madre);
                        }
                        madre.addCategoriaFiglia(tempNF);

                    }
                } else if (tempCat.get(i) instanceof CategoriaFoglia tempF) {
                    CategoriaNonFoglia madre = tempF.getMadre();
                    madre.addCategoriaFiglia(tempF);

                    // se la madre esiste allora si aggiunge la figlia e basta
                    // altrimenti aggiungo la madre
                    if (!this.radici.contains(madre)) {
                        this.radici.add(madre);
                    }
                    madre.addCategoriaFiglia(tempF);

                }
            }

            if (debug)
                for (Categoria obj : tempCat) {
                    System.out.println(obj.toString());
                }
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
    }

    public void aggiungiCategoriaF() {
        //TODO
    }

    /**
     * Permette la creazione di una categoria radice.
     */
    public void aggiungiGerarchia() {
        //TODO
    }

    /**
     * Aggiunge la descrizione al valore di dominio di una qualche categoria.
     */
    public void aggiungiDescrizioneValoreDominio() {
        //TODO
    }

    /**
     * Stampa a video la struttura tree-like delle gerarchie di radici presenti nel programma.
     */
    public void visualizzaGerarchia() {
        //TODO
        System.out.println("Visualizza gerarchie di categorie");
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.radici.size(); i++) {
            if (this.radici.get(i) instanceof CategoriaNonFoglia) {
                sb.append(this.radici.get(i).toString()).append("\n\n");
            }
            sb.append(" -----------------------------------------\n");
        }
        return sb.toString();
    }

}
