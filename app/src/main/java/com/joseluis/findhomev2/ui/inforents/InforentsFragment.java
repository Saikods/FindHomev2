package com.joseluis.findhomev2.ui.inforents;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.adapters.ImgRentAdapter;
import com.joseluis.findhomev2.pojo.Rents;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class InforentsFragment extends Fragment {

    private InforentsViewModel mViewModel;
    public static InforentsFragment newInstance() {
        return new InforentsFragment();
    }


    // DATOS DE ALQUILERES
    private ArrayList<Rents> rents;
    private ArrayList<String> imgRents;

    // SLIDER DE IMAGENES
    private ViewPager viewPager;
    private ImgRentAdapter adapter;
    private CircleIndicator indicator;

    private FloatingActionButton fab;

    //ATRIBUTOS RENT/USER
    private ImageView imageUser;
    private TextView txtPrice;
    private RatingBar rbRent;
    private TextView txtStreetName;
    
    private Button btnContactUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_inforents, container, false);

//        fab = root.findViewById(R.id.fabInfoRents);
//        fab.setOnClickListener(new View.OnClickListener() {
//         @Override
//            public void onClick(View v) {
//             Toast.makeText(getActivity(), "Añadido a FAVORITOS", Toast.LENGTH_SHORT).show();
//            }
//        });

        // DATOS(ARRAYLIST)
        imgRents = new ArrayList<>();
        rents = new ArrayList<>();

        //SLIDER DE IMAGENES
        indicator = root.findViewById(R.id.imgIndicator);
        viewPager = root.findViewById(R.id.viewPagerRent);
        adapter = new ImgRentAdapter(getActivity(), imgRents);
        viewPager.setAdapter(adapter);
        // ---------------------------------------------------

        //ELEMENTOS RENT/USER
        imageUser = root.findViewById(R.id.imgUserInfoRent);
        imageUser.setImageResource(R.drawable.alquileres);

        txtPrice = root.findViewById(R.id.txtPriceInfoRents);
        rbRent = root.findViewById(R.id.rBRatingInfoRents);
        txtStreetName = root.findViewById(R.id.txtStreetNameInfoRents);

        
        btnContactUser = root.findViewById(R.id.btnContactInfoRents);
        btnContactUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Probando Boton", Toast.LENGTH_SHORT).show();
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InforentsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){
            final Rents rent = getArguments().getParcelable("rent");

            if(rent != null){
                imgRents.addAll(rent.getImages());

                Log.d("IMAGENES", " "+imgRents.size());

                txtPrice.setText(String.valueOf(rent.getPrice()));
                rbRent.setRating(rent.getRating());
                txtStreetName.setText(rent.getStreet_name());

                indicator.setViewPager(viewPager);
                adapter.notifyDataSetChanged();



            }

        }

    }

}

