package com.petfriends.almoxarifado.events;

import java.time.LocalDateTime;

public class ItensSeparados extends BaseEvent<String> {

    public final String pedidoId;
    public final String operadorId;
    public final LocalDateTime dataSeparacao;

    public ItensSeparados(String id, String pedidoId, String operadorId) {
        super(id);
        this.pedidoId = pedidoId;
        this.operadorId = operadorId;
        this.dataSeparacao = LocalDateTime.now();
    }
}