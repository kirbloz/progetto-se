package it.unibs.projectIngesoft.view;

import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Menu;
import java.util.List;

// classe generica per la view del login a priori
public class AccessoView /*implements EventListener*/ {

    public static final String MSG_RICHIESTA_USERNAME = "Inserisci il tuo username: ";
    public static final String MSG_RICHIESTA_PASSWORD = "Inserisci la tua password: ";
    public static final String MSG_RICHIESTA_EMAIL = "Inserisci la tua email: ";
    public static final String MSG_RICHIESTA_COMPRENSORIO = " Inserisci il tuo comprensorio di Appartenenza tra quelli sopra: ";

    public static final String WARNING_USERNAME_ESISTE_GIA = ">> Errore: Username già in uso";
    public static final String WARNING_INVALID_MAIL = ">> Errore: Email non valida";

    public static final String TITLE_STARTING_MENU = "BENVENUTO";
    public static final String[] vociMenuIniziale = new String[]{
            "Login",
            "Registrazione"
    };

    public AccessoView(/*AccessoController controllerAccesso*/) {

    }

    public String[] richiestaCredenziali() {
        String username = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_USERNAME);
        String password = InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_PASSWORD);
        return new String[]{username,password};
    }

    public void stampaErroreCredenziali(String msg) {
        System.out.println(msg);
    }

    public int visualizzaMenuPrincipale() {
        Menu menuPrincipale = new Menu(TITLE_STARTING_MENU,vociMenuIniziale);
        return menuPrincipale.scegli();
    }

    public void visualizzaErroreUsernameGiaInUso() {
        System.out.println(WARNING_USERNAME_ESISTE_GIA);
    }

    public String selezionaNomeDaLista(List<String> lista){
        System.out.println(">> Scegli uno tra i seguenti nomi: ");
        for (String nome : lista) {
            System.out.println(nome);
        }
        // immissione della foglia e verifica che sia corretto [New:A in (Old:A New:A x)]
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
                System.out.println("Errore: Comprensorio inesistente");
            }
        } while (!esisteNome);

        return nomeInserito;
    }

    public String inserisciEmail() {
        return InputDatiTerminale.leggiStringaNonVuota(MSG_RICHIESTA_EMAIL);
    }

    public void visualizzaErroreMailNonValida() {
        System.out.println(WARNING_INVALID_MAIL);
    }

}
