package mapper;

import it.unibs.projectIngesoft.attivita.Albero;
import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.ValoreDominio;
import it.unibs.projectIngesoft.controller.ConfiguratoreController;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.mappers.CategorieMapper;
import it.unibs.projectIngesoft.parsing.SerializerJSON;
import it.unibs.projectIngesoft.utente.Configuratore;
import it.unibs.projectIngesoft.view.ConfiguratoreView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CategorieMapperTest {

    private CategorieMapper mapper;
    boolean xml;
    private CategorieModel model;
    @BeforeEach
    void prepareTest() {

        CategorieMapper mapper = new CategorieMapper("categorieTest.json",
                new SerializerJSON<List<Categoria>>());

        this.model = new CategorieModel(
                mapper);

         /*xml = true;

        if(!xml){
            this.mapper = new CategorieMapper("categorieTest.json",
                    new SerializerJSON<List<Categoria>>());
        }else{
            this.mapper = new CategorieMapper("categorie.xml",
                    new SerializerXML<List<Categoria>>());
        }*/
        //this.mapper = new CategorieMapper("categorieTest.json");
    }

    @Test
    void writeTest() {
        if(xml)
            return;


        Albero tree = new Albero();

        Categoria radice1 = new Categoria("Ripetizioni Scolastiche",
                "tipo");

        Categoria figlia11 = new Categoria("Ripetizioni di Materie Umanistiche",
                "materia",
                radice1,
                new ValoreDominio("umanistiche", "Materie umanistiche"));

        radice1.aggiungiCategoriaFiglia(figlia11);

        figlia11.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Letteratura Italiana",
            figlia11, new ValoreDominio("italiano")));
        figlia11.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Filosofia",
                figlia11, new ValoreDominio("filosofia")));
        figlia11.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Storia",
                figlia11, new ValoreDominio("storia", "Lezioni di racconti vari")));
        figlia11.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Storia dell'Arte",
                figlia11, new ValoreDominio("storia dell'arte")));

        Categoria figlia12 = new Categoria("Ripetizioni di Materie Scientifiche",
                "materia",
                radice1,
                new ValoreDominio("scientifiche", "Materie Scientifiche"));

        radice1.aggiungiCategoriaFiglia(figlia12);

        figlia12.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Matematica",
                figlia12, new ValoreDominio("matematica")));
        figlia12.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Fisica",
                figlia12, new ValoreDominio("fisica")));
        figlia12.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Informatica",
                figlia12, new ValoreDominio("informatica")));
        figlia12.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Chimica",
                figlia12, new ValoreDominio("chimica")));
        figlia12.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Biologia",
                figlia12, new ValoreDominio("biologia")));
        figlia12.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Scienze della Terra",
                figlia12, new ValoreDominio("scienze della terra")));

        Categoria figlia13 = new Categoria("Ripetizioni di lingue",
                "materia",
                radice1,
                new ValoreDominio("lingue"));

        radice1.aggiungiCategoriaFiglia(figlia13);

        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Inglese",
                figlia13, new ValoreDominio("inglese")));
        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Spagnolo",
                figlia13, new ValoreDominio("spagnolo")));
        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Francese",
                figlia13, new ValoreDominio("francese")));
        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Tedesco",
                figlia13, new ValoreDominio("tedesco")));
        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Latino",
                figlia13, new ValoreDominio("latino")));
        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Greco",
                figlia13, new ValoreDominio("greco")));
        figlia13.aggiungiCategoriaFiglia(new Categoria("Ripetizioni di Arabo",
                figlia13, new ValoreDominio("arabo")));


        Categoria radice2 = new Categoria("Lezioni di musica",
                "tipo");

        Categoria figlia21 = new Categoria("Lezioni di teoria musicale",
                "materia",
                radice2,
                new ValoreDominio("teoria"));


        figlia21.aggiungiCategoriaFiglia(new Categoria("Lezioni di solfeggio",
                figlia21, new ValoreDominio("solfeggio")));
        figlia21.aggiungiCategoriaFiglia(new Categoria("Lezioni di storia della musica",
                figlia21, new ValoreDominio("storia della musica")));

        radice2.aggiungiCategoriaFiglia(figlia21);



        Categoria radice3 = new Categoria("Attivita di svago",
                "tipo");
        Categoria figlia31 = new Categoria("Giochi di Logica",
                "gioco",
                radice3,
                new ValoreDominio("logica"));

        figlia31.aggiungiCategoriaFiglia(new Categoria("Lezioni di Scacchi",
                figlia31, new ValoreDominio("scacchi")));
        figlia31.aggiungiCategoriaFiglia(new Categoria("Lezioni di Sudoku",
                figlia31, new ValoreDominio("sudoku")));

        radice3.aggiungiCategoriaFiglia(figlia31);


        tree.getRadici().add(radice1);
        tree.getRadici().add(radice2);
        tree.getRadici().add(radice3);




        System.out.println(tree.getRadici());
        model.getRadici();
        System.out.println(model.getRadici());

        //SerializerJSON.serialize("categorieTest.json", tree.getRadici());

        // non so come controllare???
        //assert false;
        //mapper.write(tree.getRadici());
    }

    @Test
    void readTest() {

        ConfiguratoreController controller = new ConfiguratoreController(new ConfiguratoreView(),
                model, null, null, null, null, new Configuratore("test", "test"));

        controller.visualizzaGerarchie();

        //la lettura avviene durante la costruzione di model, che è fatta nei @BeforeEach
        assert model.getRadici() != null;
    }

}