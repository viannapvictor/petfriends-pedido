package com.petfriends.transporte.services;

import com.petfriends.transporte.domain.Entrega;
import com.petfriends.transporte.infra.EntregaRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EntregaQueryServiceImpl implements EntregaQueryService {

    private final EventStore eventStore;
    private final EntregaRepository repository;

    @Autowired
    public EntregaQueryServiceImpl(EventStore eventStore, EntregaRepository repository) {
        this.eventStore = eventStore;
        this.repository = repository;
    }

    @Override
    public Optional<Entrega> obterPorId(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Entrega> obterPorPedidoId(String pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

    @Override
    public Optional<Entrega> obterPorReservaId(String reservaId) {
        return repository.findByReservaId(reservaId);
    }

    @Override
    public List<Object> listarEventos(String id) {
        return eventStore.readEvents(id, 0)
                .asStream()
                .map(record -> record.getPayload())
                .collect(Collectors.toList());
    }
}