package com.petfriends.transporte.events;

import java.time.LocalDateTime;

public class TransporteIniciado extends BaseEvent<String> {

    public final String motoristaId;
    public final String veiculoId;
    public final LocalDateTime dataHoraSaida;

    public TransporteIniciado(String id, String motoristaId, String veiculoId) {
        super(id);
        this.motoristaId = motoristaId;
        this.veiculoId = veiculoId;
        this.dataHoraSaida = LocalDateTime.now();
    }
}