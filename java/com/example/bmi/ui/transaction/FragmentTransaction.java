package com.example.bmi.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bmi.R;
import com.example.bmi.database.sqlite;
import com.example.bmi.database.transactionSqlite;
import com.example.bmi.database.token;
import com.example.bmi.databinding.ActivityTransactionBinding;

public class FragmentTransaction extends Fragment {

    private ActivityTransactionBinding binding;
    private transactionSqlite trans;
    private token userToken;
    private double amount;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        transactionViewModel specificViewModel =
                new ViewModelProvider(this).get(transactionViewModel.class);

        binding = ActivityTransactionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTransaction;
        specificViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Initialize views
        TextView textViewAmount = root.findViewById(R.id.textViewAmount);
        TextView textViewUserName = root.findViewById(R.id.uname);
        TextView textViewBankAccount = root.findViewById(R.id.ubank);
        Button buttonConfirm = root.findViewById(R.id.buttonConfirm);

        sqlite dbHelper = new sqlite(getContext());
        trans = new transactionSqlite(getContext());
        userToken = new token(requireContext());

        // Get the amount from the arguments
        if (getArguments() != null) {
            amount = getArguments().getFloat("amount", 0.0f);
        }

        // Set the amount in the TextView
        textViewAmount.setText(String.valueOf(amount));

        // Fetch user details from the database
        String email = userToken.getUserEmail();
        if (email != null) {
            String userName = dbHelper.getUserFullName(email);
            String bankAccount = dbHelper.getUserBankAccount(email);

            // Display user details
            textViewUserName.setText(userName);
            textViewBankAccount.setText(bankAccount);
        }

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save transaction details
                String email = userToken.getUserEmail();
                if (email != null) {
                    trans.saveTransaction(email, amount);
                    Toast.makeText(requireContext(), "Transaction details saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Error: User email not found", Toast.LENGTH_SHORT).show();
                }

                // Move to home fragment
                NavController navController = Navigation.findNavController(requireActivity(), R.id.text_transaction);
                navController.navigate(R.id.action_transaction_to_Home);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
