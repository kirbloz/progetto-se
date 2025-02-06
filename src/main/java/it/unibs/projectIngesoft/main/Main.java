package it.unibs.projectIngesoft.main;

import it.unibs.projectIngesoft.persistence.serialization.JsonSerializerFactory;

public class Main {

    public static void main(String[] args) {
        ErmesController ermesController = new ErmesController(new JsonSerializerFactory());
        ermesController.mainLoop();
    }

}