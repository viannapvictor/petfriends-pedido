package com.petfriends.transporte.services;

import com.petfriends.transporte.domain.Entrega;

import java.util.List;
import java.util.Optional;

public interface EntregaQueryService {

    Optional<Entrega> obterPorId(String id);

    Optional<Entrega> obterPorPedidoId(String pedidoId);

    Optional<Entrega> obterPorReservaId(String reservaId);

    List<Object> listarEventos(String id);
}