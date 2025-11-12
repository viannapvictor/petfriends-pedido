package com.petfriends.transporte.events;

import java.time.LocalDateTime;

public class EntregaConcluida extends BaseEvent<String> {

    public final String pedidoId;
    public final String recebedor;
    public final LocalDateTime dataHoraRecebimento;
    public final String observacoes;

    public EntregaConcluida(String id, String pedidoId, String recebedor,
                            LocalDateTime dataHoraRecebimento, String observacoes) {
        super(id);
        this.pedidoId = pedidoId;
        this.recebedor = recebedor;
        this.dataHoraRecebimento = dataHoraRecebimento;
        this.observacoes = observacoes;
    }
}