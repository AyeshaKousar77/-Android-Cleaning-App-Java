package com.example.bmi.ui.car;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bmi.R;
import com.example.bmi.database.CarSqlite;
import com.example.bmi.database.token;
import com.example.bmi.databinding.CarWashBinding;
import com.example.bmi.sign_in;

public class CarFragment extends Fragment {

    private CarWashBinding binding;
    private CarSqlite dbHelper;
    private token userToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CarViewModel carViewModel = new ViewModelProvider(this).get(CarViewModel.class);

        binding = CarWashBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new CarSqlite(requireContext());
        userToken = new token(requireContext());

        carViewModel.getText().observe(getViewLifecycleOwner(), binding.textCar::setText);

        carViewModel.getVehicleTypes().observe(getViewLifecycleOwner(), vehicleTypes -> {
            ArrayAdapter<String> vehicleTypeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, vehicleTypes);
            vehicleTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerVehicleType.setAdapter(vehicleTypeAdapter);
        });

        binding.buttonPayNow.setOnClickListener(v -> saveCarService());

        return root;
    }

    private void saveCarService() {
        String email = userToken.getUserEmail();
        if (email == null) {
            Intent intent = new Intent(requireContext(), sign_in.class);
            startActivity(intent);
            return; // Ensure the method exits if the user is not signed in
        }

        String vehicleType = binding.spinnerVehicleType.getSelectedItem().toString();

        RadioGroup washOptionsGroup = binding.radioGroupWashOptions;
        int selectedWashOptionId = washOptionsGroup.getCheckedRadioButtonId();
        RadioButton selectedWashOptionButton = requireView().findViewById(selectedWashOptionId);
        String washOption = selectedWashOptionButton.getText().toString();

        double cost = calculateCost(vehicleType, washOption);

        RadioGroup timeOptionsGroup = binding.radioGroupTimeOptions;
        int selectedTimeOptionId = timeOptionsGroup.getCheckedRadioButtonId();
        RadioButton selectedTimeOptionButton = requireView().findViewById(selectedTimeOptionId);
        String timeOption = selectedTimeOptionButton.getText().toString();

        Log.d("CarFragment", "Saving car service: email=" + email + ", vehicleType=" + vehicleType + ", washOption=" + washOption + ", timeOption=" + timeOption + ", amount=" + cost);

        dbHelper.addCarService(email, vehicleType, washOption, timeOption, cost);

        // Use NavController to navigate to FragmentTransaction
        navigateToPaymentFragment((float) cost);
    }

    private double calculateCost(String vehicleType, String washOption) {
        double baseCost;

        switch (vehicleType) {
            case "Compact Car":
                baseCost = 200.0;
                break;
            case "Sedan":
                baseCost = 300.0;
                break;
            case "Truck":
                baseCost = 1000.0;
                break;
            default:
                baseCost = 500.0;
                break;
        }

        switch (washOption) {
            case "Simple Cleaning":
                return baseCost + 1000.0;
            case "Additional Service":
                return baseCost + 2000.0;
            case "Interior Cleaning":
                return baseCost + 4000.0;
            case "Exterior Cleaning":
                return baseCost + 3000.0;
            default:
                return baseCost;
        }
    }

    private void navigateToPaymentFragment(float cost) {
        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putFloat("amount", (float)cost);
        navController.navigate(R.id.here_to_transaction, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
