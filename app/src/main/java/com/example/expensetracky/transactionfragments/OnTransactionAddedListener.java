package com.example.expensetracky.transactionfragments;

public interface OnTransactionAddedListener {
    void onTransactionAdded(String date, String amount, String category, String account, String notes);

}
