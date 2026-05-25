package com.easyinvest.services;

import com.easyinvest.dtos.OrderRequestDTO;
import com.easyinvest.dtos.OrderResponseDTO;
import com.easyinvest.entities.*;
import com.easyinvest.enums.TradeType;
import com.easyinvest.enums.TransactionType;
import com.easyinvest.exceptions.*;
import com.easyinvest.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final AssetRepository assetRepository;
    private final OrderRepository orderRepository;
    private final PositionRepository positionRepository;
    private final WalletRepository walletRepository;

    public OrderService(AssetRepository assetRepository, OrderRepository orderRepository,
                        PositionRepository positionRepository, WalletRepository walletRepository) {
        this.assetRepository = assetRepository;
        this.orderRepository = orderRepository;
        this.walletRepository = walletRepository;
        this.positionRepository = positionRepository;
    }

    @Transactional
    public void buyAsset(User user, UUID assetId, Integer quantity){

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Ativo não encontrado"));

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade inválida");
        }

        Wallet wallet = user.getWallet();

        BigDecimal price = asset.getCurrentPrice();
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));

        wallet.debit(total);
        walletRepository.save(wallet);

        Position position = positionRepository
                .findByUserAndAsset(user, asset)
                .orElseGet(() -> new Position(user, asset));
        position.updateAveragePrice(price, quantity);
        position.addQuantity(quantity);
        positionRepository.save(position);

        Order order = new Order();
        order.changeUser(user);
        order.changeAsset(asset);
        order.changeTransactionType(TransactionType.COMPRA);
        order.changePriceAtPurchase(price);
        order.changeQuantity(quantity);

        orderRepository.save(order);
    }

    @Transactional
    public void sellAsset(User user, UUID assetId, Integer quantity){

        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AssetNotFoundException("Ativo não encontrado"));

        if (quantity == null || quantity <= 0) {
            throw new InvalidQuantityException("Quantidade inválida");
        }

        Position position = positionRepository
                .findByUserAndAsset(user, asset)
                .orElseThrow(() -> new PositionNotFoundException("Usuário não possui esta quantidade ou o ativo na carteira"));

        if (position.getQuantity() < quantity) {
            throw new InvalidQuantityException("Quantidade insuficiente para venda");
        }

        BigDecimal price = asset.getCurrentPrice();
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity));

        Wallet wallet = user.getWallet();
        wallet.credit(total);
        position.removeQuantity(quantity);

        if (position.isEmpty()) {
            positionRepository.delete(position);
        } else {
            positionRepository.save(position);
        }

        Order order = new Order();
        order.changeUser(user);
        order.changeAsset(asset);
        order.changeTransactionType(TransactionType.VENDA);
        order.changePriceAtPurchase(price);
        order.changeQuantity(quantity);

        Order buyOrder = orderRepository
                .findByUserAndAssetAndTransactionType(
                        user, asset, TransactionType.COMPRA
                )
                .orElseThrow(() -> new PositionNotFoundException("Usuário não possui esse ativo"));

        TradeType tradeType = determineTradeType(
                buyOrder.getOperationDate(),
                LocalDateTime.now()
        );
        order.changeTradeType(tradeType);

        walletRepository.save(wallet);
        orderRepository.save(order);
    }

    @Transactional
    public void createOrder(OrderRequestDTO orderRequestDTO, User user){

        if (orderRequestDTO.getTransactionType() == null) {
            throw new InvalidTransactionTypeException("Tipo de transação obrigatório");
        }

        if (orderRequestDTO.getTransactionType() == TransactionType.COMPRA) {
            buyAsset(user, orderRequestDTO.getAssetId(), orderRequestDTO.getQuantity());
        } else if (orderRequestDTO.getTransactionType() == TransactionType.VENDA) {
            sellAsset(user, orderRequestDTO.getAssetId(), orderRequestDTO.getQuantity());
        }
    }

    public List<OrderResponseDTO> getUserOrdersByUser(User user, TransactionType transactionType,
                                                    TradeType tradeType) {
        List<Order> orders;

        if (tradeType != null) {
            orders = orderRepository.findAllByUserIdAndTradeTypeOrderByOperationDateDesc(user.getId(), tradeType);
        } else if (transactionType != null) {
            orders = orderRepository.findAllByUserIdAndTransactionTypeOrderByOperationDateDesc(user.getId(), transactionType);
        } else {
            orders = orderRepository.findAllByUserIdOrderByOperationDateDesc(user.getId());
        }
        return orders.stream().map(order -> new OrderResponseDTO(
                order.getAsset().getTicker(),
                order.getTradeType(),
                order.getTransactionType(),
                order.getQuantity(),
                order.getPriceAtPurchase(),
                order.getOperationDate()
        )).toList();
    }
    private TradeType determineTradeType(LocalDateTime buyDate, LocalDateTime sellDate){

        if (buyDate.toLocalDate().equals(sellDate.toLocalDate())) {
            return TradeType.DAY_TRADE;
        }
        return TradeType.SWING_TRADE;
    }
}