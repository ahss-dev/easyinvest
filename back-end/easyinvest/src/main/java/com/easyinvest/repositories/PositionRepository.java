package com.easyinvest.repositories;

import com.easyinvest.entities.Asset;
import com.easyinvest.entities.Position;
import com.easyinvest.entities.User;
import com.easyinvest.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionRepository extends JpaRepository<Position, UUID> {

    Optional<Position> findByUserAndAsset (User user, Asset asset);

    List<Position> findByUserId(UUID userId);
}
