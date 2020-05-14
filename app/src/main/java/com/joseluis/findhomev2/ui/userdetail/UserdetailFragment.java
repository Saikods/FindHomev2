package com.joseluis.findhomev2.ui.userdetail;


import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joseluis.findhomev2.MainActivity;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.pojo.User;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class UserdetailFragment extends Fragment {

    private UserdetailViewModel mViewModel;

    //---- Imagen de usuario
    private CircleImageView imgUser;
    private Button btnChangePhoto;
    private Button btnSave;

    //---- Datos de usuario
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtDate;
    private TextView txtEmail;
    private TextView txtDNI;
    private TextView txtProvince;
    private TextView txtTown;
    private TextView txtPostalCode;
    private EditText txtPhone;

    private Map<String, User> mapUsers;
    private ArrayList<User> users;


    //---- Permisos e imagen seleccionada por el usuario
    private String[] permisos;
    private final int RESULT_LOAD_IMAGE = 1;
    private Uri selectedImage;

    private MainActivity mainActivity;

    //---- Conexion al usuario actual de Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;

    //---- Acceso al arbol del usuarios
    private FirebaseDatabase database;
    private DatabaseReference refUser;



    //---- Acceso al Storage de Firebase
    private FirebaseStorage storage;
    private StorageReference stReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_userdetail, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        imgUser = root.findViewById(R.id.imgProfileUserSettings);
        btnChangePhoto = root.findViewById(R.id.btnChangePhotoSettings);
        btnSave = root.findViewById(R.id.btnSaveConfigSettings);
        txtFirstName = root.findViewById(R.id.txtFirstNameUserDetail);
        txtLastName = root.findViewById(R.id.txtLastNameUserDetail);
        txtPhone = root.findViewById(R.id.txtPhoneUserDetail);
        txtEmail = root.findViewById(R.id.txtEmailUserDetail);
        txtDate = root.findViewById(R.id.txtDateUserDetail);
        txtDNI = root.findViewById(R.id.txtDNIUserDetail);
        txtProvince= root.findViewById(R.id.txtProvinceUserDetail);
        txtTown = root.findViewById(R.id.txtTownUserDetail);
        txtPostalCode = root.findViewById(R.id.txtPostalCodeUserDetail);

        users = new ArrayList<>();

        storage = FirebaseStorage.getInstance();
        stReference = storage.getReferenceFromUrl("gs://findhome-d7197.appspot.com").child("userImages/"+user.getEmail());

        database = FirebaseDatabase.getInstance();
        refUser = database.getReference("users");


        imgUser.setImageDrawable(mainActivity.getImgHeader());

        if(user != null){

        }

        permisos = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), permisos, 1);

                if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }

            }
        });

        //Boton que guarda la imagen y la añade a la configuracion del usuario
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                mainActivity.setPhotoUserTemp(selectedImage);

                updateUI(user, getView());

                Navigation.findNavController(v).navigate(R.id.nav_rents);

            }
        });


        refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, User>> genericTypeIndicator = new GenericTypeIndicator<Map<String, User>>() {};

                mapUsers = dataSnapshot.getValue(genericTypeIndicator);

                if(mapUsers != null){
                    users.addAll(mapUsers.values());

                    for(int i = 0; i < users.size(); i++){
                        if(user.getEmail().equalsIgnoreCase(users.get(i).getEmail())){
                            User actualUser = users.get(i);

                            txtFirstName.setText(actualUser.getFirst_name());
                            txtLastName.setText(actualUser.getLast_name());
                            txtDate.setText(actualUser.getDate_birth());
                            txtPhone.setText(actualUser.getPhone());
                            txtEmail.setText(actualUser.getEmail());
                            txtDNI.setText(actualUser.getDni());
                            txtProvince.setText(actualUser.getProvince());
                            txtTown.setText(actualUser.getTown());
                            txtPostalCode.setText(String.valueOf(actualUser.getCp()));

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UserdetailViewModel.class);

    }

    private void updateUI(FirebaseUser currentUser, View view) {

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(selectedImage)
                .build();

            currentUser.updateProfile(profileUpdates);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    //metodo que nos permite subir la imagen como archivo .png
    public void uploadImage(){

        Bitmap bitmap = getBitmapFromView(imgUser);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = stReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fallo al subir la imagen, reinténtalo", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Éxito al subir la imagen.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Metodo que nos permite crear un Bitmap de la imagen seleccionada con un canvas.
    public Bitmap getBitmapFromView(ImageView image)
    {
        Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        image.draw(canvas);
        return bitmap;
    }


    //Cogemos la imagen que ha seleccionado el usuario y la guardamos
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data != null){
            selectedImage = data.getData();
            imgUser.setImageURI(selectedImage);


        }
    }



}
