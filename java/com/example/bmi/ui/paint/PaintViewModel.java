package com.example.bmi.ui.paint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

public class PaintViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<String>> colorList;
    private final MutableLiveData<List<String>> roomList;

    public PaintViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is paint fragment");

        colorList = new MutableLiveData<>();
        colorList.setValue(Arrays.asList("Red", "Blue", "Green", "Yellow"));  // Example data

        roomList = new MutableLiveData<>();
        roomList.setValue(Arrays.asList("Living Room", "Bedroom", "Kitchen", "Bathroom"));  // Example data
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<String>> getColorList() {
        return colorList;
    }

    public LiveData<List<String>> getRoomList() {
        return roomList;
    }
}
