package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.config.ErmesConfig;
import it.unibs.projectIngesoft.persistence.implementations.*;
import it.unibs.projectIngesoft.presentation.controllers.AccessoController;
import it.unibs.projectIngesoft.presentation.controllers.BaseController;
import it.unibs.projectIngesoft.presentation.controllers.ConfiguratoreController;
import it.unibs.projectIngesoft.presentation.controllers.FruitoreController;
import it.unibs.projectIngesoft.core.domain.model.*;
import it.unibs.projectIngesoft.persistence.serialization.SerializerFactory;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Configuratore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Fruitore;
import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;
import it.unibs.projectIngesoft.presentation.view.ConfiguratoreView;
import it.unibs.projectIngesoft.presentation.view.FruitoreView;
import it.unibs.projectIngesoft.presentation.view.ViewFactory;

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

    public void mainLoop() {
        AccessoController controllerAccesso = new AccessoController(modelUtenti, compGeoModel);
        utenteAttivo = controllerAccesso.run();

        // crea il controller in base al tipo di utente
        BaseController<?> controller = createController(proposteModel);
        controller.run();
    }

    private BaseController<?> createController(ProposteModel proposteModel) {
        switch (utenteAttivo.getClass().getSimpleName()) { // si mantiene la logica condizionale in questo punto perchè è quello
            // responsabile della creazione dei controller che dipendono direttamente dal tipo di utente.
            // è anche l'unico punto dove diventano necessari i cast, tuttavia saranno sempre "esatti" perchè
            // ErmesTerminaleView viene creata in base al tipo  di utente
            case "Configuratore" -> {
                return new ConfiguratoreController((ConfiguratoreView) ViewFactory.createView(utenteAttivo),
                        categorieModel, fattoriModel, proposteModel, compGeoModel, modelUtenti, (Configuratore) utenteAttivo);
            }
            case "Fruitore" -> {
                return new FruitoreController((FruitoreView) ViewFactory.createView(utenteAttivo),
                        categorieModel, fattoriModel, proposteModel, compGeoModel, modelUtenti, (Fruitore) utenteAttivo);
            }
            default ->
                    throw new IllegalStateException("Unsupported user type: " + utenteAttivo.getClass().getSimpleName());

        }
    }

}
