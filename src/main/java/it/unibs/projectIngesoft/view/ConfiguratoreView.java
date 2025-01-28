package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;

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
}
