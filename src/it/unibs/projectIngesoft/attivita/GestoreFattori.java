package attivita;

import java.util.ArrayList;
import java.util.HashMap;

public class GestoreFattori {
    private String file_path;
    private HashMap<String, FattoreDiConversione> fattori;

    public GestoreFattori(String file_path){
        this.file_path = file_path;
        this.leggiFileFattori();
    }

    private void leggiFileFattori(){
        
    }
}
