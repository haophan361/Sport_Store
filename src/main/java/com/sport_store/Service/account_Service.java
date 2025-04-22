package com.sport_store.Service;

import com.sport_store.Entity.Accounts;
import com.sport_store.Repository.account_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class account_Service {
    private final PasswordEncoder passwordEncoder;
    private final account_Repository account_repository;

    public void changePassword(Accounts account, String new_password) {
        account.setPassword(passwordEncoder.encode(new_password));
        account_repository.save(account);
    }

    public String getEmail() {
        var context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }

    public Accounts get_myAccountInfo() {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        return account_repository.findAccountsByEmail(email);
    }

    public Accounts getAccountByEmail(String email) {
        return account_repository.findAccountsByEmail(email);
    }

    public boolean existByEmail(String email) {
        return account_repository.existsByEmail(email);
    }

    public void Update_isOnline(String email, boolean isOnline) {
        Accounts account = account_repository.findAccountsByEmail(email);
        account.set_online(isOnline);
        account_repository.save(account);
    }

    public void create_account(Accounts account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account_repository.save(account);
    }

    public void updateOnline() {
        account_repository.updateIsOnline();
    }
}
