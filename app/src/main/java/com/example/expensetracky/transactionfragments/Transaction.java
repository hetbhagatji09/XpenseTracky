package com.example.expensetracky.transactionfragments;

public class Transaction {
    private String date;
    private String amount;
    private String category;
    private String account;
    private String notes;

    public Transaction(String date, String amount, String category, String account, String notes) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.account = account;
        this.notes = notes;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getAccount() {
        return account;
    }

    public String getNotes() {
        return notes;
    }
    @Override
    public String toString() {
        return "Transaction{" +
                "date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", category='" + category + '\'' +
                ", account='" + account + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}

