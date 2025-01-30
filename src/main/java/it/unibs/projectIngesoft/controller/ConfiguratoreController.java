package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.attivita.Categoria;
import it.unibs.projectIngesoft.attivita.FattoreDiConversione;
import it.unibs.projectIngesoft.libraries.InputDatiTerminale;
import it.unibs.projectIngesoft.libraries.Utilitas;
import it.unibs.projectIngesoft.model.FattoriModel;
import it.unibs.projectIngesoft.view.ConfiguratoreView;

import java.util.ArrayList;
import java.util.List;

import static it.unibs.projectIngesoft.libraries.Utilitas.MAX_FATTORE;
import static it.unibs.projectIngesoft.libraries.Utilitas.MIN_FATTORE;
import static it.unibs.projectIngesoft.view.ConfiguratoreView.INSERISCI_IL_FATTORE_TRA;
import static it.unibs.projectIngesoft.view.ConfiguratoreView.MSG_INSERISCI_FOGLIA_ESTERNA;

public class ConfiguratoreController {

	private FattoriModel fattoriModel;
	private ConfiguratoreView configuratoreView;

	//todo da rifattorizzare per l'utilizzo nel controller
	/**
	 * Cicla le foglie dalla prima all'ultima per generare tutte le coppie di valori possibili
	 * (combinazione senza ripetizione) e per ogni coppia generata chiede il fattore all'utente.
	 * Genera il FattoreDiConversione + il suo inverso e li memorizza.
	 *
	 * @param nomeRadice, nome della radice delle foglie
	 * @param foglie,     lista di foglie
	 * @return lista di fattori di conversione
	 */
	private ArrayList<FattoreDiConversione> ottieniFattoriDelleNuoveCategorie(String nomeRadice, List<Categoria> foglie) {
		if (foglie.isEmpty())
			return new ArrayList<>();
		// se esiste almeno una foglia, allora calcola i fattori
		ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = new ArrayList<>();
		for (int i = 0; i < foglie.size(); i++) {
			String nomeFogliai = Utilitas.factorNameBuilder(nomeRadice, foglie.get(i).getNome());
			for (int j = i + 1; j < foglie.size(); j++) {
				String nomeFogliaj = Utilitas.factorNameBuilder(nomeRadice, foglie.get(j).getNome());
				// TODO levare user interaction
				double fattore_ij = InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliai, nomeFogliaj), MIN_FATTORE, MAX_FATTORE);

				FattoreDiConversione fattoreIJ = new FattoreDiConversione(nomeFogliai, nomeFogliaj, fattore_ij);


				nuoviDaNuovaRadice.add(fattoreIJ);
			}
		}
		return nuoviDaNuovaRadice;
	}

	//TODO chiama questa funzione quando hai finito l'inserimento di una nuova radice con tutte le sue categorie dimmerda
	private void generaEMemorizzaNuoviFattori(String nomeRadice, List<Categoria> foglie) {
		//1. chiedi i fattori nuovi all'utente sulla base delle categorie appena inserite
        ArrayList<FattoreDiConversione> nuoviDaNuovaRadice = ottieniFattoriDelleNuoveCategorie(nomeRadice, foglie);

		//2.1. chiedi le 2 foglie (una nuova(interna) e una preesistene(esterna)) per fare iol confronto
		//2.2. chiedi il fattore di conversione EsternoInterno
		// 1. scegliere una categoria per cui i fattori siano GIA' stati calcolati
		// -> permette di calcolare tutti i nuovi chiedendo un solo inserimento di valore del fattore


		//Quando si hanno i fattori tra le nuove foglie (forse non servono gli inversi subito) ed il fattore EsternoInterno
		//si può calcolare il tutto
		if(!fattoriModel.isEmpty()) {
			configuratoreView.stampaOpzioni(fattoriModel.getKeysets());
			String nomeFogliaEsternaFormattata = configuratoreView.selezioneFoglia(MSG_INSERISCI_FOGLIA_ESTERNA);
			// 2. scegliere una categoria delle nuove, da utilizzare per il primo fattore di conversione
			String nomeFogliaInternaFormattata = configuratoreView.selezioneFogliaDaLista(nomeRadice, foglie);

			// TODO spostare user interaction
			//3. chiedi il fattore di conversione tra le 2 [x in (Old:A New:A x)]
			double fattoreDiConversioneEsternoInterno = InputDatiTerminale.leggiDoubleConRange(INSERISCI_IL_FATTORE_TRA.formatted(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata),MIN_FATTORE, MAX_FATTORE);

			fattoriModel.inserisciFattoriDiConversione(nomeFogliaEsternaFormattata, nomeFogliaInternaFormattata, fattoreDiConversioneEsternoInterno, nuoviDaNuovaRadice);
		}else {
			//Todo assumo che non si chiami questo metodo se non sono state create categorie foglia (spero wade non sia un idiota e non lo chiami)
			fattoriModel.inserisciSingolaFogliaNellaHashmap(nomeRadice, foglie);
		}
	}
}
