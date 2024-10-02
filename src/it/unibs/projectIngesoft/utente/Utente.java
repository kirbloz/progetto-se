package utente;

public abstract class Utente {

    protected String username;
    protected String password;

    public Utente(){   // Usato solo nel caso del primo accesso del configuratore

    }

    public Utente(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
