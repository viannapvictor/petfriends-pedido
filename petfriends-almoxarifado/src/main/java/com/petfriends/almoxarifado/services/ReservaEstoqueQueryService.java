package com.petfriends.almoxarifado.services;

import com.petfriends.almoxarifado.domain.ReservaEstoque;

import java.util.List;
import java.util.Optional;

public interface ReservaEstoqueQueryService {

    Optional<ReservaEstoque> obterPorId(String id);

    Optional<ReservaEstoque> obterPorPedidoId(String pedidoId);

    List<Object> listarEventos(String id);
}