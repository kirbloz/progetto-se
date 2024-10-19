package it.unibs.fp.myutils;

/*
Questa classe rappresenta un menu testuale generico a piu' voci
Si suppone che la voce per uscire sia sempre associata alla scelta zero
e sia presentata in fondo al menu

*/
public class MyMenu {

	private static final String CORNICE = "--------------------------------";
	private static final String VOCE_USCITA = "0\tEsci";
	private static final String RICHIESTA_INSERIMENTO = ">> ";

	private String titolo;
	private String[] voci;

	public MyMenu(String titolo, String[] voci) {
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

	/**
	 * Sostituisce una voce di un oggetto MyMenu.
	 *
	 * @author Wade Giovanni Baisini
	 * @param index, indentificativo della voce da sostituire
	 * @param newvoce, voce da inserire
	 */
	 public void modificaVoce(int index, String newvoce) {
		  this.voci[index] = newvoce;
	  }

}
