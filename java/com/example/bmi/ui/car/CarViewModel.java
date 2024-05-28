package com.example.bmi.ui.car;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Arrays;
import java.util.List;

public class CarViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<String>> vehicleTypes;
    private final MutableLiveData<List<String>> washOptions;
    private final MutableLiveData<List<String>> timeOptions;

    public CarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is car fragment");

        vehicleTypes = new MutableLiveData<>();
        vehicleTypes.setValue(Arrays.asList("Sedan", "SUV", "Truck", "Coupe")); // Example data

        washOptions = new MutableLiveData<>();
        washOptions.setValue(Arrays.asList("Simple Cleaning", "Additional Service", "Interior Cleaning", "Exterior Cleaning")); // Example data

        timeOptions = new MutableLiveData<>();
        timeOptions.setValue(Arrays.asList("Morning", "Afternoon", "Evening")); // Example data
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<String>> getVehicleTypes() {
        return vehicleTypes;
    }

    public LiveData<List<String>> getWashOptions() {
        return washOptions;
    }

    public LiveData<List<String>> getTimeOptions() {
        return timeOptions;
    }
}
