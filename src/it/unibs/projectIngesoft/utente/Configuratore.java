package utente;

public class Configuratore extends Utente{

    private boolean primoAccesso;

    public Configuratore(String username) {
        super(username);
        this.primoAccesso = true;
    }

    public void setPrimoAccesso(boolean primoAccesso) {
        this.primoAccesso = primoAccesso;
    }

    public boolean isPrimoAccesso() {
        return primoAccesso;
    }

    public void cambioCredenziali() {
        // TODO
    }
}
