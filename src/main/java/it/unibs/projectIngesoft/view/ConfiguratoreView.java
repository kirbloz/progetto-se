package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;

import java.util.List;

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


    @Override
    public void stampaMenu() {
        System.out.println("TO IMPLEMENT");
    }

    @Override
    public int getUserSelection() {
        return InputDatiTerminale.leggiInteroConMinimo(">> Selezione (>0): ", 0);
    }

    @Override
    public String getUserInput() {
        return InputDatiTerminale.leggiStringaNonVuota(">> Input stringa: ");
    }







    /////////////////////////////////////////////// VIEW per i fattori /////////////////////////////////////////////////
    // TODO rivedere perché i vari check li fa il model tramite controller
    public String inserimentoNomeFogliaFormattato(String messaggio) {
        System.out.println(messaggio);
        String nomeFogliaFormattato;
        do {
            nomeFogliaFormattato = Utilitas.factorNameBuilder(
                    InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE),
                    InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA)
            );
        } while (!hashMapFattori.containsKey(nomeFogliaFormattato));
        return nomeFogliaFormattato;
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
