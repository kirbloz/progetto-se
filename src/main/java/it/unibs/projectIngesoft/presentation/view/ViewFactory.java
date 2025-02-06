package it.unibs.projectIngesoft.presentation.view;

import it.unibs.projectIngesoft.core.domain.entities.utenti.Utente;

public class ViewFactory {
    public static ErmesTerminaleView createView(Utente utente) {
        String nomeClasse = "it.unibs.projectIngesoft.view." + utente.getClass().getSimpleName() + "View";
        try {
            Class<? extends ErmesTerminaleView> viewClass = Class.forName(nomeClasse).asSubclass(ErmesTerminaleView.class);
            return viewClass.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to create view for user type: " + utente.getClass().getSimpleName(), e);
        }
    }
}