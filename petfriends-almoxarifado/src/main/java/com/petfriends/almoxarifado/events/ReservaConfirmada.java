package com.petfriends.almoxarifado.events;

public class ReservaConfirmada extends BaseEvent<String> {

    public final String pedidoId;

    public ReservaConfirmada(String id, String pedidoId) {
        super(id);
        this.pedidoId = pedidoId;
    }
}