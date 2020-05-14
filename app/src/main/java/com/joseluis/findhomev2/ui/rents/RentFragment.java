package com.joseluis.findhomev2.ui.rents;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.joseluis.findhomev2.MainActivity;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.adapters.RentAdapter;
import com.joseluis.findhomev2.pojo.Rents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RentFragment extends Fragment {

    private RentViewModel rentViewModel;
    private Button btnLogout;


    private Map<String, Rents> mapRents;
    private ArrayList<Rents> rents;
    //private ArrayList<String> indices;

    private RecyclerView recyclerView;
    private RentAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    private final int RESOURCE = R.layout.rents_card;

    private DatabaseReference refRent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rentViewModel =
                ViewModelProviders.of(this).get(RentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rent, container, false);
       // final TextView textView = root.findViewById(R.id.text_rent);
        rentViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
              //  textView.setText(s);
            }
        });

        // ----- RECOMENDACION DE COLEGUITA NANUKI: PONER BUTTON A LA CARD PRINCIPAL PARA AÑADIR A FAVORITOS
        // (PARA VISUALIZARLO MAS TARDE O SIMPLEMENTE GUARDARLO)

        recyclerView = root.findViewById(R.id.recyclerViewRent);

        rents = new ArrayList<>();
        //indices = new ArrayList<>();

        FirebaseDatabase database= FirebaseDatabase.getInstance();
        refRent = database.getReference("rents");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RentAdapter(getActivity(), RESOURCE, rents);


        refRent.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Rents>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Rents>>() {};
                mapRents = dataSnapshot.getValue(genericTypeIndicator);
                rents.clear();

                if(mapRents != null){
                    rents.addAll(mapRents.values());
                    Log.d("RENTSARRAY", "TAMAÑO: " + rents.size());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(adapter);


        return root;
    }
}