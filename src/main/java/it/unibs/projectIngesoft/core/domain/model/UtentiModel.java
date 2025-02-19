package it.unibs.projectIngesoft.core.domain.model;


import it.unibs.projectIngesoft.persistence.implementations.AbstractUtentiRepository;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtentiModel {

    private List<Utente> utenti;
    private final Utente defaultUtente;
    private final AbstractUtentiRepository repository;

    public UtentiModel(AbstractUtentiRepository repository) {
        this.repository = repository;
        utenti = repository.load();
        this.defaultUtente =  repository.loadDefaultUtente();
    }

    /**
     * Inserisce l'utente nell'attributo utenti se e solo se non esiste già
     *
     * @param utente, utente da inserire
     */
    public void addUtente(Utente utente) {
        assert utenti != null;
        if (!utenti.contains(utente)) {
            utenti.add(utente);
            repository.save(utenti);
        }
    }

    /**
     * Serve per il login.
     */
    public Utente verificaCredenziali(String[] credenziali) throws Exception{
        String username = credenziali[0];
        String password = credenziali[1];
        int indice;
            // controllo se credenziali default e in caso creo il nuovo configuratore
            if (username.equals(defaultUtente.getUsername()) && password.equals(defaultUtente.getPassword())) {
                Configuratore C1 = new Configuratore();
                C1.setFirstAccess(true);
                addUtente(C1);
                return C1;
            }
            // ricerca
            indice = ricercaUtente(username, password);
        if (indice == -1)
                throw new Exception();

        return utenti.get(indice);
    }

    /**
     * Effettua il controllo sulle nuove credenziali (per mantenere l'unicità dello username)
     * e poi chiama la funzione dell'utente per modificarle.
     *
     * @param C1, utente di cui cambiare le credenziali
     */
    public void cambioCredenziali(Utente C1, String newUsername,String newPassword) {
        C1.cambioCredenziali(newUsername, newPassword);
        repository.save(utenti);
    }

    /**
     * Verifica se lo username esiste già.
     *
     * @param username lo username da verificare
     * @return oggetto utente corrispondente se lo username esiste, null altrimenti
     */
    private Utente cercaConUsername(String username) {
        assert utenti != null;
        return utenti.stream()
                .filter(usr -> usr.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public boolean existsUsername(String username) {
        assert utenti != null;
        return utenti.stream()
                .anyMatch(usr -> usr.getUsername().equals(username));
    }

    /**
     * Ricerca all'interno dell'attributo utenti 'esistenza della coppia username e password data in ingresso
     *
     * @param username, username dell'utente cercato
     * @param password, password dell'utente cercato
     * @return int indice dell'oggetto, altrimenti "-1" se
     */
    public int ricercaUtente(String username, String password) {
        Utente tempUtente = cercaConUsername(username);
        if (tempUtente == null) // se l'utente non esiste, ritorna -1
            return -1;
        // se l'utente esiste allora controllo che la password sia quella corretta
        if (tempUtente.getPassword().equals(password)) {
            return utenti.indexOf(tempUtente);
        }
        // se la password è errata, ritorna -1
        return -1;
    }

    public Utente aggungiFruitore(String username, String password, String email, String comprensorio) {
        Fruitore f = new Fruitore(username, password, email, comprensorio);
        addUtente(f);
        return f;
    }

    public Optional<Fruitore> getFruitoreDaUsername(String username) {
        List<Utente> tempUtenti = new ArrayList<>(this.utenti);
        return tempUtenti.stream()
                .filter(u -> u.getClass().equals(Fruitore.class) && u.getUsername().equals(username))
                .map(u -> (Fruitore) u)
                .findFirst();
    }
}
