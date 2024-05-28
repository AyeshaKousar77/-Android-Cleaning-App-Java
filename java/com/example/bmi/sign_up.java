package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi.database.MapActivity;
import com.example.bmi.database.sqlite;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {
    private static final int REQUEST_MAP = 1;

    EditText Column_fName, Column_lName, Contact, Column_email, Column_pass;
    private double latitude;
    private double longitude;
    private boolean isLocationSet = false;

    private Spinner spinnerBank;
    EditText editTextBankAccountNo;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Initialize EditText fields
        Column_fName = findViewById(R.id.editText2);
        Column_lName = findViewById(R.id.editText3);
        Contact = findViewById(R.id.editText4);
        Column_email = findViewById(R.id.editText5);
        Column_pass = findViewById(R.id.editText6);
        spinnerBank = findViewById(R.id.spinnerBank);
        editTextBankAccountNo = findViewById(R.id.editTextBankAccountNo);

        // Button to open map activity
        Button openMapButton = findViewById(R.id.open_map_button);
        openMapButton.setOnClickListener(v -> {
            // Launch MapActivity to select location
            Intent intent = new Intent(sign_up.this, MapActivity.class);
            startActivityForResult(intent, REQUEST_MAP);
        });

        // Initialize Spinner and its adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBank.setAdapter(adapter);

        // Set filters for EditText fields
        editTextBankAccountNo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14), alphanumericFilter});

        // Populate Spinner
        populateSpinner();

        // Initialize Buttons and set onClickListeners
        Button btn4 = findViewById(R.id.b5);
        Button btn5 = findViewById(R.id.b4);

        btn4.setOnClickListener(v -> {
            Intent intent = new Intent(sign_up.this, sign_in.class);
            startActivity(intent);
        });

        btn5.setOnClickListener(v -> {
            try {
                // Retrieve and trim input values
                String firstName = Column_fName.getText().toString().trim();
                String lastName = Column_lName.getText().toString().trim();
                String contactStr = Contact.getText().toString().trim();
                String email = Column_email.getText().toString().trim();
                String password = Column_pass.getText().toString().trim();
                String bank = spinnerBank.getSelectedItem().toString();
                String accountNo = editTextBankAccountNo.getText().toString().trim();

                // Validate fields
                if (firstName.isEmpty() || lastName.isEmpty() || contactStr.isEmpty() || email.isEmpty() || password.isEmpty() || bank.isEmpty() || accountNo.isEmpty()) {
                    Toast.makeText(sign_up.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate and parse the contact number
                long contact;
                try {
                    contact = Long.parseLong(contactStr);
                } catch (NumberFormatException e) {
                    // Handle invalid contact input
                    Toast.makeText(sign_up.this, "Invalid contact number", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Ensure location is set
                if (!isLocationSet) {
                    Toast.makeText(sign_up.this, "Please select a location first", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create database instance and add user
                sqlite mdb = new sqlite(sign_up.this);
                boolean isUserAdded = mdb.addUser(firstName, lastName, contact, email, password, bank, accountNo, latitude, longitude);

                if (isUserAdded) {
                    Toast.makeText(sign_up.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(sign_up.this, "Failed to add user to database", Toast.LENGTH_SHORT).show();
                }

                // Start sign-in activity
                Intent ten = new Intent(sign_up.this, sign_in.class);
                startActivity(ten);
            } catch (Exception e) {
                // Handle any unexpected exceptions
                e.printStackTrace();
                Toast.makeText(sign_up.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinner() {
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

        adapter.addAll(banks);
    }

    private final InputFilter alphanumericFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String pattern = "^[a-zA-Z0-9]*$";
            if (!Pattern.matches(pattern, source)) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MAP && resultCode == RESULT_OK) {
            // Get the selected location from the map activity
            latitude = data.getDoubleExtra("latitude", 0);
            longitude = data.getDoubleExtra("longitude", 0);
            isLocationSet = true;
            Toast.makeText(this, "Location set successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
