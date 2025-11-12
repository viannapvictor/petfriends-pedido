package com.petfriends.almoxarifado.events;

import java.util.List;

public class EstoqueInsuficiente extends BaseEvent<String> {

    public final String pedidoId;
    public final List<String> produtosIndisponiveis;
    public final String mensagem;

    public EstoqueInsuficiente(String id, String pedidoId,
                               List<String> produtosIndisponiveis, String mensagem) {
        super(id);
        this.pedidoId = pedidoId;
        this.produtosIndisponiveis = produtosIndisponiveis;
        this.mensagem = mensagem;
    }
}