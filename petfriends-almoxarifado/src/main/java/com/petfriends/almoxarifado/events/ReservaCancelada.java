package com.petfriends.almoxarifado.events;

public class ReservaCancelada extends BaseEvent<String> {

    public final String pedidoId;
    public final String motivo;

    public ReservaCancelada(String id, String pedidoId, String motivo) {
        super(id);
        this.pedidoId = pedidoId;
        this.motivo = motivo;
    }
}