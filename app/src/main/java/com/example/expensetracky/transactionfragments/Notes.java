package com.example.expensetracky.transactionfragments;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Notes implements Parcelable {
    private String title;
    private String note;
    private String date;  // New field for date

    public Notes(String title, String note, String date) {
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    protected Notes(Parcel in) {
        title = in.readString();
        note = in.readString();
        date = in.readString();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(note);
        parcel.writeString(date);
    }
}
