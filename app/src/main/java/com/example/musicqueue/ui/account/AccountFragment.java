package com.example.musicqueue.ui.account;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;
import com.example.musicqueue.authentication.SignInActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        setColors();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        final TextView nameTV = root.findViewById(R.id.account_name);
        nameTV.setText(firebaseUser.getDisplayName().toString());

        final TextView emailTV = root.findViewById(R.id.account_email);
        emailTV.setText(firebaseUser.getEmail().toString());

        root.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return root;
    }

    private void signOut() {
        firebaseAuth.signOut();
        startActivity(new Intent(getActivity(), SignInActivity.class));
        getActivity().finish();
    }

    private void setColors() {
        String ACCENT_COLOR = "#64B5F6";
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setBackgroundDrawable(new ColorDrawable(Color.parseColor(ACCENT_COLOR)));
        ((MainActivity) getActivity()).updateStatusBarColor(ACCENT_COLOR);
        ((MainActivity) getActivity()).updateStatusBarIconColor(true);

        Toolbar toolbar = getActivity().findViewById(R.id.action_bar);
        if (toolbar!= null){
            String COLOR_PRIMARY = "#192125";
            toolbar.setTitleTextColor(Color.parseColor(COLOR_PRIMARY));
        }
    }

}