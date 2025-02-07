package it.unibs.projectIngesoft.presentation.view;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;
import java.util.List;

// classe generica per la view del login a priori
public class AccessoView {

    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";
    public static final String MSG_RICHIESTA_EMAIL = "Inserisci la tua email: ";
    public static final String MSG_RICHIESTA_COMPRENSORIO = " Inserisci il tuo comprensorio di Appartenenza tra quelli sopra: ";


    public static final String WARNING_LOGIN_FALLITO_CREDENZIALI_ERRATE = "Login fallito, credenziali errate.";
    public static final String WARNING_USERNAME_ESISTE_GIA = ">> Errore: Username già in uso";
    public static final String WARNING_INVALID_MAIL = ">> Errore: Email non valida";

    public static final String TITLE_STARTING_MENU = "BENVENUTO";
    public static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };
    public static final String MSG_INPUT_SELEZIONE_NOME = ">> Scegli uno tra i seguenti nomi: ";
    public static final String WARNING_NON_ESISTE_COMPRENSORIO = "Errore: Comprensorio inesistente";

    public AccessoView(/*AccessoController controllerAccesso*/) {

    }

    public String[] richiestaCredenziali() {
        String username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        String password = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
        return new String[]{username,password};
    }

    public void print(String msg){
        System.out.println(msg);
    }

    public void stampaErroreCredenziali() {
        print(WARNING_LOGIN_FALLITO_CREDENZIALI_ERRATE);
    }

    public int visualizzaMenuPrincipale() {
        Menu menuPrincipale = new Menu(TITLE_STARTING_MENU,vociMenuIniziale);
        return menuPrincipale.scegli();
    }

    public void visualizzaErroreUsernameGiaInUso() {
        print(WARNING_USERNAME_ESISTE_GIA);
    }

    public String selezionaNomeComprensorioDaLista(List<String> lista){
        print(MSG_INPUT_SELEZIONE_NOME);
        for (String nome : lista) {
            print(nome);
        }
        String nomeInserito;
        boolean esisteNome = false;
        do {
            nomeInserito = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_COMPRENSORIO);
            for (String nome : lista) {
                if (nome.equals(nomeInserito)) {
                    esisteNome = true;
                    break;
                }
            }
            if (!esisteNome) {
                print(WARNING_NON_ESISTE_COMPRENSORIO);
            }
        } while (!esisteNome);
        return nomeInserito;
    }

    public String inserisciEmail() {
        return InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
    }

    public void visualizzaErroreMailNonValida() {
        print(WARNING_INVALID_MAIL);
    }

}
