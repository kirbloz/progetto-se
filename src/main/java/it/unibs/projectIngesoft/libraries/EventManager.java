package it.unibs.projectIngesoft.libraries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//EventManager è quello che si mette nelle classi da ascoltare
public class EventManager {
    // hashmap degli ascoltatori divisa per evento (String) che ascoltano
    Map<String, List<EventListener>> listeners = new HashMap<>();

    //Il costruttore prende in input tutti gli eventi che la classe che lo implementa può sparare
    public EventManager(String... operations) {
        for (String operation : operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    //subscribe serve per aggiungere dinamicamente ascoltatori all' Hashmap
    public void subscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    //unsubscribe serve per togliere dinamicamente ascoltatori dall' Hashmap
    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    //notify serve per mandare l'evento agli ascoltatori, con un qualche tipo di oggetto allegato
    public void notify(String eventType, Object o) {
        List<EventListener> observers = listeners.get(eventType);
        for (EventListener listener : observers) {
            listener.update(eventType, o);
        }
    }
}
