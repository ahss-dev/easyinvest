package com.easyinvest.services;

import com.easyinvest.entities.*;
import com.easyinvest.enums.AssetType;
import com.easyinvest.enums.Sex;
import com.easyinvest.enums.TransactionType;
import com.easyinvest.exceptions.AssetNotFoundException;
import com.easyinvest.exceptions.InsufficientBalanceException;
import com.easyinvest.exceptions.InvalidQuantityException;
import com.easyinvest.exceptions.PositionNotFoundException;
import com.easyinvest.repositories.AssetRepository;
import com.easyinvest.repositories.OrderRepository;
import com.easyinvest.repositories.PositionRepository;
import com.easyinvest.repositories.WalletRepository;
import com.easyinvest.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldBuyAssetSucessfully() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(50000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(44.60));
        UUID assetId = UUID.randomUUID();

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        orderService.buyAsset(user, assetId, 250);
        assertEquals(
                BigDecimal.valueOf(38850.00),
                wallet.getBalance()
        );
        verify(orderRepository).save(any(Order.class));
        verify(walletRepository).save(wallet);
    }

    @Test
    void shouldInsufficentBalance() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(500.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(44.60));
        UUID assetId = UUID.randomUUID();

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        assertThrows(InsufficientBalanceException.class, () -> orderService.buyAsset(user, assetId, 15));

        verify(orderRepository, never()).save(any(Order.class));
        verify(walletRepository, never()).save(any(Wallet.class));

    }

    @Test
    void shouldAssetNotFound() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(25000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PERT4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(20.93));
        UUID assetId = UUID.randomUUID();

        when(assetRepository.findById(assetId)).thenReturn(Optional.empty());
        assertThrows(AssetNotFoundException.class, () -> orderService.buyAsset(user, assetId, 30));

        verify(orderRepository, never()).save(any(Order.class));
        verify(walletRepository, never()).save(any(Wallet.class));

    }

    @Test
    void shouldAssetQuantityNotAllowed() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(25000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(20.93));
        UUID assetId = UUID.randomUUID();

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        assertThrows(InvalidQuantityException.class, () -> orderService.buyAsset(user, assetId, -10));

        verify(orderRepository, never()).save(any(Order.class));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void shouldPositionNotFound() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(25000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(20.93));
        UUID assetId = UUID.randomUUID();

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        when(positionRepository.findByUserAndAsset(user, asset)).thenReturn(Optional.empty());
        assertThrows(PositionNotFoundException.class, () -> orderService.sellAsset(user, assetId, 10));

        verify(orderRepository, never()).save(any(Order.class));
        verify(walletRepository, never()).save(any(Wallet.class));

    }

    @Test
    void shouldSellIsBiggerthanPosition() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(25000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(20.93));
        UUID assetId = UUID.randomUUID();

        Position position = new Position(user, asset);
        position.addQuantity(20);

        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        assertThrows(PositionNotFoundException.class, () -> orderService.sellAsset(user, assetId, 21));

        verify(orderRepository, never()).save(any(Order.class));
        verify(walletRepository, never()).save(any(Wallet.class));

    }

    @Test
    void shouldSellBeSucessful() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(25000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(20.93));
        UUID assetId = UUID.randomUUID();

        Order buyOrder = new Order();
        buyOrder.changeOperationDate(LocalDateTime.now());

        Position position = new Position(user, asset);
        position.addQuantity(20);

        when(orderRepository.findByUserAndAssetAndTransactionType(user, asset, TransactionType.COMPRA)).thenReturn(Optional.of(buyOrder));
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        when(positionRepository.findByUserAndAsset(user, asset)).thenReturn(Optional.of(position));

        orderService.sellAsset(user, assetId, 10);

        assertEquals(
                0,
                BigDecimal.valueOf(25209.30).compareTo(wallet.getBalance())
        );
        assertEquals(
                10,
                position.getQuantity()
        );
        verify(orderRepository).save(any(Order.class));
        verify(walletRepository).save(wallet);
        verify(positionRepository).save(any(Position.class));
    }

    @Test
    void shouldRemovePositionWhenQuantityBecomesZero() {

        User user = new User("rick", "rickytest@gmail.com", "123456",
                "11122233320");
        user.updateContactInfo("38999552030", "rua a", Sex.MASCULINO);
        Wallet wallet = new Wallet(user, BigDecimal.valueOf(25000.00));
        user.setWallet(wallet);
        Asset asset = new Asset("PETR4", "Petrobras", AssetType.ACAO, BigDecimal.valueOf(20.93));
        UUID assetId = UUID.randomUUID();

        Order buyOrder = new Order();
        buyOrder.changeOperationDate(LocalDateTime.now());

        Position position = new Position(user, asset);
        position.addQuantity(20);

        when(orderRepository.findByUserAndAssetAndTransactionType(user, asset, TransactionType.COMPRA)).thenReturn(Optional.of(buyOrder));
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(asset));
        when(positionRepository.findByUserAndAsset(user, asset)).thenReturn(Optional.of(position));

        orderService.sellAsset(user, assetId, 20);

        verify(positionRepository).delete(position);
    }
}
