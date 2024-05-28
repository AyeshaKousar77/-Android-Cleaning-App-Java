package com.example.bmi.ui.specific;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SpecificViewModel  extends ViewModel {

    private final MutableLiveData<String> mText;

    public SpecificViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(" ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
