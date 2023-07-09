package fr.kataBank.repository;

import fr.kataBank.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface OperationRepository extends JpaRepository<Operation,Long> {
    List<Operation> findAllByIdAccountAndOperationDateBetweenOrderByDateDesc(UUID idAccount, LocalDate startDate, LocalDate endDate);

}
