package com.example.bmi.ui.you;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bmi.R;
import com.example.bmi.database.sqlite;
import com.example.bmi.database.token;
import com.example.bmi.database.UserProfile;
import com.example.bmi.database.MapActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class YouFragment extends Fragment {

    private TextView firstNameTextView, lastNameTextView, phoneTextView, emailTextView, passwordTextView,
            locationTextView, bankNameTextView, accountNoTextView;
    private EditText firstNameEditText, lastNameEditText, phoneEditText, emailEditText, passwordEditText,
            locationEditText, accountNoEditText;
    private Spinner bankNameSpinner;
    private Button editButton;

    private boolean isEditing = false;
    private sqlite mdb;
    private token session;
    private UserProfile userProfile;
    private static final int REQUEST_CODE_MAP_ACTIVITY = 101;
    private final ActivityResultLauncher<Intent> mapActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    double latitude = data.getDoubleExtra("latitude", 0.0);
                    double longitude = data.getDoubleExtra("longitude", 0.0);
                    locationEditText.setText(String.format(Locale.getDefault(), "%.6f, %.6f", latitude, longitude));
                }
            }
    );
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);

        // Initialize views
        firstNameTextView = view.findViewById(R.id.vieW1);
        lastNameTextView = view.findViewById(R.id.vieW2);
        phoneTextView = view.findViewById(R.id.vieW3);
        emailTextView = view.findViewById(R.id.vieW4);
        passwordTextView = view.findViewById(R.id.vieW5);
        bankNameTextView = view.findViewById(R.id.vieW6);
        accountNoTextView = view.findViewById(R.id.vieW7);
        locationTextView  = view.findViewById(R.id.loci);

        firstNameEditText = view.findViewById(R.id.ttt1);
        lastNameEditText = view.findViewById(R.id.ttt2);
        phoneEditText = view.findViewById(R.id.ttt3);
        emailEditText = view.findViewById(R.id.ttt4);
        passwordEditText = view.findViewById(R.id.ttt5);
        bankNameSpinner = view.findViewById(R.id.bankNameSpinner);
        accountNoEditText = view.findViewById(R.id.ttt7);
        locationEditText = view.findViewById(R.id.loc);
        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditing) {
                    // Open MapActivity to select location
                    Intent intent = new Intent(getContext(), MapActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_MAP_ACTIVITY);
                }
            }
        });

        editButton = view.findViewById(R.id.editButton);

        // Set onClickListener for Edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleEditing();
            }
        });

        // Initialize database and session management
        mdb = new sqlite(getContext());
        session = new token(getContext());

        // Set initial values after the view has been created
        setProfileData();

        return view;
    }

    private void setProfileData() {
        String email = session.getUserEmail();
        if (email != null) {
            userProfile = mdb.getUserProfile(email);
            if (userProfile != null) {
                firstNameTextView.setText(userProfile.getFirstName());
                lastNameTextView.setText(userProfile.getLastName());
                phoneTextView.setText(userProfile.getPhone());
                emailTextView.setText(userProfile.getEmail());
                passwordTextView.setText(userProfile.getPassword());
                locationTextView.setText(String.valueOf(userProfile.getLocation()));
                bankNameTextView.setText(userProfile.getBankName());
                accountNoTextView.setText(userProfile.getAccountNo());

                firstNameEditText.setText(userProfile.getFirstName());
                lastNameEditText.setText(userProfile.getLastName());
                phoneEditText.setText(userProfile.getPhone());
                emailEditText.setText(userProfile.getEmail());
                passwordEditText.setText(userProfile.getPassword());
                locationEditText.setText(String.valueOf(userProfile.getLocation()));
                accountNoEditText.setText(userProfile.getAccountNo());

                List<String> banks = getBanksList();
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, banks);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bankNameSpinner.setAdapter(adapter);
                setSpinnerSelection(bankNameSpinner, userProfile.getBankName());

                setEditableFields(isEditing);
            }
        }
    }

    private List<String> getBanksList() {
        List<String> banks = new ArrayList<>();
        banks.add("National Bank of Pakistan (NBP)");
        banks.add("Habib Bank Limited (HBL)");
        banks.add("MCB Bank Limited");
        banks.add("Allied Bank Limited (ABL)");
        banks.add("Askari Bank Limited");
        banks.add("Bank Alfalah Limited");
        banks.add("Faysal Bank Limited");
        banks.add("Bank Islami Pakistan Limited");
        banks.add("Meezan Bank Limited");
        banks.add("United Bank Limited (UBL)");
        return banks;
    }

    private void setEditableFields(boolean isEditable) {
        int visibility = isEditable ? View.VISIBLE : View.GONE;

        firstNameTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        lastNameTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        phoneTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        emailTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        locationTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        bankNameTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        accountNoTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);
        passwordTextView.setVisibility(isEditable ? View.GONE : View.VISIBLE);

        firstNameEditText.setVisibility(visibility);
        lastNameEditText.setVisibility(visibility);
        phoneEditText.setVisibility(visibility);
        emailEditText.setVisibility(visibility);
        locationEditText.setVisibility(visibility);
        bankNameSpinner.setVisibility(visibility);
        accountNoEditText.setVisibility(visibility);
        passwordEditText.setVisibility(visibility);

        editButton.setText(isEditable ? "Save" : "Edit");
    }

    private void toggleEditing() {
        if (isEditing) {
            // Save changes to database
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String pass = passwordEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String bankName = bankNameSpinner.getSelectedItem().toString();
            String accountNo = accountNoEditText.getText().toString();

            // Update user profile in UI
            firstNameTextView.setText(firstName);
            lastNameTextView.setText(lastName);
            phoneTextView.setText(phone);
            emailTextView.setText(email);
            passwordTextView.setText(pass);
            locationTextView.setText(location);
            bankNameTextView.setText(bankName);
            accountNoTextView.setText(accountNo);

            // Reset editing state
            isEditing = false;
            setEditableFields(false);
        } else {
            // Set editing mode
            isEditing = true;
            setEditableFields(true);

            // Open MapActivity to select location
            Intent intent = new Intent(getContext(), MapActivity.class);
            mapActivityResultLauncher.launch(intent);
            // Update the location field with the current value
            intent.putExtra("location", locationTextView.getText().toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if there's updated data from MapActivity
        if (getActivity().getIntent() != null && getActivity().getIntent().hasExtra("firstName")) {
            // Retrieve the updated data from the intent
            String firstName = getActivity().getIntent().getStringExtra("firstName");
            String lastName = getActivity().getIntent().getStringExtra("lastName");
            String phone = getActivity().getIntent().getStringExtra("phone");
            String email = getActivity().getIntent().getStringExtra("email");
            String pass = getActivity().getIntent().getStringExtra("password");
            String location = getActivity().getIntent().getStringExtra("location");
            String bankName = getActivity().getIntent().getStringExtra("bankName");
            String accountNo = getActivity().getIntent().getStringExtra("accountNo");

            // Update user profile in UI
            firstNameTextView.setText(firstName);
            lastNameTextView.setText(lastName);
            phoneTextView.setText(phone);
            emailTextView.setText(email);
            passwordTextView.setText(pass);
            locationTextView.setText(location);
            bankNameTextView.setText(bankName);
            accountNoTextView.setText(accountNo);

            firstNameEditText.setText(firstName);

            lastNameEditText.setText(lastName);
            phoneEditText.setText(phone);
            emailEditText.setText(email);
            passwordEditText.setText(pass);
            locationEditText.setText(location);
            setSpinnerSelection(bankNameSpinner, bankName);
            accountNoEditText.setText(accountNo);

            // Reset editing state
            isEditing = false;
            setEditableFields(false);

            // Clear the intent to avoid processing the same data again
            getActivity().getIntent().removeExtra("firstName");
        }
    }


    // Helper method to set spinner selection
    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
