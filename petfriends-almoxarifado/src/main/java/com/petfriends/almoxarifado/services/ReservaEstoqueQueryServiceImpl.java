package com.petfriends.almoxarifado.services;

import com.petfriends.almoxarifado.domain.ReservaEstoque;
import com.petfriends.almoxarifado.infra.ReservaEstoqueRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaEstoqueQueryServiceImpl implements ReservaEstoqueQueryService {

    private final EventStore eventStore;
    private final ReservaEstoqueRepository repository;

    @Autowired
    public ReservaEstoqueQueryServiceImpl(EventStore eventStore,
                                          ReservaEstoqueRepository repository) {
        this.eventStore = eventStore;
        this.repository = repository;
    }

    @Override
    public Optional<ReservaEstoque> obterPorId(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ReservaEstoque> obterPorPedidoId(String pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

    @Override
    public List<Object> listarEventos(String id) {
        return eventStore.readEvents(id, 0)
                .asStream()
                .map(record -> record.getPayload())
                .collect(Collectors.toList());
    }
}