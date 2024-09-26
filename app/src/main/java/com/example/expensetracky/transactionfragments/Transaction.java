package com.example.expensetracky.transactionfragments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Transaction implements Parcelable {
    private String date;
    private String amount;
    private String category;
    private String account;
    private String notes;

    // Required empty constructor for Firebase
    public Transaction() {
        // No-arg constructor is needed
    }

    public Transaction(String date, String amount, String category, String account, String notes) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.account = account;
        this.notes = notes;
    }

    protected Transaction(Parcel in) {
        date = in.readString();
        amount = in.readString();
        category = in.readString();
        account = in.readString();
        notes = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    // Getters and Setters for Firebase to access the fields
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(date);
        parcel.writeString(amount);
        parcel.writeString(category);
        parcel.writeString(account);
        parcel.writeString(notes);
    }
}
