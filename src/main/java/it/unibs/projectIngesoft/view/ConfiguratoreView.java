package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.model.CategorieModel;
import it.unibs.projectIngesoft.utente.Fruitore;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfiguratoreView extends ErmesTerminaleView {

    //public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    public static final String[] vociMainConfiguratore = new String[]{
            "Cambia Credenziali",
            "Menu Comprensorio ",
            "Menu Categorie",
            "Menu Fattori",
            "Menu Proposte"
    };

    public static final String TITLE_MENU_COMPRENSORIO = "MENU' COMPRENSORI GEOGRAFICI";
    public static final String[] VOCI_COMPRENSORIO_GEOGRAFICO = new String[]{
            "Aggiungi Comprensorio Geografico",
            "Visualizza Comprensorio Geografico"
    };
    public static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    public static final String[] vociCategorieConfiguratore = new String[]{
            "Aggiungi Gerarchia",
            "Visualizza Gerarchie"
    };

    public static final String TITLE_MENU_PROPOSTE = "MENU' PROPOSTE";
    public static final String[] vociProposteConfiguratore = new String[]{
            "Visualizza proposte da categoria",
            "Visualizza proposte pronte"
    };

    public static final String TITLE_MENU_FATTORI = "MENU' FATTORI";
    public static final String[] vociFattori = new String[]{
            "Visualizza Fattori di Conversione"
    };

    // STRINGHE PER CATEGORIE

    public static final String TITLE_SUBMENU_AGGIUNGI_GERARCHIA = "AGGIUNGI GERARCHIA";
    public static final String[] VOCI_SUBMENU_AGGIUNGI_GERARCHIA = new String[]{
            "Aggiungi Categoria",
    };

    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n> ";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";

    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";
    public static final String MSG_INSERIMENTO_RADICE = ">> Scegli un nome per il nuovo albero di categorie che non esista già.\n> ";

    public static final String MSG_INSERIMENTO_NUOVA_CATEGORIA = ">> Inserisci il nome della NUOVA CATEGORIA:\n> ";

    public static final String WARNING_CATEGORIA_ESISTE = ">> (!!) Per favore indica una categoria che non esista già in questo albero gerarchico.\n";
    public static final String WARNING_CATEGORIA_NF_NON_ESISTE = ">> (!!) Per favore indica una categoria non foglia dell'albero gerarchico selezionato.\n";


    private static final String MSG_INPUT_NOME = ">> Inserisci un nome non già in uso:\n> ";
    public static final String NOME_COMPRENSORIO_FORMATTED = " [ %s ]";

    /// Copiato da FattoriModel
    public static final String INSERISCI_IL_FATTORE_TRA = ">> Inserisci il fattore tra [%s] e [%s]:\n> ";

    public static final String WARNING_CATEGORIA_NON_ESISTE = ">> (!!) La foglia richiesta non esiste o non esistono fattori relativi a questa foglia";
    public static final String WARNING_NO_FATTORI_MEMORIZZATI = ">> (!!) Non ci sono Fattori di Conversione memorizzati";

    // STRINGHE PER PROPOSTE

    public static final String HEADER_PROPOSTE_PRONTE = ">> PROPOSTE PRONTE <<";
    public static final String HEADER_PROPOSTE_CATEGORIA = ">> PROPOSTE CON %s <<\n";
    public static final String MSG_FORMATTED_PROPOSTA_PRONTA = "%s, %s\n >>> Indirizzo email: %s\n";
    public static final String WARNING_NO_COMPRENSORI_DA_VISUALIZZARE = ">> Nessun comprensorio da visualizzare.";
    public static final String INPUT_NOME_COMUNE = ">> Inserisci il nome del COMUNE: ";
    public static final String MSG_SCEGLI_NOMI_LISTA = ">> Scegli uno tra i seguenti nomi: ";
    public static final String MSG_SCELTA_CATEGORIA = ">> Scegli la categoria.";
    public static final String WARNING_NO_GERARCHIE_MEMORIZZATE = ">> Nessuna gerarchia memorizzata.";

    // STRINGHE PER COMPRENSORI

    public int visualizzaMenuPrincipale() {
        Menu menu = new Menu(TITLE_MAIN_MENU,
                vociMainConfiguratore);
        return menu.scegli();
    }

    public int visualizzaMenuCategorie() {
        Menu menuCategorie = new Menu(TITLE_MENU_CATEGORIE,
                vociCategorieConfiguratore);
        return menuCategorie.scegli();
    }

    public int visualizzaMenuAggiungiGerarchia() {
        Menu subMenu = new Menu(TITLE_SUBMENU_AGGIUNGI_GERARCHIA, VOCI_SUBMENU_AGGIUNGI_GERARCHIA);
        return subMenu.scegli();
    }

    public int visualizzaMenuFattori() {
        Menu menu = new Menu(TITLE_MENU_FATTORI,
                vociFattori);
        return menu.scegli();
    }

    public int visualizzaMenuComprensorio() {
        Menu menuCategorie = new Menu(TITLE_MENU_COMPRENSORIO, VOCI_COMPRENSORIO_GEOGRAFICO);
        return menuCategorie.scegli();
    }

    public int visualizzaMenuProposte() {
        Menu menuCategorie = new Menu(TITLE_MENU_PROPOSTE, vociProposteConfiguratore);
        return menuCategorie.scegli();
    }

    public void uscitaMenu(String menu) {
        switch (menu) {
            case "aggiungiGerarchia":
                print(">> USCITA MENU AGGIUNTA GERARCHIA..");
                break;
            case "programma":
                print(">> USCITA PROGRAMMA..");
                break;
            case "submenu":
                print(">> USCITA SUBMENU..");
                break;
        }
    }


    public String visualizzaInserimentoNomeCategoriaRadice(CategorieModel model) {
        visualizzaListaRadici(model.getRadici());
        return getUserInput(MSG_PRINT_LISTA_RADICI + MSG_INSERIMENTO_RADICE);
    }

    public String visualizzaInserimentoCampoCategoria() {
        return getUserInput(MSG_INSERIMENTO_NUOVO_DOMINIO);
    }

    public void visualizzaGerarchia(Categoria radice) {
        visualizzaCategoria(radice);
        visualizzaFiglieCategoria(radice);
    }


    /*
     * Dato il nome di una categoria ritorna una stringa formattata con tutti i fattori di conversione relativi a quella categoria
     *
     * @param categoriaFormattata, nome categoria nel formato root:leaf
     * @return String
     */
    public void visualizzaFattori(Map<String, List<FattoreDiConversione>> hashListaFattori, String categoriaFormattata) {

        if (!hashListaFattori.containsKey(categoriaFormattata)) {
            print(WARNING_CATEGORIA_NON_ESISTE);
            return;
        }

        if (hashListaFattori.get(categoriaFormattata).isEmpty()) {
            print(WARNING_NO_FATTORI_MEMORIZZATI);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (FattoreDiConversione f : hashListaFattori.get(categoriaFormattata)) {
            String valoreFormattato = String.format(Locale.US, "%.3f", f.getFattore());
            sb.append("[ ")
                    .append(f.getNome_c1()).append(", ").append(f.getNome_c2())
                    .append(", ").append(valoreFormattato).append(" ]\n");
        }
        print(sb.toString());
    }


    /// ///////INSERIMENTO PER CASI D'USO CATEGORIE //////
    /**
     * Guida l'input del nome di una nuova Categoria per una gerarchia.
     *
     * @param model,  classe Model che gestisce le gerarchie.
     * @param radice, radice della gerarchia a cui aggiungere la nuova Categoria
     * @return nome della nuova Categoria
     */
    public String inserimentoNomeNuovaCategoria(CategorieModel model, Categoria radice) {
        assert radice != null : "radice non può essere null";

        String tempNome;
        do {
            visualizzaGerarchia(radice); // stampa la gerarchia
            tempNome = getUserInput(MSG_INSERIMENTO_NUOVA_CATEGORIA);
            if (model.esisteCategoriaNellaGerarchia(tempNome, radice.getNome())) {
                System.out.print(WARNING_CATEGORIA_ESISTE);
            }
        } while (model.esisteCategoriaNellaGerarchia(tempNome, radice.getNome()));
        return tempNome;
    }

    /**
     * Guida l'immissione del nome della Categoria a cui assegnare nomeCategoria come figlia.
     * Controlla che si inserisca il nome di una Categoria che può avere figlie.
     *
     * @param nomeCategoria,  Categoria figlia da assegnare alla madre
     * @param possibiliMadri, Lista delle categorie tra cui scegliere la madre.
     * @return Categoria a cui la Categoria figlia sarà assegnata
     */
    public String inserimentoNomeCategoriaMadre(String nomeCategoria, List<String> possibiliMadri) {
        String nomeMadre;
        do {
            nomeMadre = getUserInput(String.format(MSG_INSERIMENTO_NOME_CATEGORIA_MADRE, nomeCategoria));
            if (!possibiliMadri.contains(nomeMadre))
                System.out.println(WARNING_CATEGORIA_NF_NON_ESISTE);
        } while (!possibiliMadri.contains(nomeMadre));

        return nomeMadre;
    }

    /**
     * Guida l'immissione di una stringa imponendo un vincolo di univocit&agrave; tra i valori del dominio che le Categorie "sorelle"
     * assumono.
     *
     * @param tempNome,       Categoria a cui assegnare il nome del ValoreDominio
     * @param categoriaMadre, Categoria madre da cui si eredita il dominio e da cui ottenere i valori delle "sorelle"
     * @return nome del ValoreDominio
     */
    public String inserimentoValoreDominio(String tempNome, Categoria categoriaMadre) {
        String nomeValore;
        List<String> valoriSorelle = categoriaMadre.getValoriDominioFiglie();
        do {
            nomeValore = getUserInput(String.format(MSG_INSERIMENTO_VALORE_DOMINIO, tempNome, categoriaMadre.getCampoFiglie()));
        } while (valoriSorelle.contains(nomeValore));
        return nomeValore;
    }


    /// //////////////////////////////////////////// VIEW per i fattori /////////////////////////////////////////////////


    // TODO rivedere

    /**
     * Permette di scegliere una categoria foglia tra quelle appena create.
     * Guida l'immissione del nome e radice della categoria.
     *
     * @param nomeRadice, nome della radice delle foglie
     * @param foglie,     lista di foglie appena create
     * @return stringa formattata come "radice:foglia"
     */
    public String selezioneFogliaDaLista(String nomeRadice, List<Categoria> foglie) {
        // stampa categorie interne (foglie della nuova radice)
        String[] foglieArray = new String[foglie.size()];
        for (int i = 0; i < foglie.size(); i++) {
            foglieArray[i] = Utilitas.factorNameBuilder(nomeRadice, foglie.get(i).getNome());
        }
        return selezioneFogliaDaLista(foglieArray);
    }

    public String selezioneFogliaDaLista(String[] foglie) {

        for (String foglia : foglie) {
            System.out.println(foglia);
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String fogliaSelezionata;
        boolean fogliaEsiste = false;
        do {
            fogliaSelezionata = inserimentoFogliaFormattata(MSG_SCELTA_CATEGORIA);
            for (String foglia : foglie) {
                if (foglia.equals(fogliaSelezionata)) {
                    fogliaEsiste = true;
                    break;
                }
            }
        } while (!fogliaEsiste);
        return fogliaSelezionata;
    }

    public double ottieniFattoreDiConversione(String nomeFogliaEsternaFormattata, String nomeFogliaInternaFormattata) {
        return InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata), Utilitas.MIN_FATTORE, Utilitas.MAX_FATTORE);
    }



    /// ///////////////////////////////////////////// Comprensorio View /////////////////////////////////////////////////

    public String selezionaNomeDaLista(List<String> lista) {
        System.out.println(MSG_SCEGLI_NOMI_LISTA);
        for (String nome : lista) {
            System.out.println(nome);
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String nomeInserito;
        boolean esisteNome = false;
        do {
            nomeInserito = getUserInput(MSG_INPUT_NOME);
            for (String nome : lista) {
                if (nome.equals(nomeInserito)) {
                    esisteNome = true;
                    break;
                }
            }
        } while (!esisteNome);

        return nomeInserito;
    }

    public String inserimentoNomeComprensorio(String[] array) {
        return getUserInput(MSG_INPUT_NOME, array);
    }

    public List<String> inserimentoComuni() {
        return InputDatiTerminale.inserisciListaStringheUnivoche(INPUT_NOME_COMUNE, true);
    }

    public void visualizzaComprensorio(String nome, List<String> comuni) {
        print(NOME_COMPRENSORIO_FORMATTED.formatted(nome.toUpperCase()));
        for (String s : comuni) {
            print(s);
        }
    }

    public void stampaErroreComprensoriVuoto() {
        print(WARNING_NO_COMPRENSORI_DA_VISUALIZZARE);
    }


    /// ///////////////////////////// proposte ////////////////////////////////////


    public void visualizzaProposteDaNotificare(List<Proposta> listaDaNotificare) {
        if (listaDaNotificare.isEmpty()) {
            print(WARNING_NO_PROPOSTE_DA_VISUALIZZARE);
            return;
        }

        listaDaNotificare.forEach(proposta -> {
            System.out.println(proposta);
            Fruitore autore = proposta.getAutore();
            String email = autore.getEmail();
            String comprensorio = autore.getComprensorioDiAppartenenza();
            print(MSG_FORMATTED_PROPOSTA_PRONTA.formatted(autore.getUsername(), comprensorio, email));
            proposta.notificata();
        });
    }

    public void visualizzaProposteCategoriaHeader(String categoria) {
        print(HEADER_PROPOSTE_CATEGORIA.formatted(categoria));
    }

    public void visualizzaPropostePronteHeader() {
        print(HEADER_PROPOSTE_PRONTE);
    }

}
