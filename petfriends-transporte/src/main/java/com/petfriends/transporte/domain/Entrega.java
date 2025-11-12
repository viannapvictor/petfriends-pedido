package com.petfriends.transporte.domain;

import com.petfriends.transporte.commands.*;
import com.petfriends.transporte.events.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@Entity
public class Entrega {

    @AggregateIdentifier
    @Id
    private String id;

    private String pedidoId;
    private String reservaId;
    private String status;
    private String enderecoCompleto;
    private String dataPrevisaoEntrega;
    private String motoristaId;
    private String veiculoId;
    private String recebedor;
    private String dataHoraRecebimento;

    public Entrega() {
    }

    @CommandHandler
    public Entrega(AgendarEntregaCommand comando) {
        if (comando.endereco == null) {
            throw new IllegalArgumentException("Endereço de entrega é obrigatório");
        }

        String enderecoCompleto = formatarEndereco(comando.endereco);

        AggregateLifecycle.apply(
                new EntregaAgendada(
                        comando.id,
                        comando.pedidoId,
                        comando.reservaId,
                        enderecoCompleto,
                        comando.dataPrevisaoEntrega
                )
        );
    }

    @EventSourcingHandler
    protected void on(EntregaAgendada evento) {
        this.id = evento.id;
        this.pedidoId = evento.pedidoId;
        this.reservaId = evento.reservaId;
        this.status = StatusEntrega.AGENDADA.toString();
        this.enderecoCompleto = evento.enderecoCompleto;
        this.dataPrevisaoEntrega = evento.dataPrevisao.toString();
    }

    @CommandHandler
    protected void on(IniciarTransporteCommand comando) {
        if (!StatusEntrega.AGENDADA.toString().equals(this.status)) {
            throw new IllegalStateException("Só é possível iniciar transporte de entregas agendadas");
        }

        AggregateLifecycle.apply(
                new TransporteIniciado(comando.id, comando.motoristaId, comando.veiculoId)
        );
    }

    @EventSourcingHandler
    protected void on(TransporteIniciado evento) {
        this.status = StatusEntrega.EM_TRANSITO.toString();
        this.motoristaId = evento.motoristaId;
        this.veiculoId = evento.veiculoId;
    }

    @CommandHandler
    protected void on(ConcluirEntregaCommand comando) {
        if (!StatusEntrega.EM_TRANSITO.toString().equals(this.status)) {
            throw new IllegalStateException("Só é possível concluir entregas em trânsito");
        }

        AggregateLifecycle.apply(
                new EntregaConcluida(
                        comando.id,
                        this.pedidoId,
                        comando.recebedor,
                        comando.dataRecebimento,
                        comando.observacoes
                )
        );
    }

    @EventSourcingHandler
    protected void on(EntregaConcluida evento) {
        this.status = StatusEntrega.CONCLUIDA.toString();
        this.recebedor = evento.recebedor;
        this.dataHoraRecebimento = evento.dataHoraRecebimento.toString();
    }

    private String formatarEndereco(AgendarEntregaCommand.EnderecoDTO endereco) {
        return String.format("%s, %s %s - %s, %s/%s - CEP: %s",
                endereco.rua,
                endereco.numero,
                endereco.complemento != null ? endereco.complemento : "",
                endereco.bairro,
                endereco.cidade,
                endereco.estado,
                endereco.cep);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getReservaId() {
        return reservaId;
    }

    public void setReservaId(String reservaId) {
        this.reservaId = reservaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getDataPrevisaoEntrega() {
        return dataPrevisaoEntrega;
    }

    public void setDataPrevisaoEntrega(String dataPrevisaoEntrega) {
        this.dataPrevisaoEntrega = dataPrevisaoEntrega;
    }

    public String getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(String motoristaId) {
        this.motoristaId = motoristaId;
    }

    public String getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(String veiculoId) {
        this.veiculoId = veiculoId;
    }

    public String getRecebedor() {
        return recebedor;
    }

    public void setRecebedor(String recebedor) {
        this.recebedor = recebedor;
    }

    public String getDataHoraRecebimento() {
        return dataHoraRecebimento;
    }

    public void setDataHoraRecebimento(String dataHoraRecebimento) {
        this.dataHoraRecebimento = dataHoraRecebimento;
    }
}