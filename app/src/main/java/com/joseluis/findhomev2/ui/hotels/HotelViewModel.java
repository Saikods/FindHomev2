package com.joseluis.findhomev2.ui.hotels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HotelViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HotelViewModel() {
        mText = new MutableLiveData<>();
         mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}