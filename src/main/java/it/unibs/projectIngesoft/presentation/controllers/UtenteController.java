package it.unibs.projectIngesoft.presentation.controllers;

import it.unibs.projectIngesoft.core.domain.model.*;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;

public abstract class UtenteController<T extends Utente> {

    protected final CategorieModel categorieModel;
    protected final FattoriModel fattoriModel;
    protected final ProposteModel proposteModel;
    protected final ComprensorioGeograficoModel compGeoModel;
    protected final UtentiModel utentiModel;
    protected final T utenteAttivo;

    protected UtenteController(
            CategorieModel categorieModel,
            FattoriModel fattoriModel,
            ProposteModel proposteModel,
            ComprensorioGeograficoModel compGeoModel,
            UtentiModel utentiModel,
            T utenteAttivo) {
        this.categorieModel = categorieModel;
        this.fattoriModel = fattoriModel;
        this.proposteModel = proposteModel;
        this.compGeoModel = compGeoModel;
        this.utentiModel = utentiModel;
        this.utenteAttivo = utenteAttivo;
    }

    /**
     * Esegue la logica principale di ogni controller.
     * Ogni controller specifico deve definire il suo comportamento.
     */
    public abstract void run();

    protected abstract void cambioCredenziali();

}
