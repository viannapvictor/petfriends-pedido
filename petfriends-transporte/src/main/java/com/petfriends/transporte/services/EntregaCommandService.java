package com.petfriends.transporte.services;

import com.petfriends.transporte.commands.AgendarEntregaCommand.EnderecoDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public interface EntregaCommandService {

    CompletableFuture<String> agendarEntrega(String pedidoId, String reservaId,
                                             EnderecoDTO endereco, LocalDate dataPrevisao);

    CompletableFuture<String> iniciarTransporte(String id, String motoristaId, String veiculoId);

    CompletableFuture<String> concluirEntrega(String id, String recebedor,
                                              LocalDateTime dataRecebimento, String observacoes);
}