package com.joseluis.findhomev2.ui.rents;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joseluis.findhomev2.MainActivity;
import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.adapters.RentAdapter;
import com.joseluis.findhomev2.pojo.Rents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RentViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    private DatabaseReference refRent;
    private RecyclerView recyclerView;

    public RentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is rent fragment");




//        refRent.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//

    }

    public LiveData<String> getText() {
        return mText;
    }
}