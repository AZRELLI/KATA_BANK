package fr.kataBank.domain.dto;

public class OperationDto {

    private long amount ;

    public OperationDto() {
    }

    public OperationDto(long amount) {
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }
}
