package com.easyinvest.services;

import com.easyinvest.dtos.PositionResponseDTO;
import com.easyinvest.entities.Position;
import com.easyinvest.entities.User;
import com.easyinvest.external.MarketDataService;

import com.easyinvest.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PositionService {

    private final PositionRepository positionRepository;
    private final MarketDataService marketDataService;

    public PositionService(PositionRepository positionRepository, MarketDataService marketDataService) {
        this.positionRepository = positionRepository;
        this.marketDataService = marketDataService;
    }

    public List<PositionResponseDTO> getUserPositions(User user) {

        List<Position> positions = positionRepository.findByUserId(user.getId());

        return positions.stream()
                .map(position -> {
                    BigDecimal currentPrice;
                    try {
                        currentPrice = marketDataService.getCurrentPrice(
                                        position.getAsset().getTicker()
                                );
                    } catch (Exception e) {
                        currentPrice = position.getAveragePrice();
                    }

                    BigDecimal profitLoss =
                            currentPrice.subtract(position.getAveragePrice())
                                    .multiply(BigDecimal.valueOf(position.getQuantity()));

                    return new PositionResponseDTO(
                            position.getAsset().getTicker(),
                            position.getQuantity(),
                            position.getAveragePrice(),
                            position.getAveragePrice()
                                    .multiply(BigDecimal.valueOf(position.getQuantity())),
                            currentPrice,
                            profitLoss
                    );
                }).toList();
    }
}