package com.microtech.SmartShop.repository;

import com.microtech.SmartShop.entity.Client;
import com.microtech.SmartShop.entity.Commande;
import com.microtech.SmartShop.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);
    List<Commande> findByClientAndStatut(Client client, OrderStatus statut);
    long countByClientAndStatut(Client client, OrderStatus statut);

    @Query("SELECT COALESCE(SUM(c.totalTTC), 0) FROM Commande c WHERE c.client = :client AND c.statut = :statut")
    double sumTotalTTCByClientAndStatut(@Param("client") Client client, @Param("statut") OrderStatus statut);
    List<Commande>  findByClient(Client client);
}
