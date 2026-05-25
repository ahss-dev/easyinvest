package com.easyinvest.repositories;

import com.easyinvest.entities.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssetRepository  extends JpaRepository<Asset, UUID> {
}
