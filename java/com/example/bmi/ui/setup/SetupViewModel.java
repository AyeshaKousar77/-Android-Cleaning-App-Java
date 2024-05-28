package com.example.bmi.ui.setup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SetupViewModel  extends ViewModel {

    private final MutableLiveData<String> mText;

    public SetupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(" ");
        List<String> numbers = generateNumberList();
        numberList.setValue(numbers);
    }

    public LiveData<String> getText() {
        return mText;
    }
    private final MutableLiveData<List<String>> numberList = new MutableLiveData<>();

    public LiveData<List<String>> getNumberList() {
        return numberList;
    }

    private List<String> generateNumberList() {
        // Generate a list of numbers (e.g., 1 to 10)
        List<String> numbersList = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            numbersList.add(String.valueOf(i));
        }
        return numbersList;
    }
}
