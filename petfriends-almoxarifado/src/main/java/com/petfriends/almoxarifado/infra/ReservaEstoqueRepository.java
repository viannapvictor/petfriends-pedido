package com.petfriends.almoxarifado.infra;

import com.petfriends.almoxarifado.domain.ReservaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservaEstoqueRepository extends JpaRepository<ReservaEstoque, String> {
    Optional<ReservaEstoque> findByPedidoId(String pedidoId);
}