package utente;

/*la classe serve solo per identificare che tipo di azioni si possono eseguire
o le azioni che può fare il configuratore le mettiamo qui? */

public class Configuratore extends Utente{ 
    
    public Configuratore() { //per non usare null nella creazione di un configuratore vuoto
        this.username = "admin";
        this.password = "1234";
    }

}
