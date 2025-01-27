package it.unibs.projectIngesoft.libraries;

public interface EventListener {
    //Serve per aggiornare se stessi all'arrivo di un evento in base a che evento si è ricevuto
    void update(String eventType, Object o);
}
