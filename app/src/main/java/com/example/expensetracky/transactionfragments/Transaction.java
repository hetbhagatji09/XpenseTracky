package com.example.expensetracky.transactionfragments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction implements Parcelable {
    private String date;  // Assuming the date is stored in a "yyyy-MM-dd" format
    private double amount;
    private String category;
    private String account;
    private String notes;
    private String dayOfWeek;


    // Required empty constructor for Firebase
    public Transaction() {
        // No-arg constructor is needed
    }

    public Transaction(String date, Double amount, String category, String account, String notes,String dayOfWeek) {
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.account = account;
        this.notes = notes;

        this.dayOfWeek=dayOfWeek;
    }

    protected Transaction(Parcel in) {
        date = in.readString();
        if (in.readByte() == 0) {
            amount = 0;
        } else {
            amount = in.readDouble();
        }
        category = in.readString();
        account = in.readString();
        notes = in.readString();

        dayOfWeek = in.readString();
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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

    // New Methods to get day number, day of the week, and full date
    public String getDayNumber() {
        // Assuming date is stored in the format "yyyy-MM-dd"
        return formatDateField("d");  // Day of the month
    }

    public String getDayOfWeek() {
        // Get full name of the day of the week (e.g., "Monday")
        return formatDateField("EEEE");
    }

    public String getFullDate() {
        // Get the full date in a readable format (e.g., "18 September 2024")
        return formatDateField("dd MMMM yyyy");
    }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    private String formatDateField(String format) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";  // Return a default value in case of an error
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(date);
        if (amount == 0) {
            parcel.writeByte((byte) 0);  // Indicate null amount
        } else {
            parcel.writeByte((byte) 1);  // Indicate non-null amount
            parcel.writeDouble(amount);
        }
        parcel.writeString(category);
        parcel.writeString(account);
        parcel.writeString(notes);
        parcel.writeString(dayOfWeek);
    }
}