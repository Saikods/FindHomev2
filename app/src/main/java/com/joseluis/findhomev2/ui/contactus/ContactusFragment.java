package com.joseluis.findhomev2.ui.contactus;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.joseluis.findhomev2.R;

public class ContactusFragment extends Fragment {
    //private ScrollView constraintLayout;

    private ContactusViewModel mViewModel;

    private TextView txtTitle;
    private LinearLayout linearProduct;
    private LinearLayout linearAccount;

    //--- Spinner y datos del mismo
    private Spinner spinnerProduct;
    private ArrayAdapter adapter;

    private TextView txt;

    public static ContactusFragment newInstance() {
        return new ContactusFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contactus, container, false);

        //4 OPCIONES (?) -quiza mas opciones
        //1º: Informar sobre un producto (Hosteles-Alquileres-TopSitios)
        //2º: Informar de un error
        //3º: Información de cuenta (?) -quiza meterlo directamente en ajustes de usuario-
        //4º: Mandar un comentario acerca de la App -bueno o malo-
        //5º:
        txt = root.findViewById(R.id.txtInformContact);
        linearProduct = root.findViewById(R.id.linearBtnProductContact);
        linearAccount = root.findViewById(R.id.linearBtnAccountContact);
        txtTitle = root.findViewById(R.id.txtTitleContactus);
        spinnerProduct = root.findViewById(R.id.spinnerProductContact);

        changeTypeFace();

        String[] products = getResources().getStringArray(R.array.products);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, products);
        spinnerProduct.setAdapter(adapter);


        linearProduct.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(linearAccount.getBackground() == getResources().getDrawable(R.drawable.contact_linear_selected)){
                    linearAccount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_unselected));
                    linearProduct.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_selected));

                } else{
                    linearProduct.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_selected));
                    spinnerProduct.setVisibility(View.VISIBLE);
                }


            }
        });

        linearAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(linearProduct.getBackground() == getResources().getDrawable(R.drawable.contact_linear_selected)){
                    linearProduct.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_unselected));
                    linearAccount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_selected));
                    spinnerProduct.setVisibility(View.GONE);
                } else {
                    linearAccount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_selected));
                }
            }
        });


        return root;


        //CODIGO PARA CAPTAR LOS CLICKS EN LAYOUT(FUERA DE TEXTVIEWS/IMAGEVIEWS...ETC FUERA DE ELEMENTOS)
        //constraintLayout = root.findViewById(R.id.contactus);
//        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    linearLayout.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.contact_linear_unselected));
//                }
//
//                return true;
//            }
//        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ContactusViewModel.class);
        // TODO: Use the ViewModel
    }

    public void changeTypeFace(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            txtTitle.setTypeface(getResources().getFont(R.font.superclarendon));
        }
    }


}
