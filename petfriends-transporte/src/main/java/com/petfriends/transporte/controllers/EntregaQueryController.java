package com.petfriends.transporte.controllers;

import com.petfriends.transporte.domain.Entrega;
import com.petfriends.transporte.services.EntregaQueryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/transporte/entregas")
public class EntregaQueryController {

    private final EntregaQueryService service;

    public EntregaQueryController(EntregaQueryService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Entrega obterPorId(@PathVariable String id) {
        return service.obterPorId(id)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));
    }

    @GetMapping("/pedido/{pedidoId}")
    public Entrega obterPorPedidoId(@PathVariable String pedidoId) {
        return service.obterPorPedidoId(pedidoId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada para o pedido"));
    }

    @GetMapping("/reserva/{reservaId}")
    public Entrega obterPorReservaId(@PathVariable String reservaId) {
        return service.obterPorReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada para a reserva"));
    }

    @GetMapping("/{id}/eventos")
    public List<Object> listarEventos(@PathVariable String id) {
        return service.listarEventos(id);
    }
}