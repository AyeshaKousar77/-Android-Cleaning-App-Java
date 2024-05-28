package com.example.bmi.ui.setup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bmi.R;
import com.example.bmi.database.KitchenCleanSqlite;
import com.example.bmi.database.token;
import com.example.bmi.databinding.KitchenSetBinding;
import com.example.bmi.sign_in;

public class SetupFragment extends Fragment {

    private KitchenSetBinding binding;
    private KitchenCleanSqlite dbHelper;
    private token userToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SetupViewModel slideshowViewModel = new ViewModelProvider(this).get(SetupViewModel.class);

        binding = KitchenSetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new KitchenCleanSqlite(getContext());
        userToken = new token(requireContext());

        final TextView textView = binding.textKitchen;
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Spinner numberSpinner = root.findViewById(R.id.numberSpinner);
        slideshowViewModel.getNumberList().observe(getViewLifecycleOwner(), numbersList -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, numbersList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            numberSpinner.setAdapter(adapter);
        });

        Button proceedButton = root.findViewById(R.id.buttonKit);
        proceedButton.setOnClickListener(v -> saveKitchenCleaningService());
        return root;
    }

    private void saveKitchenCleaningService() {
        String email = userToken.getUserEmail();
        if (email == null) {
            Intent intent = new Intent(requireContext(), sign_in.class);
            startActivity(intent);
            return;
        }

        Spinner numberSpinner = getView().findViewById(R.id.numberSpinner);
        String size = numberSpinner.getSelectedItem().toString();

        RadioGroup typeGroup = getView().findViewById(R.id.radioGroup);
        int selectedTypeId = typeGroup.getCheckedRadioButtonId();
        RadioButton selectedTypeButton = getView().findViewById(selectedTypeId);
        String type = selectedTypeButton.getText().toString();

        double sum = 0.0;
        if (type.equals(getString(R.string.k_op1))) {
            sum += 2000.0;
        } else if (type.equals(getString(R.string.k_op2))) {
            sum += 4000.0;
        }

        CheckBox checkboxCountertops = getView().findViewById(R.id.checkbox_countertops);
        CheckBox checkboxAppliances = getView().findViewById(R.id.checkbox_appliances);
        CheckBox checkboxDeepCleaning = getView().findViewById(R.id.checkbox_deep_cleaning);
        CheckBox checkboxOrganizing = getView().findViewById(R.id.checkbox_organizing);

        if (checkboxCountertops.isChecked()) sum += 5000;
        if (checkboxAppliances.isChecked()) sum += 1000;
        if (checkboxDeepCleaning.isChecked()) sum += 1500;
        if (checkboxOrganizing.isChecked()) sum += 2000;

        Log.d("SetupFragment", "Saving kitchen cleaning service: email=" + email + ", size=" + size + ", type=" + type + ", amount=" + sum);

        dbHelper.addKitchenCleaningService(email, size, type, sum);

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
