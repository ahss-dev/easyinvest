package com.easyinvest.services;

import com.easyinvest.dtos.DashBoardResponseDTO;
import com.easyinvest.entities.User;
import com.easyinvest.entities.Wallet;
import com.easyinvest.entities.Position;
import com.easyinvest.external.MarketDataService;
import com.easyinvest.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class DashBoardService {

    private final PositionRepository positionRepository;
    private final MarketDataService marketDataService;

    public DashBoardService(PositionRepository positionRepository,
                            MarketDataService marketDataService) {
        this.positionRepository = positionRepository;
        this.marketDataService = marketDataService;
    }

    public DashBoardResponseDTO getDashBoardSummary(User user) {

        String userName = user.getName();

        Wallet wallet = user.getWallet();
        BigDecimal walletBalance = wallet.getBalance();

        List<Position> positions =
                positionRepository.findByUserId(user.getId());

        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal totalEquity = BigDecimal.ZERO;
        BigDecimal totalProfitLoss = BigDecimal.ZERO;

        for (Position position : positions) {

            BigDecimal currentPrice;
            try {
                currentPrice = marketDataService.getCurrentPrice(
                        position.getAsset().getTicker()
                );
            } catch (Exception e) {
                currentPrice = position.getAveragePrice();
            }

            BigDecimal invested =
                    position.getAveragePrice().multiply(BigDecimal.valueOf(position.getQuantity()));

            BigDecimal profitLoss =
                    currentPrice.subtract(position.getAveragePrice())
                            .multiply(BigDecimal.valueOf(position.getQuantity()));

            totalInvested = totalInvested.add(invested).setScale(2, RoundingMode.HALF_UP);
            totalProfitLoss = totalProfitLoss.add(profitLoss).setScale(2, RoundingMode.HALF_UP);
        }

        totalEquity = walletBalance
                .add(totalInvested)
                .add(totalProfitLoss);

        return new DashBoardResponseDTO(userName, walletBalance, totalInvested, totalEquity, totalProfitLoss);
    }
}