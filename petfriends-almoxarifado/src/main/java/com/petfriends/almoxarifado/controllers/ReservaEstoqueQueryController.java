package com.petfriends.almoxarifado.controllers;

import com.petfriends.almoxarifado.domain.ReservaEstoque;
import com.petfriends.almoxarifado.services.ReservaEstoqueQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/almoxarifado/reservas")
public class ReservaEstoqueQueryController {

    private final ReservaEstoqueQueryService service;

    public ReservaEstoqueQueryController(ReservaEstoqueQueryService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ReservaEstoque obterPorId(@PathVariable String id) {
        return service.obterPorId(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ReservaEstoque obterPorPedidoId(@PathVariable String pedidoId) {
        return service.obterPorPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada para o pedido"));
    }

    @GetMapping("/{id}/eventos")
    public List<Object> listarEventos(@PathVariable String id) {
        return service.listarEventos(id);
    }
}