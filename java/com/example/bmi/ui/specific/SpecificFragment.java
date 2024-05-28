package com.example.bmi.ui.specific;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bmi.R;
import com.example.bmi.database.SpecificSqlite;
import com.example.bmi.database.token;
import com.example.bmi.databinding.SpecificBinding;
import com.example.bmi.sign_in;

import java.util.Objects;

public class SpecificFragment extends Fragment {

    private SpecificBinding binding;
    private SpecificSqlite dbHelper;
    private token userToken;
    double baseCharge = 0.0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SpecificViewModel specificViewModel = new ViewModelProvider(this).get(SpecificViewModel.class);

        binding = SpecificBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new SpecificSqlite(getContext());
        userToken = new token(requireContext());

        final TextView textView = binding.textSpecific;
        specificViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Button confirmButton = root.findViewById(R.id.buttonConfirmBooking);
        confirmButton.setOnClickListener(v -> saveBooking());

        return root;
    }

    private void saveBooking() {
        String email = userToken.getUserEmail();
        if (email == null) {
            Intent intent = new Intent(requireContext(), sign_in.class);
            startActivity(intent);
            return;
        }

        CheckBox checkBoxKitchen = getView().findViewById(R.id.checkBoxKitchen);
        CheckBox checkBoxBathroom = getView().findViewById(R.id.checkBoxBathroom);
        CheckBox checkBoxCarpetCleaning = getView().findViewById(R.id.checkBoxCarpetCleaning);
        CheckBox checkBoxWindowWashing = getView().findViewById(R.id.checkBoxWindowWashing);

        int kitchenSelected = checkBoxKitchen.isChecked() ? 1 : 0;
        int bathroomSelected = checkBoxBathroom.isChecked() ? 1 : 0;
        int carpetCleaningSelected = checkBoxCarpetCleaning.isChecked() ? 1 : 0;
        int windowWashingSelected = checkBoxWindowWashing.isChecked() ? 1 : 0;

        RadioGroup radioGroupFrequency = getView().findViewById(R.id.radioGroupFrequency);
        int frequencyId = radioGroupFrequency.getCheckedRadioButtonId();
        if (frequencyId == -1) {
            // Handle case where no frequency is selected
            // You can show a toast message or any other form of user feedback
            return;
        }

        RadioButton selectedFrequencyButton = getView().findViewById(frequencyId);
        String frequency = selectedFrequencyButton.getText().toString();

        String specialInstructions = Objects.requireNonNull(binding.editText3.getText()).toString().trim();

        double totalCharges = calculateCharges(kitchenSelected, bathroomSelected,
                carpetCleaningSelected, windowWashingSelected, frequency);

        Log.d("SpecificFragment", "Saving booking: email=" + email + ", frequency=" + frequency + ", amount=" + totalCharges);

        dbHelper.addBooking(email, kitchenSelected, bathroomSelected, carpetCleaningSelected, windowWashingSelected, frequency, specialInstructions, totalCharges);

        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putFloat("amount",(float) totalCharges);
        navController.navigate(R.id.here_to_transaction, args);
    }

    private double calculateCharges(int kitchenSelected, int bathroomSelected,
                                    int carpetCleaningSelected, int windowWashingSelected,
                                    String frequency) {

        double kitchenCharge = 2000.0; // Example charge for kitchen
        double bathroomCharge = 1075.0; // Example charge for bathroom
        double carpetCleaningCharge = 2000.0; // Example charge for carpet cleaning
        double windowWashingCharge = 1000.0; // Example charge for window washing
        double frequencyCharge = 0.0;

        if (kitchenSelected == 1) {
            baseCharge += kitchenCharge;
        }
        if (bathroomSelected == 1) {
            baseCharge += bathroomCharge;
        }
        if (carpetCleaningSelected == 1) {
            baseCharge += carpetCleaningCharge;
        }
        if (windowWashingSelected == 1) {
            baseCharge += windowWashingCharge;
        }

        switch (frequency) {
            case "Weekly":
                frequencyCharge = baseCharge * 4.0;
                break;
            case "Bi-weekly":
                frequencyCharge = baseCharge * 2.0;
                break;
            case "Monthly":
                frequencyCharge = 0.0;
                break;
            default:
                break;
        }

        return baseCharge + frequencyCharge;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
