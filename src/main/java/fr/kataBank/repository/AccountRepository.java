package fr.kataBank.repository;

import fr.kataBank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long>{
    Account save(Account account);
    Optional<Account> findAccountById(UUID accountId);
}
