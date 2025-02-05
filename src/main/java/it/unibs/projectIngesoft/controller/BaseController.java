package it.unibs.projectIngesoft.controller;

import it.unibs.projectIngesoft.model.*;
import it.unibs.projectIngesoft.utente.Utente;
import it.unibs.projectIngesoft.view.ErmesTerminaleView;

public abstract class BaseController<T extends Utente> {

    protected final ErmesTerminaleView view;
    protected final CategorieModel categorieModel;
    protected final FattoriModel fattoriModel;
    protected final ProposteModel proposteModel;
    protected final ComprensorioGeograficoModel compGeoModel;
    protected final UtentiModel utentiModel;
    protected final T utenteAttivo;

    protected BaseController(
            ErmesTerminaleView view,
            CategorieModel categorieModel,
            FattoriModel fattoriModel,
            ProposteModel proposteModel,
            ComprensorioGeograficoModel compGeoModel,
            UtentiModel utentiModel,
            T utenteAttivo) {
        this.view = view;
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

}
