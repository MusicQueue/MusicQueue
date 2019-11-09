package com.example.musicqueue.ui.song;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;

public class SongFragment extends Fragment {

    private SongViewModel songViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       songViewModel = ViewModelProviders.of(this).get(SongViewModel.class);
       View root = inflater.inflate(R.layout.fragment_library, container, false);

       setColors();

       final TextView textView = root.findViewById(R.id.text_song);
       songViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s); }
        });
       return root;
    }

    private void setColors(){
        String PRIMARY_COLOR = "#192125";
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor(PRIMARY_COLOR)));
        ((MainActivity)getActivity()).updateStatusBarColor(PRIMARY_COLOR);
        ((MainActivity)getActivity()).updateStatusBarIconColor(false);
        Toolbar toolbar = getActivity().findViewById(R.id.action_bar);
        if(toolbar != null){
            String COLOR_FONT_LIGHT = "#F5F5F5";
            toolbar.setTitleTextColor(Color.parseColor(COLOR_FONT_LIGHT));
        }
    }
}
