package com.petfriends.almoxarifado.services;

import com.petfriends.almoxarifado.commands.ReservarEstoqueCommand;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ReservaEstoqueCommandService {

    CompletableFuture<String> reservarEstoque(String pedidoId, List<ItemReservaRequest> itens);

    CompletableFuture<String> confirmarReserva(String id);

    CompletableFuture<String> cancelarReserva(String id, String motivo);

    CompletableFuture<String> separarItens(String id, String operadorId);

    class ItemReservaRequest {
        public String produtoId;
        public int quantidade;

        public ItemReservaRequest() {}

        public ItemReservaRequest(String produtoId, int quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }
    }
}