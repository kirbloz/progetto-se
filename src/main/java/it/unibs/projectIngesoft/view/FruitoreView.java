package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;
import it.unibs.projectIngesoft.libraries.Utilitas;

import java.util.function.Predicate;

public class FruitoreView {

    public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
    public static final String[] vociMainFruitore = new String[]{
            "Cambia Credenziali",
            //"Effettua proposta di scambio",
            "Menu Proposte",
            "Menu Categorie",
    };

    public static final String TITLE_MENU_CATEGORIE = "MENU' CATEGORIE";
    public static final String[] vociCategorieFruitore = new String[]{
            "Esplora Gerarchie"
    };

    public static final String TITLE_MENU_PROPOSTE = "MENU' PROPOSTE";
    public static final String[] vociProposteFruitore = new String[]{
            "Visualizza proposte inviate",
            "Effettua proposta di scambio",
            "Modifica stato proposta"
    };


    // CATEGORIE

    public static final String HEADER_ESPLORA_GERARCHIE = "\n>> ESPLORA GERARCHIE <<\n";
    public static final String HEADER_ESPLORAZIONE_LIVELLO = "\n>> LIVELLO CORRENTE [ %s ] <<\n";

    public static final String TITLE_SUBMENU_ESPLORA_GERARCHIA = "ESPLORA GERARCHIA";
    public static final String[] VOCI_SUBMENU_ESPLORA_GERARCHIA = new String[]{
            "Esplora un nuovo livello",
            "Torna indietro di un livello"
    };

    public static final String WARNING_NO_RAMI_DA_ESPLORARE = ">> (!!) Non ci sono nuovi rami da esplorare";

    public static final String HEADER_PROPOSTE_PRONTE = ">> PROPOSTE PRONTE <<";
    public static final String HEADER_PROPOSTE_MODIFICABILI = ">> PROPOSTE MODIFICABILI<<\n";
    public static final String HEADER_PROPOSTE_AUTORE = ">> PROPOSTE DI %s <<\n";
    public static final String HEADER_PROPOSTE_CATEGORIA = ">> PROPOSTE CON %s <<\n";

    public static final String HEADER_PROPOSTE_CHIUSE = ">> PROPOSTE CHIUSE\n";
    public static final String HEADER_PROPOSTE_RITIRATE = ">> PROPOSTE RITIRATE\n";
    public static final String HEADER_PROPOSTE_APERTE = ">> PROPOSTE APERTE\n";

    public static final String MSG_INSERISCI_RICHIESTA = ">> Inserisci una categoria valida di cui vuoi effettuare la RICHIESTA.";
    public static final String MSG_INSERISCI_OFFERTA = ">> Inserisci una categoria valida che sei disposto a OFFRIRE in cambio.";
    public static final String MSG_RICHIESTA_ORE = ">> Inserisci il numero di ORE che vuoi richiedere:\n> ";
    public static final String MSG_CONFERMA_PROPOSTA = ">> Dovrai offrire %d ore in cambio. Confermi?%n> ";

    private static final String MSG_INSERISCI_CATEGORIA = ">> Inserisci una categoria di cui ricercare le proposte";
    public static final String MSG_FORMATTED_PROPOSTA_PRONTA = "%s, %s\n >>> Indirizzo email: %s\n";

    public static final String MSG_SELEZIONE_CATEGORIA_RICHIESTA = ">> Inserisci la categoria RICHIESTA per la selezione: ";
    public static final String MSG_SELEZIONE_ORE = ">> Inserisci il monte ORE RICHIESTE per la selezione: ";
    public static final String MSG_SELEZIONE_CATEGORIA_OFFERTA = ">> Inserisci la categoria OFFERTA per la selezione: ";
    public static final String MSG_CONFERMA_CAMBIO_STATO = ">> Vuoi cambiare lo stato della proposta da %s a %s?";
    public static final String MSG_STATO_MODIFICATO = ">> Stato modificato in %s";

    public static final String WARNING_IMPOSSIBILE_CALCOLARE_ORE = ">> Impossibile Calcolare il numero di ore da offrire.\n";
    public static final String WARNING_PROPOSTA_ANNULLATA = ">> Proposta annullata";
    public static final String WARNING_PROPOSTA_DUPLICATA = ">> Proposta duplicata! Procedura annullata.";
    public static final String MSG_NON_HAI_PROPOSTE_NON_CHIUSE = ">> Non hai proposte non chiuse";
    public static final String MSG_INSERISCI_NOME_FOGLIA = ">> Inserisci il nome della categoria FOGLIA:\n> ";
    public static final String MSG_INSERISCI_NOME_RADICE = ">> Inserisci il nome della categoria RADICE:\n> ";



    //@Override
    public int visualizzaMenuCategorie() {
        //todo da implementare
        return 0;
    }

    public int visualizzaMenuProposte() {
        //todo da implementare
        return 0;
    }

    public int visualizzaMenuPrincipale() {
        Menu menu = new Menu(TITLE_MAIN_MENU,  vociMainFruitore);
        return menu.scegli();
    }

    public void uscitaMenu(String menu) {

    }


    public String getUserInput(String prompt) {
        return InputDatiTerminale.leggiStringaNonVuota(prompt);
    }

    public String richiestaUsername() {
        //duplicato in configuratore view
        //valutare se spostare in una classe utilities
        return "";
    }

    public String richiestaPassword() {
        //duplicato in configuratore view
        //valutare se spostare in una classe utilities
        return "";
    }

    ///////////////////////// PROPOSTE //////////////////////////

    public void visualizzaProposte(){
        //todo da implementare
    }

    public int selezioneOreOfferta(){
        return InputDatiTerminale.leggiInteroPositivo(MSG_SELEZIONE_ORE);
    }

    public boolean confermaCambioStato(String statoAttuale, String statoNuovo){
        return InputDatiTerminale.yesOrNo(MSG_CONFERMA_CAMBIO_STATO.formatted(statoAttuale, statoNuovo));
    }

    public String inserimentoFogliaFormattata(String messaggio) {
        // inserimento guidato e controllo [Old:A in (Old:A New:A x)]
        //return inserimentoNomeFogliaFormattato(messaggio);
        System.out.println(messaggio);
        return Utilitas.factorNameBuilder(
                InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_RADICE),
                InputDatiTerminale.leggiStringaNonVuota(MSG_INSERISCI_NOME_FOGLIA)
        );
    }


}
