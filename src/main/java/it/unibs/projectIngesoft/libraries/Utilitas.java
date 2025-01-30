package it.unibs.projectIngesoft.libraries;

public class Utilitas {

	public static final double MIN_FATTORE = 0.5;
	public static final double MAX_FATTORE = 2.0;


	/**
	 * Costruisce la stringa che rappresenta univocamente un fattore.
	 * Concatena il nome della radice, un ":" e il nome della categoria foglia di riferimento.
	 *
	 * @param root, categoria radice della categoria foglia
	 * @param leaf, categoria foglia di riferimento
	 * @return stringa formattata
	 */
	public static String factorNameBuilder(String root, String leaf) {
		return root + ":" + leaf;
	}
}
