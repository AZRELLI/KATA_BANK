package fr.kataBank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Entity
@Table(name = "operation")
public class Operation implements Serializable{

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue
        private Long id;

        private LocalDate date;

        @Enumerated(EnumType.STRING)
        private OperationType type;

        private Long amount ;

        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "account_id")
        private Account account;

        public Operation() {
        }

        public Operation(LocalDate date, OperationType type, long amount, Account account) {
                this.date = date;
                this.type = type;
                this.amount = amount;
                this.account = account;
        }

        public Account getAccount() {
                return account;
        }

        public void setAccount(Account account) {
                this.account = account;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public LocalDate getDate() {
                return date;
        }

        public void setDate(LocalDate date) {
                this.date = date;
        }

        public OperationType getType() {
                return type;
        }

        public void setType(OperationType type) {
                this.type = type;
        }

        public Long getAmount() {
                return amount;
        }

        public void setAmount(Long amount) {
                this.amount = amount;
        }
}
