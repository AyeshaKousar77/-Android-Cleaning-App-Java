package com.example.bmi.ui.paint;

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
import com.example.bmi.database.PaintSqlite;
import com.example.bmi.database.token;
import com.example.bmi.databinding.PaintPageBinding;
import com.example.bmi.sign_in;

public class PaintFragment extends Fragment {

    private PaintPageBinding binding;
    private PaintSqlite dbHelper;
    private token userToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PaintViewModel paintViewModel = new ViewModelProvider(this).get(PaintViewModel.class);

        binding = PaintPageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new PaintSqlite(getContext());
        userToken = new token(requireContext());

        final TextView textView = binding.textPaint;
        paintViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        Spinner colorSpinner = root.findViewById(R.id.color_selection_spinner);
        Spinner roomSpinner = root.findViewById(R.id.room_selection_spinner);

        paintViewModel.getColorList().observe(getViewLifecycleOwner(), colorList -> {
            ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, colorList);
            colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            colorSpinner.setAdapter(colorAdapter);
        });

        paintViewModel.getRoomList().observe(getViewLifecycleOwner(), roomList -> {
            ArrayAdapter<String> roomAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, roomList);
            roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roomSpinner.setAdapter(roomAdapter);
        });

        Button proceedButton = root.findViewById(R.id.button_proceed_painting);
        proceedButton.setOnClickListener(v -> savePaintingService());

        return root;
    }

    private void savePaintingService() {
        String email = userToken.getUserEmail();
        if (email == null) {
            Intent intent = new Intent(requireContext(), sign_in.class);
            startActivity(intent);
            return;
        }

        Spinner colorSpinner = requireView().findViewById(R.id.color_selection_spinner);
        String color = colorSpinner.getSelectedItem().toString();

        Spinner roomSpinner = getView().findViewById(R.id.room_selection_spinner);
        String room = roomSpinner.getSelectedItem().toString();

        RadioGroup serviceGroup = getView().findViewById(R.id.painting_service_options);
        int selectedServiceId = serviceGroup.getCheckedRadioButtonId();
        RadioButton selectedServiceButton = getView().findViewById(selectedServiceId);
        String serviceType = selectedServiceButton.getText().toString();

        double sum = 0.0;
        if (serviceType.equals(getString(R.string.Interior_Painting))) {
            sum += 300.0;
        } else if (serviceType.equals(getString(R.string.Exterior_Painting))) {
            sum += 500.0;
        } else if (serviceType.equals(getString(R.string.Both))) {
            sum += 700.0;
        }

        CheckBox checkboxWallPreparation = getView().findViewById(R.id.wall_preparation_checkbox);
        CheckBox checkboxWallpaperRemoval = getView().findViewById(R.id.wallpaper_removal_checkbox);
        CheckBox checkboxTrimPainting = getView().findViewById(R.id.trim_painting_checkbox);

        if (checkboxWallPreparation.isChecked()) sum += 50;
        if (checkboxWallpaperRemoval.isChecked()) sum += 100;
        if (checkboxTrimPainting.isChecked()) sum += 150;

        Log.d("PaintFragment", "Saving painting service: email=" + email + ", color=" + color + ", room=" + room + ", serviceType=" + serviceType + ", amount=" + sum);

        dbHelper.addPaintingService(email, color, room, serviceType, sum);

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
