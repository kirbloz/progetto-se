package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;

import java.util.List;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.model.CategorieModel;

public class ConfiguratoreView implements UtenteViewableTerminal {

    public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    public static final String[] vociMainConfiguratore = new String[]{
            "Cambia Credenziali",
            "Menu Comprensorio ",
            "Menu Categorie",
            "Menu Fattori",
            "Menu Proposte"
    };

    public static final String TITLE_MENU_COMPRENSORIO = "MENU' COMPRENSORI GEOGRAFICI";
    public static final String[] vociComprensorioGeografico = new String[]{
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

    /// Copiato da FattoriModel
    public static final String INSERISCI_IL_FATTORE_TRA = ">> Inserisci il fattore tra [%s] e [%s]:\n> ";

    public static final String MSG_INSERISCI_NOME_FOGLIA = ">> Inserisci il nome della categoria FOGLIA:\n> ";
    public static final String MSG_INSERISCI_NOME_RADICE = ">> Inserisci il nome della categoria RADICE:\n> ";

    public static final String MSG_INSERISCI_FOGLIA_ESTERNA = ">> Inserire la foglia (da una gerarchia) ESTERNA con cui fare il confronto tra queste";
    public static final String MSG_INSERISCI_FOGLIA_INTERNA = ">> Inserire la foglia (dalla gerarchia) INTERNA con cui fare il confronto tra queste";
    public static final String MSG_INSERISCI_CATEGORIA_VISUALIZZA_FATTORI = ">> Inserisci la categoria di cui vuoi controllare i fattori di conversione";

    public static final String WARNING_CATEGORIA_NON_ESISTE = ">> (!!) La foglia richiesta non esiste o non esistono fattori relativi a questa foglia";
    public static final String WARNING_NO_FATTORI_MEMORIZZATI = ">> (!!) Non ci sono Fattori di Conversione memorizzati";
    /// fine roba da FattoriModel


    // STRINGHE PER CATEGORIE

    public static final String HEADER_VISUALIZZA_GERARCHIE = ">> Visualizza gerarchie di categorie <<\n";
    public static final String HEADER_VISUALIZZA_RADICE = ">> Visualizza gerarchia di %s <<\n";

    public static final String TITLE_SUBMENU_AGGIUNGI_GERARCHIA = "AGGIUNGI GERARCHIA";
    public static final String[] VOCI_SUBMENU_AGGIUNGI_GERARCHIA = new String[]{
            "Aggiungi Categoria",
    };


    public static final String ASK_INSERISCI_DESCRIZIONE_VALORE_DOMINIO = ">> Vuoi inserire una descrizione per questo valore?";
    public static final String MSG_INSERIMENTO_NUOVO_DOMINIO = ">> Inserisci il nome del dominio della nuova categoria:\n> ";
    public static final String MSG_INSERIMENTO_VALORE_DOMINIO = ">> Inserisci il valore di %s nel dominio di {%s}\n> ";
    public static final String MSG_INSERIMENTO_DOMINIO_PER_FIGLIE = ">> Inserisci il nome del dominio per eventuali figlie della nuova categoria:\n> ";

    public static final String ASK_CATEGORIA_IS_FOGLIA = ">> Questa Categoria è Foglia?";

    public static final String MSG_INSERIMENTO_NOME_CATEGORIA_MADRE = ">> Inserisci il nome della CATEGORIA MADRE per %s:\n> ";

    public static final String MSG_INSERIMENTO_RADICE = ">> Scegli un nome per il nuovo albero di categorie che non esista già.\n";
    public static final String MSG_PRINT_LISTA_RADICI = ">> Di seguito tutte le categorie radice.\n";

    public static final String MSG_SELEZIONE_RADICE = ">> Inserisci il nome di una categoria radice\n";
    public static final String MSG_INPUT_NOME_RADICE = ">> Inserisci il nome della categoria radice\n> ";
    public static final String MSG_INPUT_SCELTA_CAMPO = ">> Scegli un campo tra quelli delle categorie non foglia\n";

    public static final String MSG_INPUT_NUOVO_NOME_RADICE = ">> Inserisci il nome della NUOVA CATEGORIA RADICE:\n> ";
    public static final String MSG_INSERIMENTO_NUOVA_CATEGORIA = ">> Inserisci il nome della NUOVA CATEGORIA:\n> ";

    public static final String MSG_INPUT_DESCRIZIONE_VALORE_DOMINIO = ">> Inserisci la descrizione (da 0 a 100 caratteri):\n> ";
    public static final String CONFIRM_DESCRIZIONE_AGGIUNTA = ">> Descrizione aggiunta <<";

    public static final String WARNING_RADICE_ESISTE = ">> (!!) Per favore indica una categoria radice che non esiste già\n";
    public static final String WARNING_RADICE_NON_ESISTE = ">> (!!) Per favore indica una categoria radice che esiste";
    public static final String WARNING_CATEGORIA_ESISTE = ">> (!!) Per favore indica una categoria che non esista già in questo albero gerarchico.\n";
    public static final String WARNING_CATEGORIA_NF_NON_ESISTE = ">> (!!) Per favore indica una categoria non foglia dell'albero gerarchico selezionato.\n";
    public static final String WARNING_NO_GERARCHIE_MEMORIZZATE = ">> (!!) Nessuna gerarchia memorizzata.";


    // STRINGHE PER PROPOSTE


    // STRINGHE PER COMPRENSORI


    public void stampaMenu() {
        System.out.println("TO IMPLEMENT");
    }


    public int getUserSelection() {
        return InputDatiTerminale.leggiInteroConMinimo(">> Selezione (>0): ", 0);

    }


    public String getUserInput(String prompt) {
        return InputDatiTerminale.leggiStringaNonVuota(prompt);
    }

    @Override
    public String getUserInput() {
        return "";
    }


    public int visualizzaMenuPrincipale() {
        Menu menu = new Menu(ConfiguratoreView.TITLE_MAIN_MENU,
                vociMainConfiguratore);
        return menu.scegli();
    }


    public int visualizzaMenuCategorie() {
        Menu menuCategorie = new Menu(ConfiguratoreView.TITLE_MENU_CATEGORIE,
                vociCategorieConfiguratore);
        return menuCategorie.scegli();

    }

    public int visualizzaMenuAggiungiGerarchia() {
        Menu subMenu = new Menu(TITLE_SUBMENU_AGGIUNGI_GERARCHIA, VOCI_SUBMENU_AGGIUNGI_GERARCHIA);
        return subMenu.scegli();
    }


    public String visualizzaInserimentoCategoriaRadice(CategorieModel model) {
        visualizzaListaRadici(model.getRadici());
        return getUserInput(MSG_INSERIMENTO_RADICE + MSG_PRINT_LISTA_RADICI);
    }

    public String visualizzaInserimentoCampoCategoria() {
        return getUserInput(MSG_INSERIMENTO_NUOVO_DOMINIO);
    }


    public void uscitaMenu(String menu) {
        switch (menu) {
            case "aggiungiGerarchia":
                System.out.println(">> USCITA ZIOPERA");
            case "programma":
                System.out.println(">> USCITA PROGRAMA");
        }
    }


    /**
     * Visualizza una stringa di descrizione delle categorie radici,
     * senza eventuali figlie.
     *
     * @param radici lista radici.
     */
    public void visualizzaListaRadici(List<Categoria> radici) {
        if (radici.isEmpty())
            return;
        System.out.println(HEADER_VISUALIZZA_GERARCHIE);
        for (Categoria radice : radici) {
            System.out.println("> RADICE");
            visualizzaCategoria(radice);
        }
        System.out.println();
    }


    public void visualizzaCategoria(Categoria categoria) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(categoria.getNome()).append(" ]\n");

        if (!categoria.isRadice()) {  // se non è una radice, allora si stampano i dati della categoria madre
            sb.append("Madre: ").append(categoria.getNomeMadre())
                    .append("\n");
            sb.append("Dominio: ").append(categoria.getCampo())
                    .append(" = ").append(categoria.getValoreDominio()).
                    append("\n");
        }
        if (!categoria.isFoglia()) { // se non è foglia, si stampa il dominio impresso alle figlie
            sb.append("Dominio Figlie: ").append(categoria.getCampoFiglie())
                    .append("\n");
        } else {
            sb.append("> Foglia\n");
        }
        System.out.println(sb);
        System.out.println();
    }

    public void visualizzaFiglieCategoria(Categoria categoria) {
        if (!categoria.hasFiglie()) {
            System.out.println("\t﹂ Nessuna figlia.");
            return;
        }

        List<Categoria> figlie = categoria.getCategorieFiglie();

        figlie.forEach(figlia -> {
            System.out.println();
            visualizzaCategoria(figlia);
        });

        System.out.println();
    }







    /////////////////////////////////////////////// VIEW per i fattori /////////////////////////////////////////////////
    // TODO rivedere perché i vari check li fa il model tramite controller
    public String inserimentoNomeFogliaFormattato(String messaggio) {
        System.out.println(messaggio);
        String nomeFogliaFormattato;
        /*do {
            nomeFogliaFormattato = Utilitas.factorNameBuilder(
                    InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE),
                    InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA)
            );
        } while (!hashMapFattori.containsKey(nomeFogliaFormattato));
        return nomeFogliaFormattato;*/
        return null;
    }

    public void stampaOpzioni(String[] opzioni) {
        for (String opt : opzioni) {
            System.out.println(opt);
        }
    }

    // TODO rivedere
    /**
     * Permette di scegliere una categoria foglia tra quelle che già hanno tutti i fattori di conversione calcolati
     * e memorizzati. Guida l'immissione del nome e radice della categoria.
     *
     * @return stringa formattata come "radice:foglia"
     */
    public String selezioneFoglia(String messaggio) {
        // inserimento guidato e controllo [Old:A in (Old:A New:A x)]
        return inserimentoNomeFogliaFormattato(messaggio);
    }
    //todo rivedere
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
        System.out.println(MSG_INSERISCI_FOGLIA_INTERNA);
        for (Categoria foglia : foglie) {
            System.out.println(Utilitas.factorNameBuilder(nomeRadice, foglia.getNome()));
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
        String nomeFogliaNonFormattato;
        boolean fogliaEsiste = false;
        do {
            nomeFogliaNonFormattato = InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA);
            for (Categoria foglia : foglie) {
                if (foglia.getNome().equals(nomeFogliaNonFormattato)) {
                    fogliaEsiste = true;
                    break;
                }
            }
        } while (!fogliaEsiste);

        return Utilitas.factorNameBuilder(nomeRadice, nomeFogliaNonFormattato);
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
