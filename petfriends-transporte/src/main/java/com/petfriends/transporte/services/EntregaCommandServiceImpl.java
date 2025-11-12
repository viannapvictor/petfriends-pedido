package com.petfriends.transporte.services;

import com.petfriends.transporte.commands.*;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class EntregaCommandServiceImpl implements EntregaCommandService {

    private final CommandGateway commandGateway;

    @Autowired
    public EntregaCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> agendarEntrega(String pedidoId, String reservaId,
                                                    AgendarEntregaCommand.EnderecoDTO endereco,
                                                    LocalDate dataPrevisao) {
        String entregaId = UUID.randomUUID().toString();
        return commandGateway.send(
                new AgendarEntregaCommand(entregaId, pedidoId, reservaId, endereco, dataPrevisao)
        );
    }

    @Override
    public CompletableFuture<String> iniciarTransporte(String id, String motoristaId, String veiculoId) {
        return commandGateway.send(new IniciarTransporteCommand(id, motoristaId, veiculoId));
    }

    @Override
    public CompletableFuture<String> concluirEntrega(String id, String recebedor,
                                                     LocalDateTime dataRecebimento, String observacoes) {
        return commandGateway.send(
                new ConcluirEntregaCommand(id, recebedor, dataRecebimento, observacoes)
        );
    }
}