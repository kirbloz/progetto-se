package it.unibs.projectIngesoft.presentation.view;

import it.unibs.projectIngesoft.core.domain.entities.Categoria;
import it.unibs.projectIngesoft.core.domain.entities.Proposta;
import it.unibs.projectIngesoft.core.domain.entities.StatiProposta;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;

import java.util.List;

public abstract class ErmesTerminaleView {

    public static final String TITLE_MAIN_MENU = "MENU' PRINCIPALE - SCAMBIO ORE";

    private static final String MSG_RICHIESTA_USERNAME = ">> Inserisci un username non ancora utilizzato: ";
    private static final String MSG_RICHIESTA_PASSWORD = ">> Inserisci la password: ";

    public static final String MSG_PRINT_LISTA_RADICI = ">> Qui trovi tutte le categorie radice.\n";


    public static final String MSG_INSERISCI_NOME_FOGLIA = ">> Inserisci il nome della categoria FOGLIA:\n> ";
    public static final String MSG_INSERISCI_NOME_RADICE = ">> Inserisci il nome della categoria RADICE:\n> ";


    public static final String HEADER_PROPOSTE_CHIUSE = ">> PROPOSTE CHIUSE\n";
    public static final String HEADER_PROPOSTE_RITIRATE = ">> PROPOSTE RITIRATE\n";
    public static final String HEADER_PROPOSTE_APERTE = ">> PROPOSTE APERTE\n";

    public static final String WARNING_NO_PROPOSTE_DA_VISUALIZZARE = ">> (!!) Nessuna proposta da visualizzare.";
    public static final String WARNING_NO_GERARCHIE_MEMORIZZATE = ">> Nessuna gerarchia memorizzata.";

    public void print(String message) {
        System.out.println(message);
    }

    public String getUserInput(String prompt) {
        return InputDatiTerminale.leggiStringaNonVuota(prompt);
    }

    public String getUserInputFromNotAvailable(String prompt, String[] array) {
        return InputDatiTerminale.stringReaderNotInAvailable(prompt, array);
    }

    public String getUserInputFromAvailable(String prompt, String[] array){
        return InputDatiTerminale.stringReaderFromAvailable(prompt, array);

    }

    public String getUserInputMinMaxLength(String prompt, int minLength, int maxLength) {
        return InputDatiTerminale.stringReaderSpecificLength(prompt, minLength, maxLength);
    }

    public boolean getUserChoiceYoN(String prompt) {
        return InputDatiTerminale.yesOrNo(prompt);
    }

    public String richiestaUsername() {
        return getUserInput(MSG_RICHIESTA_USERNAME);
    }

    public String richiestaPassword() {
        return getUserInput(MSG_RICHIESTA_PASSWORD);
    }



    public String inserimentoFogliaFormattata(String messaggio) {
        // inserimento guidato e controllo [Old:A in (Old:A New:A x)]
        print(messaggio);
        return Utilitas.factorNameBuilder(
                getUserInput(MSG_INSERISCI_NOME_RADICE),
                getUserInput(MSG_INSERISCI_NOME_FOGLIA)
        );
    }


    public void visualizzaCategoria(Categoria categoria) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ").append(categoria.getNome()).append(" ]\n");

        if (!categoria.isRadice()) {  // se non è una radice, allora si stampano i dati della categoria madre
            sb.append("Madre: ").append(categoria.getNomeMadre())
                    .append("\n");
            sb.append("Dominio: ").append(categoria.getCampo())
                    .append(" = ").append(buildValoreDominioString(categoria))
                    .append("\n");
        }
        if (!categoria.isFoglia()) { // se non è foglia, si stampa il dominio impresso alle figlie
            sb.append("Dominio Figlie: ").append(categoria.getCampoFiglie())
                    .append("\n");
        } else {
            sb.append("> Foglia");
        }
        print(sb.toString());
    }

    private String buildValoreDominioString(Categoria categoria) {
        StringBuilder valoreDominioString = new StringBuilder();
        valoreDominioString.append(categoria.getNomeValoreDominio());
        if (!categoria.getDescrizioneValoreDominio().trim().isEmpty())
            valoreDominioString.append(", '").append(categoria.getDescrizioneValoreDominio()).append("'");
        return valoreDominioString.toString();
    }

    public void visualizzaFiglieCategoria(Categoria categoria) {
        if (categoria.hasFiglie()) {
            print("\t﹂ Nessuna figlia.");
            return;
        }

        List<Categoria> figlie = categoria.getCategorieFiglie();
        figlie.forEach(figlia -> {
            visualizzaCategoria(figlia);
            visualizzaFiglieCategoria(figlia);
        });

        print("");
    }

    public void visualizzaListaRadici(List<Categoria> radici) {
        System.out.println(MSG_PRINT_LISTA_RADICI);
        if (radici == null || radici.isEmpty()) {
            visualizzaErroreNoGerarchieMemorizzate();
            return;
        }
        for (Categoria radice : radici)
            visualizzaCategoria(radice);
    }

    public void visualizzaErroreNoGerarchieMemorizzate(){
        print(WARNING_NO_GERARCHIE_MEMORIZZATE);
    }

    public void visualizzaProposte(List<Proposta> lista) {
        if (lista.isEmpty()) {
            print(WARNING_NO_PROPOSTE_DA_VISUALIZZARE);
            return;
        }

        StringBuilder aperte = new StringBuilder();
        StringBuilder chiuse = new StringBuilder();
        StringBuilder ritirate = new StringBuilder();
        aperte.append(HEADER_PROPOSTE_APERTE);
        chiuse.append(HEADER_PROPOSTE_CHIUSE);
        ritirate.append(HEADER_PROPOSTE_RITIRATE);

        lista.forEach(
                proposta -> {
                    switch (proposta.getStato()) {
                        case StatiProposta.APERTA -> aperte.append(proposta).append("\n");
                        case StatiProposta.CHIUSA -> chiuse.append(proposta).append("\n");
                        case StatiProposta.RITIRATA -> ritirate.append(proposta).append("\n");
                    }
                });

        print(aperte.toString());
        print(chiuse.toString());
        print(ritirate.toString());
    }
}
