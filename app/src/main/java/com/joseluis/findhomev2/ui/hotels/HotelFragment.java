package com.joseluis.findhomev2.ui.hotels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.adapters.HotelAdapter;
import com.joseluis.findhomev2.pojo.Hotel;

import java.util.ArrayList;
import java.util.Map;

public class HotelFragment extends Fragment {

    private HotelViewModel hotelViewModel;

    // DATOS
    private ArrayList<Hotel> hotels;
    private Map<String, Hotel> mapHotels;

    //RECYCLER VIEW
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HotelAdapter adapter;
    private final int RESOURCE = R.layout.hotels_card;


    //FIREBASE
    private DatabaseReference refHotels;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        hotelViewModel =
                ViewModelProviders.of(this).get(HotelViewModel.class);
        View root = inflater.inflate(R.layout.fragment_hotels, container, false);
        // final TextView textView = root.findViewById(R.id.text_slideshow);
        hotelViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        recyclerView = root.findViewById(R.id.recyclerViewHotels);
        hotels = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refHotels = database.getReference("hotels");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new HotelAdapter(getActivity(), RESOURCE, hotels);

        refHotels.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Hotel>> genericTypeIndicator = new GenericTypeIndicator<Map<String, Hotel>>() {};
                mapHotels = dataSnapshot.getValue(genericTypeIndicator);
                hotels.clear();


                if(mapHotels != null) {
                    hotels.addAll(mapHotels.values());
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