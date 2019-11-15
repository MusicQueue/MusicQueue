package com.example.musicqueue.ui.account;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.musicqueue.MainActivity;
import com.example.musicqueue.R;
import com.example.musicqueue.authentication.SignInActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        root = inflater.inflate(R.layout.fragment_account, container, false);

        setColors();

        setAccount();

        root.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        root.findViewById(R.id.advanced_settings_text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
            }
        });

        return root;
    }

    /**
     * signOut signs the user's account out using Firebase and using
     * Google account sign out; called when user presses sign out button
     */
    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();

        // Google sign out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut();

        // navigate back to SignIn Activity
        startActivity(new Intent(getActivity(), SignInActivity.class));
        getActivity().finish();
    }

    /**
     * setColors sets the colors for the action bar, status bar, status bar icons,
     * and the toolbar
     */
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

    /**
     * setAccount sets the user's display name and email in the Account layout
     */
    private void setAccount() {
        TextView nameTV = root.findViewById(R.id.account_name);
        nameTV.setText(firebaseUser.getDisplayName().toString());

        TextView emailTV = root.findViewById(R.id.account_email);
        emailTV.setText(firebaseUser.getEmail().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        setAccount();
    }

}