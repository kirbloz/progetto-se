package utente;

import it.unibs.fp.myutils.InputDati;

public abstract class Utente {

    protected String username;
    protected String password;
    public static final String MSG_RICHIESTA_NUSERNAME = "Inserisci il nuovo username: ";
    public static final String MSG_RICHIESTA_NPASSWORD = "Inserisci la nuova password: ";
   
    public Utente(){   // Usato solo nel caso del primo accesso del configuratore

    }

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public void cambioCredenziali() {
        String newUsername, newPassword;
        newUsername = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NUSERNAME);
        newPassword = InputDati.leggiStringaNonVuota(MSG_RICHIESTA_NPASSWORD);
        this.username = newUsername;
        this.password = newPassword;
        // TODO SALVARE NEL FILE
    }
}
