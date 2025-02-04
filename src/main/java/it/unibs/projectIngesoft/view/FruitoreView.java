package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.Proposta;
import it.unibs.projectIngesoft.attivita.StatiProposta;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;

import static it.unibs.projectIngesoft.view.AccessoView.MSG_RICHIESTA_PASSWORD;
import static it.unibs.projectIngesoft.view.AccessoView.MSG_RICHIESTA_USERNAME;

public class FruitoreView extends ErmesTerminaleView{

    //public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";
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

    public static final String HEADER_ESPLORAZIONE_LIVELLO = "\n>> LIVELLO CORRENTE [ %s ] <<\n";

    public static final String TITLE_SUBMENU_ESPLORA_GERARCHIA = "ESPLORA GERARCHIA";
    public static final String[] VOCI_SUBMENU_ESPLORA_GERARCHIA = new String[]{
            "Esplora un nuovo livello",
            "Torna indietro di un livello"
    };

    public static final String WARNING_NO_RAMI_DA_ESPLORARE = ">> (!!) Non ci sono nuovi rami da esplorare";


    public static final String HEADER_PROPOSTE_MODIFICABILI = ">> PROPOSTE MODIFICABILI<<\n";
    public static final String HEADER_PROPOSTE_AUTORE = ">> PROPOSTE DI %s <<\n";

    //public static final String HEADER_PROPOSTE_CHIUSE = ">> PROPOSTE CHIUSE\n";
    //public static final String HEADER_PROPOSTE_RITIRATE = ">> PROPOSTE RITIRATE\n";
    //public static final String HEADER_PROPOSTE_APERTE = ">> PROPOSTE APERTE\n";

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

    public static final String WARNING_NO_GERARCHIE_MEMORIZZATE = ">> (!!) Nessuna gerarchia memorizzata.";
    public static final String MSG_PRIMO_LIVELLO = ">> PRIMO LIVELLO";

    //@Override
    public int visualizzaMenuCategorie() {
        Menu menu = new Menu(TITLE_MENU_CATEGORIE, vociCategorieFruitore);
        return menu.scegli();
    }

    public int visualizzaMenuProposte() {
        Menu menu = new Menu(TITLE_MENU_PROPOSTE, vociProposteFruitore);
        return menu.scegli();
    }

    public int visualizzaMenuPrincipale() {
        Menu menu = new Menu(TITLE_MAIN_MENU, vociMainFruitore);
        return menu.scegli();
    }

    public int visualizzaMenuEsploraGerarchia() {
       Menu subMenu = new Menu(TITLE_SUBMENU_ESPLORA_GERARCHIA, VOCI_SUBMENU_ESPLORA_GERARCHIA);
        return subMenu.scegli();
    }

    public void visualizzaLivello(String dominio, Categoria categoriaMadre) {
        print(String.format(HEADER_ESPLORAZIONE_LIVELLO, dominio));
        if (categoriaMadre.isRadice())
            print(MSG_PRIMO_LIVELLO);

        for (Categoria categoria : categoriaMadre.getCategorieFiglie()) {
            visualizzaCategoria(categoria);
        }
    }

    public String richiestaUsername() {
        return InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
    }

    public String richiestaPassword() {
        return InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
    }

    /// ////////////////////// PROPOSTE //////////////////////////

    public void uscitaMenu(String menu) {
        switch (menu) {
            case "esplora":
                System.out.println(">> USCITA MENU ESPLORA GERARCHIE..");
                break;
            case "programma":
                System.out.println(">> USCITA PROGRAMA..");
                break;
            case "submenu":
                System.out.println(">> USCITA SUBMENU..");
                break;
        }
    }

    public int inserimentoOre() {
        return InputDatiTerminale.leggiInteroPositivo(MSG_RICHIESTA_ORE);
    }

    public void visualizzaErroreInserimentoCategoria() {
        print("Errore inserimento categoria!");
    }

    public void visualizzaErroreCalcoloOre() {
        print(WARNING_IMPOSSIBILE_CALCOLARE_ORE);
    }

    //todo formattare meglio la stringa per la stampa perchè così si fa fatica a leggerla
    public boolean confermaInserimento(String categoriaRichiesta, String categoriaOfferta, int oreRichiesta, int oreOfferta) {
        return getUserChoiceYoN("\n" + categoriaRichiesta + " : " + oreRichiesta + "\n" + categoriaOfferta + " : " + oreOfferta + "\n" + MSG_CONFERMA_PROPOSTA.formatted(oreOfferta));
    }

    public void visualizzaMessaggioAnnulla() {
        print(WARNING_PROPOSTA_ANNULLATA);
    }

    public void visualizzaMessaggioErroreDuplicato() {
        print(WARNING_PROPOSTA_DUPLICATA);
    }

    public void visualizzaErroreProposteInesistenti() {
        print(MSG_NON_HAI_PROPOSTE_NON_CHIUSE);
    }

    public boolean visualizzaInserimentoConfermaCambioStatoProposta(Proposta p) {
        StatiProposta statoAttuale = p.getStato();
        StatiProposta statoNuovo = (statoAttuale == StatiProposta.APERTA) ? StatiProposta.RITIRATA : StatiProposta.APERTA;

        return getUserChoiceYoN(MSG_CONFERMA_CAMBIO_STATO.formatted(statoAttuale, statoNuovo));
    }

    public void visualizzaConfermaCambioStatoProposta(){
        print(MSG_STATO_MODIFICATO);
    }

    public void visualizzaProposteModificabiliHeader() {
        print(HEADER_PROPOSTE_MODIFICABILI);
    }

    public void visualizzaProposteAutoreHeader(String autore) {
        print(HEADER_PROPOSTE_AUTORE.formatted(autore));
    }
}
