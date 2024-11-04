package it.unibs.projectIngesoft.libraries;

public class Menu {
    private static final String CORNICE = "--------------------------------";
    private static final String VOCE_USCITA = "0\tEsci";
    private static final String RICHIESTA_INSERIMENTO = "> ";

    private final String titolo;
    private String[] voci;

    public Menu(String titolo, String[] voci) {
        this.titolo = titolo;
        this.voci = voci;
    }

    public int scegli() {
        stampaMenu();
        return InputDati.leggiIntero(RICHIESTA_INSERIMENTO, 0, voci.length);
    }

    public void stampaMenu() {
        System.out.println(CORNICE);
        System.out.println(titolo);
        System.out.println(CORNICE);
        for (int i = 0; i < voci.length; i++) {
            System.out.println((i + 1) + "\t" + voci[i]);
        }
        System.out.println();
        System.out.println(VOCE_USCITA);
        System.out.println();
    }

}
