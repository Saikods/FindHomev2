package com.joseluis.findhomev2;

import android.Manifest;
import android.app.AlertDialog;
import android.app.usage.ExternalStorageStats;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joseluis.findhomev2.pojo.User;
import com.joseluis.findhomev2.ui.rents.RentFragment;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;

    //--- Acceso al usuario actual de Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;

    //--- Acceso a Storage de Firebase
    private FirebaseStorage storage;
    private StorageReference stRef;

    private CircleImageView img_user;
    private TextView txtUsername;
    private TextView txtEmail;
    private NavigationView navigationView;

    //Variables del Menu para cambiar visibilidad
    private Menu menuNav;
    private MenuItem nav_signIn;
    private MenuItem nav_logIn;
    private MenuItem nav_postrent;
    private MenuItem nav_userdetail;
    private MenuItem nav_contactus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setLogo(R.drawable.logofh);
        //Manera de centrar el icono en el medio de la toolbar
        //toolbar.setPadding(0, 0, 230, 0);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_rents, R.id.nav_postrent, R.id.nav_hotels,
                R.id.nav_tools, R.id.nav_login, R.id.nav_signin, R.id.nav_userdetail, R.id.nav_contactus)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //CODIGO PARA USAR COMPONENTES DEL NAV_HEADER_MAIN.xml
        View hView = navigationView.getHeaderView(0);
        txtUsername = hView.findViewById(R.id.txtUserName);
        txtEmail = hView.findViewById(R.id.txtEmailUser);
        img_user = hView.findViewById(R.id.imgProfileUserSettings);



        // COMO DESACTIVAR ELEMENTOS DEL MENU (PARA USUARIOS NO REGISTRADOS)
        menuNav = navigationView.getMenu();
        //MenuItem nav_postrents = menuNav.findItem(R.id.nav_postrent);
        //nav_postrents.setVisible(false);
        // -------------------------------------------------------------------


        user = auth.getCurrentUser();
        nav_signIn = menuNav.findItem(R.id.nav_signin);
        nav_logIn = menuNav.findItem(R.id.nav_login);
        nav_postrent = menuNav.findItem(R.id.nav_postrent);
        nav_userdetail = menuNav.findItem(R.id.nav_userdetail);
        nav_contactus = menuNav.findItem(R.id.nav_contactus);


        if(user != null){
           // Log.d("NAMEUSER", ""+user.getDisplayName());

            txtUsername.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
            if(user.getPhotoUrl() != null){
                setPhotoUser();
            }

            nav_signIn.setVisible(false);
            nav_logIn.setVisible(false);
            nav_postrent.setVisible(true);
            nav_userdetail.setVisible(true);
            nav_contactus.setVisible(true);


        }else {
            txtUsername.setText("¡Bienvenido a FindHome! ");
            txtEmail.setText("Puedes iniciar sesion o registrarte");
            nav_postrent.setVisible(false);
            nav_userdetail.setVisible(false);
            nav_contactus.setVisible(false);
            img_user.setImageResource(R.drawable.imgpref);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        user = auth.getCurrentUser();

        switch (item.getItemId()){
            case R.id.btnLogoutToolbar:
            if(user!=null){
                showLogout().show();
                restartNavHeaderData();
                setFVSigninLogin();
            }
            break;
        }

        return super.onOptionsItemSelected(item);

    }

    public void setPhotoUser() {
        final long ONE_MEGABYTE = 1024 * 1024;
        user = auth.getCurrentUser();
        stRef = storage.getReferenceFromUrl("gs://findhome-d7197.appspot.com").child("userImages/"+user.getEmail());
        stRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmapIMG = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if(user!= null) {
                    img_user.setImageBitmap(bitmapIMG);
                }
            }
        });
    }

    public void setPhotoUserTemp(Uri img){
        if(user != null){
            img_user.setImageURI(img);
        }
    }


    // Cambiar informacion del header con la info del User
    public void changeNavHeaderData(FirebaseUser user){

        if(user!=null) {
            txtUsername.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());
        }
    }
    //restablecer la informacion del header cuando se desloguea user
    public void restartNavHeaderData(){

        txtUsername.setText("¡Bienvenido a FindHome! ");
        txtEmail.setText("Puedes iniciar sesion o registrarte");
    }

    //mostar o no items del menu segun usuario logueado
    public void setVNavSigninLogin(FirebaseUser user){

        if(user != null){
            nav_logIn.setVisible(false);
            nav_signIn.setVisible(false);
            nav_postrent.setVisible(true);
            nav_userdetail.setVisible(true);
            nav_contactus.setVisible(true);

        }
    }

    //mostar o no items del menu segun usuario NO logueado
    public void setFVSigninLogin(){
        nav_logIn.setVisible(true);
        nav_signIn.setVisible(true);
        nav_postrent.setVisible(false);
        nav_userdetail.setVisible(false);
        nav_contactus.setVisible(false);
        img_user.setImageResource(R.drawable.imgpref);
    }

    //obtener la imagen actual del header
    public Drawable getImgHeader(){
        Drawable id_img = img_user.getDrawable();

        return id_img;
    }

    public AlertDialog showLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View txt = inflater.inflate(R.layout.alert_logout, null);
        TextView txtTerms = txt.findViewById(R.id.txtViewTermsAlert);

        builder.setView(txt);
        builder.setTitle("Cerrar sesión");
        builder.setIcon(R.drawable.ic_logout);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplication(), "Se ha cerrado sesion", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

}
