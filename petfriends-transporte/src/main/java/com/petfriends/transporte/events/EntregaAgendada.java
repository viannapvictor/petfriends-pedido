package com.petfriends.transporte.events;

import java.time.LocalDate;

public class EntregaAgendada extends BaseEvent<String> {

    public final String pedidoId;
    public final String reservaId;
    public final String enderecoCompleto;
    public final LocalDate dataPrevisao;

    public EntregaAgendada(String id, String pedidoId, String reservaId,
                           String enderecoCompleto, LocalDate dataPrevisao) {
        super(id);
        this.pedidoId = pedidoId;
        this.reservaId = reservaId;
        this.enderecoCompleto = enderecoCompleto;
        this.dataPrevisao = dataPrevisao;
    }
}