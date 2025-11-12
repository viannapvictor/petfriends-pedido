package com.petfriends.almoxarifado.controllers;

import com.petfriends.almoxarifado.services.ReservaEstoqueCommandService;
import com.petfriends.almoxarifado.services.ReservaEstoqueCommandService.ItemReservaRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/almoxarifado/reservas")
public class ReservaEstoqueCommandController {

    private final ReservaEstoqueCommandService service;

    public ReservaEstoqueCommandController(ReservaEstoqueCommandService service) {
        this.service = service;
    }

    @PostMapping
    public CompletableFuture<String> reservarEstoque(@RequestBody ReservaRequest request) {
        return service.reservarEstoque(request.pedidoId, request.itens);
    }

    @PutMapping(value = "/{id}/confirmar")
    public CompletableFuture<String> confirmarReserva(@PathVariable String id) {
        return service.confirmarReserva(id);
    }

    @PutMapping(value = "/cancelar")
    public CompletableFuture<String> cancelarReserva(@RequestBody Map<String, String> request) {
        return service.cancelarReserva(request.get("id"), request.get("motivo"));
    }

    @PutMapping(value = "/separar")
    public CompletableFuture<String> separarItens(@RequestBody Map<String, String> request) {
        return service.separarItens(request.get("id"), request.get("operadorId"));
    }

    public static class ReservaRequest {
        public String pedidoId;
        public List<ItemReservaRequest> itens;

        public ReservaRequest() {}
    }
}