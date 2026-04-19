package com.easyinvest.services;
import com.easyinvest.dto.UserResponseDTO;
import com.easyinvest.dto.UserUpdateDTO;
import com.easyinvest.dto.UserCreateDTO;
import com.easyinvest.entities.User;
import com.easyinvest.entities.Wallet;
import com.easyinvest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    public UserResponseDTO createUser(UserCreateDTO dto) {

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

        Wallet wallet = new Wallet(user, initialBalance);
        user.setWallet(wallet);

        user.updateContactInfo(dto.getPhone(), dto.getAddress());

        User saved = userRepository.save(user);

        return new UserResponseDTO(
                saved.getId().toString(),
                saved.getName(),
                saved.getEmail()
        );
    }

    public List<UserResponseDTO> findAll() {
        if (userRepository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhum usuário encontrado!");
        }
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(
                        user.getId().toString(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    public UserResponseDTO findById(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));
        return new UserResponseDTO(
                user.getId().toString(),
                user.getName(),
                user.getEmail()
        );
    }

    public UserResponseDTO update(String id, UserUpdateDTO dto) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        if (dto.getName() != null) {
            user.changeName(dto.getName());
        }

        user.updateContactInfo(dto.getPhone(), dto.getAddress());

        User updated = userRepository.save(user);

        return new UserResponseDTO(
                updated.getId().toString(),
                updated.getName(),
                updated.getEmail()
        );
    }

    public void delete(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        userRepository.delete(user);
    }

    public BigDecimal getUserBalance(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!"));
        return user.getWallet().getBalance();
    }

}