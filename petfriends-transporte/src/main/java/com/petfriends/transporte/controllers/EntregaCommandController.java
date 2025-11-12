package com.petfriends.transporte.controllers;

import com.petfriends.transporte.commands.AgendarEntregaCommand.EnderecoDTO;
import com.petfriends.transporte.services.EntregaCommandService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/transporte/entregas")
public class EntregaCommandController {

    private final EntregaCommandService service;

    public EntregaCommandController(EntregaCommandService service) {
        this.service = service;
    }

    @PostMapping
    public CompletableFuture<String> agendarEntrega(@RequestBody AgendarEntregaRequest request) {
        EnderecoDTO endereco = new EnderecoDTO(
                request.rua,
                request.numero,
                request.complemento,
                request.bairro,
                request.cidade,
                request.estado,
                request.cep
        );

        return service.agendarEntrega(
                request.pedidoId,
                request.reservaId,
                endereco,
                request.dataPrevisao
        );
    }

    @PutMapping(value = "/iniciar")
    public CompletableFuture<String> iniciarTransporte(@RequestBody Map<String, String> request) {
        return service.iniciarTransporte(
                request.get("id"),
                request.get("motoristaId"),
                request.get("veiculoId")
        );
    }

    @PutMapping(value = "/concluir")
    public CompletableFuture<String> concluirEntrega(@RequestBody ConcluirEntregaRequest request) {
        return service.concluirEntrega(
                request.id,
                request.recebedor,
                request.dataRecebimento,
                request.observacoes
        );
    }

    public static class AgendarEntregaRequest {
        public String pedidoId;
        public String reservaId;
        public String rua;
        public String numero;
        public String complemento;
        public String bairro;
        public String cidade;
        public String estado;
        public String cep;
        public LocalDate dataPrevisao;

        public AgendarEntregaRequest() {}
    }

    public static class ConcluirEntregaRequest {
        public String id;
        public String recebedor;
        public LocalDateTime dataRecebimento;
        public String observacoes;

        public ConcluirEntregaRequest() {}
    }
}