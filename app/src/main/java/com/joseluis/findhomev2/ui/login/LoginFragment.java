package com.joseluis.findhomev2.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joseluis.findhomev2.MainActivity;
import com.joseluis.findhomev2.R;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private TextView txtTitle;

    private EditText txtEmail;
    private EditText txtPassword;
    private Button btnDoLogin;

    private MainActivity mainActivity;


    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                ViewModelProviders.of(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        txtTitle = root.findViewById(R.id.txtLoginTitle);
        // Cambiar FontFamily con un .ttf (Fuente de fuera de Google Fonts)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtTitle.setTypeface(getResources().getFont(R.font.superclarendon));
        }

        txtEmail = root.findViewById(R.id.txtEmailLogin);
        txtPassword = root.findViewById(R.id.txtPasswordLogin);
        btnDoLogin = root.findViewById(R.id.btnDoLogin);

        auth = FirebaseAuth.getInstance();


        btnDoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()){
                    txtEmail.setError("Este campo no puede estar vacio");
                    txtPassword.setError("Este campo no puede estar vacio");
                }

                doLogin(txtEmail.getText().toString(), txtPassword.getText().toString());
            }
        });


        return root;
    }

    private void updateUI(FirebaseUser currentUser, View view) {
        //Lo usaré como toma de decisiones para abrir la ventana inicial

        if(currentUser != null){
            Navigation.findNavController(view).navigate(R.id.nav_rents);
        }

    }

    private void doLogin(final String email, final String password){

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            mainActivity.changeNavHeaderData(user);
                            mainActivity.setVNavSigninLogin(user);
                            mainActivity.setPhotoUser();

                            updateUI(user, getView());
                        }else{
                            Toast.makeText(getActivity(), "Error inesperado al iniciar sesion", Toast.LENGTH_SHORT).show();
                            updateUI(null,null);
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
}