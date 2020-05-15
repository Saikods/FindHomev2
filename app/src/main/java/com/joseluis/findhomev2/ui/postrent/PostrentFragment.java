package com.joseluis.findhomev2.ui.postrent;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.adapters.ImgPostrentAdapter;
import com.joseluis.findhomev2.pojo.Rents;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class PostrentFragment extends Fragment {

    private static final int PICK_IMAGE_MULTIPLE = 1;
    private PostrentViewModel postrentViewModel;

    private Button btnChooseImages;
    private Button btnPostrent;
    private ImageView imgZero;
    private TextView txtTitle;
    private EditText txtProvince;
    private EditText txtTown;
    private EditText txtStreetName;
    private EditText txtDescription;
    private EditText txtPrice;


    //--- Variables necesarias para guardar imagenes seleccionadas
    private ArrayList<String> imagesSelected;
    private List<Uri> mArrayUri;
    private String imageEncoded;
    private String urlImage;
    private List<String> urlImages;

    private String downloadUrl;


    //--- Cursores para 1 o más imagenes seleccionadas
    private Cursor cursor1Image;
    private Cursor cursorMult;

    //--- Array de permisos de lectura sobre galeria
    private String[] permisos;

    //--- Elementos necesarios para RecyclerView
    private RecyclerView recyclerView;
    private ImgPostrentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private final int RESOURCE = R.layout.img_postrent;

    //--- Referencias a Firebase
    private FirebaseDatabase database;
    private DatabaseReference refRents;
    private FirebaseAuth auth;
    private FirebaseUser user;

    Rents rent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postrentViewModel =
                ViewModelProviders.of(this).get(PostrentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_postrent, container, false);
        postrentViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });
        urlImages = new ArrayList<>();
        mArrayUri = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnChooseImages = root.findViewById(R.id.btnUploadPhotosPostrent);
        recyclerView = root.findViewById(R.id.recyclerImagesPostrent);
        imgZero = root.findViewById(R.id.imgZeroPostrent);
        txtTitle = root.findViewById(R.id.txtTitlePostrent);
        txtProvince = root.findViewById(R.id.txtProvincePostRent);
        txtTown = root.findViewById(R.id.txtTownPostrent);
        txtStreetName = root.findViewById(R.id.txtStreetNamePostrent);
        txtDescription = root.findViewById(R.id.txtDescriptionPostrent);
        txtPrice = root.findViewById(R.id.txtPricePostrent);
        btnPostrent = root.findViewById(R.id.btnPostrentPost);

        changeTypeFace();

        database = FirebaseDatabase.getInstance();
        refRents = database.getReference("rents");

        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ImgPostrentAdapter(getActivity(), RESOURCE, mArrayUri);

        recyclerView.setAdapter(adapter);

        rent = new Rents();


        permisos = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        btnChooseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(), permisos, 1);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGE_MULTIPLE);
                    }

                }
            }
        });

        btnPostrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseStorage storage = FirebaseStorage.getInstance();

                //final StorageReference imagesRef = storage.getReference().child("rentsPosted/");

                for (String imagen : rent.getImages()){
                    Log.d("IMAGENES", imagen);
                }



                //Rents rent = new Rents(txtStreetName.getText().toString(), txtProvince.getText().toString(), txtTown.getText().toString(), txtDescription.getText().toString(), 0, Integer.parseInt(txtPrice.getText().toString()), auxUri);

                rent.setDescription();

                refRents.child(user.getEmail()).push().setValue(rent);
                Navigation.findNavController(v).navigate(R.id.nav_rents);
            }
        });


        adapter.notifyDataSetChanged();


        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // Cuando hay imagen seleccionada
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
                // Cogemos la imagen de data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesSelected = new ArrayList<>();

                if (data.getData() != null) {         //Solo una imagen seleccionada

                    Uri mImageUri = data.getData();
                    mArrayUri.add(mImageUri);
                    adapter.notifyDataSetChanged();
                    imgZero.setVisibility(GONE);
                    recyclerView.setBackground(getResources().getDrawable(R.drawable.recycler_imgs));

                    if (getActivity() != null) {
                        // Obtenemos cursor
                        cursor1Image = getActivity().getContentResolver().query(mImageUri, filePathColumn, null, null, null);
                        // ovemos el cursor a la primera posicion (fila)
                        if (cursor1Image != null) {
                            cursor1Image.moveToFirst();

                            int columnIndex = cursor1Image.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor1Image.getString(columnIndex);
                            cursor1Image.close();

                        }

                    }

                } else {
                    //Multiples imagenes seleccionadas
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            imagesSelected.add(uri.toString());
                            // Obtenemos cursor
                            cursorMult = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Movemos el cursor a la primera posicion (fila)
                            cursorMult.moveToFirst();

                            int columnIndex = cursorMult.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursorMult.getString(columnIndex);
                            cursorMult.close();
                        }
                        Log.v("MainActivity", "Selected Images" + mArrayUri.size());
                        adapter.notifyDataSetChanged();
                        recyclerView.setBackground(getResources().getDrawable(R.drawable.recycler_imgs));
                    }
                }
            } else {
                Toast.makeText(getActivity(), "No has seleccionado imagenes", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Algo salió mal", Toast.LENGTH_LONG).show();
        }

        if (mArrayUri.size() > 0) {
            imgZero.setVisibility(GONE);
            uploadImages();
            Log.d("IMAGENES", "Tengo imagenes a subir");
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void changeTypeFace() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtTitle.setTypeface(getResources().getFont(R.font.superclarendon));
        }
    }

    int upload_count = 0;
    private void uploadImages() {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        final StorageReference imagesRef = storage.getReference().child("rentsPosted/");

        for( int upload_count = this.upload_count; upload_count < mArrayUri.size(); upload_count++){

            Uri individualImage = mArrayUri.get(upload_count);
            final StorageReference imageName = imagesRef.child("Image"+individualImage.getLastPathSegment());

            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           String urlImage = uri.toString();
                           urlImages.add(urlImage);

                           rent.getImages().add(urlImage);
                            Log.d("IMAGENES", "subida imagen: "+urlImage);
                       }
                   });
                }
            });
            this.upload_count = mArrayUri.size();
        }

    }

}

