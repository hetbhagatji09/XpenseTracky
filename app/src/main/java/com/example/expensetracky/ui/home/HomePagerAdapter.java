package com.example.expensetracky.ui.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.expensetracky.transactionfragments.DailyFragment;
import com.example.expensetracky.transactionfragments.NotesFragment;

public class HomePagerAdapter extends FragmentStateAdapter {

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DailyFragment();

            case 1:
                return new NotesFragment();
            default:
                return new DailyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Total number of tabs
    }
}
