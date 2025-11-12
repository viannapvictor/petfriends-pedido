package com.petfriends.almoxarifado.events;

import java.time.LocalDateTime;
import java.util.List;

public class EstoqueReservado extends BaseEvent<String> {

    public final String pedidoId;
    public final List<ItemReservado> itens;
    public final LocalDateTime dataReserva;

    public EstoqueReservado(String id, String pedidoId, List<ItemReservado> itens) {
        super(id);
        this.pedidoId = pedidoId;
        this.itens = itens;
        this.dataReserva = LocalDateTime.now();
    }

    public static class ItemReservado {
        public final String produtoId;
        public final int quantidade;

        public ItemReservado(String produtoId, int quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }
    }
}