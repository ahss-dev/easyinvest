package com.easyinvest.repositories;

import com.easyinvest.entities.Asset;
import com.easyinvest.entities.Order;
import com.easyinvest.entities.User;
import com.easyinvest.enums.TradeType;
import com.easyinvest.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserId(UUID userId);

    Optional<Order> findByUserAndAssetAndTransactionType (
            User user,
            Asset asset,
            TransactionType transactionType
    );

    List<Order> findAllByUserIdOrderByOperationDateDesc(UUID userId);

    List<Order> findAllByUserIdAndTransactionTypeOrderByOperationDateDesc (UUID userId, TransactionType transactionType);

    List<Order> findAllByUserIdAndTradeTypeOrderByOperationDateDesc (UUID userId, TradeType tradeType);
}