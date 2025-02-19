package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.config.ErmesConfig;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.persistence.implementations.*;
import it.unibs.projectIngesoft.presentation.controllers.AccessoController;
import it.unibs.projectIngesoft.presentation.controllers.UtenteController;
import it.unibs.projectIngesoft.core.domain.model.*;
import it.unibs.projectIngesoft.persistence.serialization.SerializerFactory;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.controllers.FruitoreController;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import it.unibs.projectIngesoft.presentation.view.FruitoreView;

public class ErmesController {

    private final SerializerFactory serializerFactory;
    private final ErmesConfig config;

    private final UtentiModel modelUtenti;
    private final ComprensorioGeograficoModel compGeoModel;
    private final CategorieModel categorieModel;
    private final FattoriModel fattoriModel;
    private final ProposteModel proposteModel;
    private Utente utenteAttivo;

    public ErmesController(SerializerFactory serializerFactory) {
        this.serializerFactory = serializerFactory;

        this.config = new ErmesConfig();
        this.modelUtenti = initializeUtentiModel();
        this.compGeoModel = initializeCompGeoModel();
        this.categorieModel = initializeCategorieModel();
        this.fattoriModel = initializeFattoriModel();
        proposteModel = initializeProposteModel();
    }

    private UtentiModel initializeUtentiModel() {
        return new UtentiModel(new UtentiRepository(
                config.getUtentiPath(),
                config.getDefaultCredsPath(),
                serializerFactory.createSerializer(),
                serializerFactory.createSerializer()
        ));
    }

    private ComprensorioGeograficoModel initializeCompGeoModel() {
        return new ComprensorioGeograficoModel(
                new CompGeoRepository(config.getComprensoriPath(),
                        serializerFactory.createSerializer())
        );
    }

    private CategorieModel initializeCategorieModel() {
        return new CategorieModel(
                new CategorieRepository(config.getCategoriePath(),
                        serializerFactory.createSerializer())

        );
    }

    private FattoriModel initializeFattoriModel() {
        return new FattoriModel(
                new FattoriDiConversioneRepository(config.getFattoriPath(),
                        serializerFactory.createSerializer())
        );
    }

    private ProposteModel initializeProposteModel(){
        return new ProposteModel(
                new ProposteRepository(config.getPropostePath(),
                        serializerFactory.createSerializer()));
    }

    private UtenteController<?> createController() {
        switch (utenteAttivo.getType()) {
            case "Configuratore" -> {
                return new ConfiguratoreController(new ConfiguratoreView(),
                        categorieModel, fattoriModel, proposteModel, compGeoModel, modelUtenti, (Configuratore) utenteAttivo);
            }
            case "Fruitore" -> {
                return new FruitoreController(new FruitoreView(),
                        categorieModel, fattoriModel, proposteModel, compGeoModel, modelUtenti, (Fruitore) utenteAttivo);
            }
            default ->
                    throw new IllegalStateException("Unsupported user type: " + utenteAttivo.getClass().getSimpleName());

        }
    }

    public void run() {
        AccessoController controllerAccesso = new AccessoController(modelUtenti, compGeoModel);
        utenteAttivo = controllerAccesso.run();
        // crea il controller in base al tipo di utente attivo
        UtenteController<?> controller = createController();
        controller.run();
    }

}
