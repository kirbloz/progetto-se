package attivita;

import java.util.ArrayList;

public class CategoriaNonFoglia extends CategoriaRadice {

    //campo, dominio, madre, descrizione
    public Categoria madre;


    public CategoriaNonFoglia(String nome, ArrayList<String> dominio, String campo, Categoria madre) {
        super(nome, dominio, campo);
        this.madre = madre;
    }

    public CategoriaNonFoglia(String nome, String campo, Categoria madre) {
        super(nome, new ArrayList<String>(), campo);
        this.madre = madre;
    }

    public Categoria getMadre() {
        return madre;
    }

    public void setMadre(Categoria madre) {
        this.madre = madre;
    }

}
