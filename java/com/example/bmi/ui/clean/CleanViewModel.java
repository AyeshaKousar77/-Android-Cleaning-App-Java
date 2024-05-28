package com.example.bmi.ui.clean;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CleanViewModel  extends ViewModel {

    private final MutableLiveData<String> mText;

    public CleanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(" ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
