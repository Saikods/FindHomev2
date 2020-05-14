package com.joseluis.findhomev2.ui.sigin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.pojo.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SigninFragment extends Fragment {

    private SigninViewModel signinViewModel;

    private TextView txtTitle;
    private TextView txtSubtitle;
    private TextView txtDate;
    private TextView txtTerms;

    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtRepeatPassword;
    private EditText txtUsername;
    private EditText txtFirstName;
    private EditText txtLastName;
    private EditText txtDNI;
    private EditText txtTelephone;
    private EditText txtTown;
    private EditText txtPostalCode;

    private Button btnSignIn;

    //DatePickerDialog (Para selector de fecha de nacimiento)
    private Button btnDate;
    private DatePickerDialog.OnDateSetListener mDateListener;


    //Spinner de provincias
    private ArrayAdapter<String> adapterPronvices;
    private Spinner spinnerP;

    //Authentication FireBase
    private FirebaseAuth auth;

    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signinViewModel =
                ViewModelProviders.of(this).get(SigninViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signin, container, false);

        auth = FirebaseAuth.getInstance();

        // CODIGO PARA AÑADIR FUENTE EXTERNA A GOOGLE FONTS AL TEXT VIEW
        txtTitle = root.findViewById(R.id.txtTitleSignIn);
        txtSubtitle = root.findViewById(R.id.txtSubtitleSignIn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtTitle.setTypeface(getResources().getFont(R.font.superclarendon));
            txtSubtitle.setTypeface(getResources().getFont(R.font.superclarendon));
        }
        txtDate = root.findViewById(R.id.txtDateViewSignIn);
        spinnerP = root.findViewById(R.id.spinnerProvinces);
        txtTerms = root.findViewById(R.id.txtTermsSignIn);

        txtEmail = root.findViewById(R.id.txtEmailSignIn);
        txtPassword = root.findViewById(R.id.txtPasswordSigIn);
        txtRepeatPassword = root.findViewById(R.id.txtRepeatPasswordSignIn);
        txtUsername = root.findViewById(R.id.txtUserNameSignIn);
        txtFirstName = root.findViewById(R.id.txtFirstNameSignIn);
        txtLastName = root.findViewById(R.id.txtLastNameSignIn);
        txtDNI = root.findViewById(R.id.txtDniSignIn);
        txtTelephone = root.findViewById(R.id.txtTelephoneSignIn);
        txtTown = root.findViewById(R.id.txtTownSignIn);
        txtPostalCode = root.findViewById(R.id.txtPostalCodeSignIn);
        btnSignIn = root.findViewById(R.id.btnSignIn);


        //Llamada a Alert View cuando le demos click en "terminos y condiciones"
        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTerms().show();
            }
        });


        // Realizar registro mediante boton
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference refUser = database.getReference("users");

                User user = new User(txtDNI.getText().toString(), txtFirstName.getText().toString(), txtLastName.getText().toString(),
                        txtDate.getText().toString(), txtTelephone.getText().toString(), Integer.parseInt(txtPostalCode.getText().toString()),
                        String.valueOf(spinnerP.getSelectedItem()), txtTown.getText().toString(), txtUsername.getText().toString(),
                        txtPassword.getText().toString(), txtEmail.getText().toString());

                refUser.child(txtUsername.getText().toString()).setValue(user);

                doSignIn(txtEmail.getText().toString(), txtPassword.getText().toString(), getView());
                Log.d("USER", user.getDni()+user.getEmail());

            }
        });


        // Codigo para crear DatePicker y coger fecha seleccionada
        btnDate = root.findViewById(R.id.btnDateSignIn);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    //CREAMOS NUESTRO DATEPICKERDIALOG PARA QUE SALGA EN UNA VENTANA EMERGENTE
                    //SI LE PASAMOS UN R.STYLE AL CONSTRUCTOR DESPUES DE GETACTIVITY(),
                    //SE TRANSFORMARÁ AUTOMATICAMENTE EN EL MODO CALENDARIO EN VEZ DE MODO SPINNER
                    final DatePickerDialog dialog =new DatePickerDialog(getActivity(), mDateListener, year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                    dialog.show();

                    dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            txtDate.setText(dayOfMonth+"/"+month+"/"+year);
                        }
                    });
                }
            }
        });

        //Llenamos el Spinner de Provincias con las provincias que se encuentran en strings.xml
        String[] provinces = getResources().getStringArray(R.array.provinces);
        adapterPronvices = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, provinces);
        spinnerP.setAdapter(adapterPronvices);



        return root;

    }


    private void doSignIn(String email, String password, final View view){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    FirebaseUser user = auth.getCurrentUser();

                    updateUI(user, getView());
                    FirebaseAuth.getInstance().signOut();
                    Navigation.findNavController(view).navigate(R.id.nav_rents);
                    Toast.makeText(getActivity(), "Se ha creado el usuario con éxito", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Ha ocurrido un error con el registro",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null, null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser currentUser, View view) {
        //Lo usaré como toma de decisiones para abrir la ventana inicial
        String completeName = txtFirstName.getText().toString() +" "+ txtLastName.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(completeName)
                .build();


        if (currentUser != null) {
            currentUser.updateProfile(profileUpdates);
        }
    }

    //CODIGO PARA HACER VISIBLE LA CONTRASEÑA DEL TXTPASSWORD : FALTA MEJORAR Y CAMBIAR COSAS

//        txtPassword.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                switch (event.getAction()){
////                    case MotionEvent.ACTION_DOWN:
////                        txtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
////                        break;
////                        case MotionEvent.ACTION_UP:
////                            txtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
////                            break;
////                }
////                return true;
////            }
////        });

    private AlertDialog showTerms(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View txt = inflater.inflate(R.layout.alert_terms, null);
        TextView txtTerms = txt.findViewById(R.id.txtViewTermsAlert);

        builder.setView(txt);
        builder.setTitle("Términos y Condiciones");
        builder.setIcon(R.drawable.ic_terms);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }


}