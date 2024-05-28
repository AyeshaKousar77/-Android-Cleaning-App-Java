package com.example.bmi.ui.clean;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bmi.R;
import com.example.bmi.database.CleanSqlite;
import com.example.bmi.database.token;
import com.example.bmi.databinding.CleanPageBinding;
import com.example.bmi.sign_in;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class CleanFragment extends Fragment {

    private CleanPageBinding binding;
    private CleanSqlite dbHelper;
    private token userToken;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CleanViewModel cleanViewModel = new ViewModelProvider(this).get(CleanViewModel.class);

        binding = CleanPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new CleanSqlite(getContext());
        userToken = new token(requireContext());

        final TextView textView = binding.textClean;
        cleanViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button proceedButton = root.findViewById(R.id.button12);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCleaningService();
            }
        });
        return root;
    }

    private void saveCleaningService() {
        String email = userToken.getUserEmail();
        if (email == null) {
            Intent intent = new Intent(requireContext(), sign_in.class);
            startActivity(intent);
            return; // Ensure the method exits if the user is not signed in
        }

        RadioGroup typeGroup = getView().findViewById(R.id.choose);
        int selectedTypeId = typeGroup.getCheckedRadioButtonId();
        RadioButton selectedTypeButton = getView().findViewById(selectedTypeId);
        String type = selectedTypeButton.getText().toString();

        double sum = 0.0;
        if (type.equals(getString(R.string.Initial))) {
            sum += 500.0;
        } else if (type.equals(getString(R.string.Deep))) {
            sum += 1000;
        }

        TextInputEditText sizeField = getView().findViewById(R.id.size_of_apartment_field);
        int size = 0;
        try {
            size = Integer.parseInt(Objects.requireNonNull(sizeField.getText()).toString());
        } catch (NumberFormatException e) {
            sizeField.setError("Invalid size");
            Log.e("CleanFragment", "Invalid size input");
            return;
        }
        sum += size * 1000;

        TextInputEditText roomsField = getView().findViewById(R.id.number_of_rooms_field);
        int rooms = 0;
        try {
            rooms = Integer.parseInt(Objects.requireNonNull(roomsField.getText()).toString());
        } catch (NumberFormatException e) {
            roomsField.setError("Invalid number of rooms");
            Log.e("CleanFragment", "Invalid rooms input");
            return;
        }
        sum += rooms * 300;

        RadioGroup timeGroup = getView().findViewById(R.id.radioGroup);
        int selectedTimeId = timeGroup.getCheckedRadioButtonId();
        RadioButton selectedTimeButton = getView().findViewById(selectedTimeId);
        String time = selectedTimeButton.getText().toString();

        RadioGroup frequencyGroup = getView().findViewById(R.id.radioGroup1);
        int selectedFrequencyId = frequencyGroup.getCheckedRadioButtonId();
        RadioButton selectedFrequencyButton = getView().findViewById(selectedFrequencyId);
        String frequency = selectedFrequencyButton.getText().toString();

        Log.d("CleanFragment", "Saving cleaning service: email=" + email + ", type=" + type + ", size=" + size + ", rooms=" + rooms + ", time=" + time + ", frequency=" + frequency + ", amount=" + sum);

        dbHelper.addCleaningService(email, type, size, rooms, time, frequency, sum);

        // Use NavController to navigate to FragmentTransaction
        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putFloat("amount", (float) sum);
        navController.navigate(R.id.here_to_transaction, args);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
