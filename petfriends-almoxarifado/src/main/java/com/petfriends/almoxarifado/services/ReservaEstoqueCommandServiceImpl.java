package com.petfriends.almoxarifado.services;

import com.petfriends.almoxarifado.commands.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ReservaEstoqueCommandServiceImpl implements ReservaEstoqueCommandService {

    private final CommandGateway commandGateway;

    @Autowired
    public ReservaEstoqueCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> reservarEstoque(String pedidoId, List<ItemReservaRequest> itens) {
        String reservaId = UUID.randomUUID().toString();

        List<ReservarEstoqueCommand.ItemReservaDTO> itensDTO = itens.stream()
                .map(item -> new ReservarEstoqueCommand.ItemReservaDTO(item.produtoId, item.quantidade))
                .collect(Collectors.toList());

        return commandGateway.send(new ReservarEstoqueCommand(reservaId, pedidoId, itensDTO));
    }

    @Override
    public CompletableFuture<String> confirmarReserva(String id) {
        return commandGateway.send(new ConfirmarReservaCommand(id));
    }

    @Override
    public CompletableFuture<String> cancelarReserva(String id, String motivo) {
        return commandGateway.send(new CancelarReservaCommand(id, motivo));
    }

    @Override
    public CompletableFuture<String> separarItens(String id, String operadorId) {
        return commandGateway.send(new SepararItensCommand(id, operadorId));
    }
}