package com.easyinvest.services;
import com.easyinvest.dtos.UserResponseDTO;
import com.easyinvest.dtos.UserUpdateDTO;
import com.easyinvest.dtos.UserCreateDTO;
import com.easyinvest.entities.User;
import com.easyinvest.entities.Wallet;
import com.easyinvest.repositories.UserRepository;
import com.easyinvest.security.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.wallet.initial-balance}")
    private BigDecimal initialBalance;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(UserCreateDTO dto) {

        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email já cadastrado!");
                });

        userRepository.findByCpf(dto.getCpf())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado!");
                });

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User(
                dto.getName(),
                dto.getEmail(),
                encodedPassword,
                dto.getCpf()
        );

        user.updateContactInfo(
                dto.getPhone(),
                dto.getAddress(),
                dto.getSex()
        );

        Wallet wallet = new Wallet(user, initialBalance);
        user.setWallet(wallet);

        user.updateContactInfo(dto.getPhone(), dto.getAddress(), dto.getSex());

        userRepository.save(user);
    }

    public List<UserResponseDTO> findAll() {

        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId().toString(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserResponseDTO findUserAuthenticatedById(User user) {
        return new UserResponseDTO(
                user.getId().toString(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserResponseDTO updateAuthenticatedUser(User user, UserUpdateDTO dto) {
        if (dto.getName() != null) {
            user.changeName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.changeEmail(dto.getEmail());
        }
        user.updateContactInfo(dto.getPhone(), dto.getAddress(), dto.getSex());
        User updated = userRepository.save(user);
        return new UserResponseDTO(
                updated.getId().toString(),
                updated.getName(),
                updated.getEmail()
        );
    }

    public void deleteAuthenticatedUser (User user) {
        userRepository.delete(user);
    }

    public BigDecimal getAuthenticatedUserBalance (User user) {
        BigDecimal balance = user.getWallet().getBalance();
        return balance != null ? balance : BigDecimal.ZERO;
    }
}