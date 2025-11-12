package com.petfriends.almoxarifado.domain;

import com.petfriends.almoxarifado.commands.*;
import com.petfriends.almoxarifado.events.*;
import jakarta.persistence.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Aggregate
@Entity
public class ReservaEstoque {

    @AggregateIdentifier
    @Id
    private String id;

    private String pedidoId;
    private String status;

    @ElementCollection
    @CollectionTable(name = "reserva_itens", joinColumns = @JoinColumn(name = "reserva_id"))
    private List<ItemReserva> itens;

    private String operadorId;

    public ReservaEstoque() {
        this.itens = new ArrayList<>();
    }

    @CommandHandler
    public ReservaEstoque(ReservarEstoqueCommand comando) {
        if (comando.itens == null || comando.itens.isEmpty()) {
            throw new IllegalArgumentException("Não é possível reservar estoque sem itens");
        }

        boolean estoqueDisponivel = validarDisponibilidadeEstoque(comando.itens);

        if (estoqueDisponivel) {
            List<EstoqueReservado.ItemReservado> itensReservados = comando.itens.stream()
                    .map(item -> new EstoqueReservado.ItemReservado(item.produtoId, item.quantidade))
                    .collect(Collectors.toList());

            AggregateLifecycle.apply(
                    new EstoqueReservado(comando.id, comando.pedidoId, itensReservados)
            );
        } else {
            List<String> produtosIndisponiveis = comando.itens.stream()
                    .map(item -> item.produtoId)
                    .collect(Collectors.toList());

            AggregateLifecycle.apply(
                    new EstoqueInsuficiente(
                            comando.id,
                            comando.pedidoId,
                            produtosIndisponiveis,
                            "Estoque insuficiente para atender o pedido"
                    )
            );
        }
    }

    @EventSourcingHandler
    protected void on(EstoqueReservado evento) {
        this.id = evento.id;
        this.pedidoId = evento.pedidoId;
        this.status = StatusReserva.PENDENTE.toString();
        this.itens = evento.itens.stream()
                .map(item -> new ItemReserva(item.produtoId, item.quantidade))
                .collect(Collectors.toList());
    }

    @EventSourcingHandler
    protected void on(EstoqueInsuficiente evento) {
        this.id = evento.id;
        this.pedidoId = evento.pedidoId;
        this.status = StatusReserva.INSUFICIENTE.toString();
    }

    @CommandHandler
    protected void on(ConfirmarReservaCommand comando) {
        if (StatusReserva.INSUFICIENTE.toString().equals(this.status)) {
            throw new IllegalStateException("Não é possível confirmar reserva com estoque insuficiente");
        }

        AggregateLifecycle.apply(new ReservaConfirmada(comando.id, this.pedidoId));
    }

    @EventSourcingHandler
    protected void on(ReservaConfirmada evento) {
        this.status = StatusReserva.CONFIRMADA.toString();
    }

    @CommandHandler
    protected void on(CancelarReservaCommand comando) {
        if (StatusReserva.SEPARADA.toString().equals(this.status)) {
            throw new IllegalStateException("Não é possível cancelar reserva já separada");
        }

        AggregateLifecycle.apply(
                new ReservaCancelada(comando.id, this.pedidoId, comando.motivo)
        );
    }

    @EventSourcingHandler
    protected void on(ReservaCancelada evento) {
        this.status = StatusReserva.CANCELADA.toString();
    }

    @CommandHandler
    protected void on(SepararItensCommand comando) {
        if (!StatusReserva.CONFIRMADA.toString().equals(this.status)) {
            throw new IllegalStateException("Só é possível separar itens de reservas confirmadas");
        }

        AggregateLifecycle.apply(
                new ItensSeparados(comando.id, this.pedidoId, comando.operadorId)
        );
    }

    @EventSourcingHandler
    protected void on(ItensSeparados evento) {
        this.status = StatusReserva.SEPARADA.toString();
        this.operadorId = evento.operadorId;
    }

    private boolean validarDisponibilidadeEstoque(List<ReservarEstoqueCommand.ItemReservaDTO> itens) {
        return itens.stream().allMatch(item -> item.quantidade > 0);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemReserva> getItens() {
        return itens;
    }

    public void setItens(List<ItemReserva> itens) {
        this.itens = itens;
    }

    public String getOperadorId() {
        return operadorId;
    }

    public void setOperadorId(String operadorId) {
        this.operadorId = operadorId;
    }

    @Embeddable
    public static class ItemReserva {
        private String produtoId;
        private int quantidade;

        public ItemReserva() {}

        public ItemReserva(String produtoId, int quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
        }

        public String getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(String produtoId) {
            this.produtoId = produtoId;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }
}