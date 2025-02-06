package it.unibs.projectIngesoft.config;

public class ErmesConfig {

    private static final String FATTORI_DI_CONVERSIONE_JSON_FILEPATH = "fattoriTest.json";
    private static final String UTENTI_JSON_FILEPATH = "usersTest.json";
    private static final String UTENTI_DEF_CREDS_JSON_FILEPATH = "defaultCredentials.json";
    private static final String CATEGORIE_JSON_FILEPATH = "categorieTest.json";
    private static final String COMPRENSORI_GEOGRAFICI_JSON_FILEPATH = "comprensoriGeograficiTest.json";
    private static final String PROPOSTE_JSON_FILEPATH = "proposteTest.json";

    public String getFattoriPath() {
        return FATTORI_DI_CONVERSIONE_JSON_FILEPATH;
    }

    public String getUtentiPath() {
        return UTENTI_JSON_FILEPATH;
    }

    public String getDefaultCredsPath() {
        return UTENTI_DEF_CREDS_JSON_FILEPATH;
    }

    public String getCategoriePath() {
        return CATEGORIE_JSON_FILEPATH;
    }

    public String getComprensoriPath() {
        return COMPRENSORI_GEOGRAFICI_JSON_FILEPATH;
    }

    public String getPropostePath() {
        return PROPOSTE_JSON_FILEPATH;
    }

}
