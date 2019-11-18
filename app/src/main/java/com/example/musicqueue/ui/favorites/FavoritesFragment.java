package com.example.musicqueue.ui.favorites;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;

public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorites, container, false);

        setColors();

        final TextView textView = root.findViewById(R.id.text_search);
        favoritesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }

    private void setColors() {
        String PRIMARY_COLOR = "#192125";
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor(PRIMARY_COLOR)));
        ((MainActivity)getActivity()).updateStatusBarColor(PRIMARY_COLOR);
        ((MainActivity)getActivity()).updateStatusBarIconColor(false);
        Toolbar toolbar = getActivity().findViewById(R.id.action_bar);
        if (toolbar!= null){
            String COLOR_FONT_LIGHT = "#F5F5F5";
            toolbar.setTitleTextColor(Color.parseColor(COLOR_FONT_LIGHT));
        }
    }
}