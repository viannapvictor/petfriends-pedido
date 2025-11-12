package com.petfriends.transporte.infra;

import com.petfriends.transporte.domain.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, String> {

    Optional<Entrega> findByPedidoId(String pedidoId);
    Optional<Entrega> findByReservaId(String reservaId);
}