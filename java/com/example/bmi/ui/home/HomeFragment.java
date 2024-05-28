package com.example.bmi.ui.home;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.lifecycle.ViewModelProvider;
import com.example.bmi.R;
import com.example.bmi.databinding.FragmentHomeBinding;
public class HomeFragment extends Fragment {

     FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        CardView cardView = root.findViewById(R.id.cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.text_home);
                // Navigate to CleanFragment using the action defined in the navigation graph
                navController.navigate(R.id.action_homeFragment_to_cleanFragment);
            }
        });
        CardView cardView2 = root.findViewById(R.id.cardView2);

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.text_home);
                // Navigate to CleanFragment using the action defined in the navigation graph
                navController.navigate(R.id.action_homeFragment_to_paintFragment);
            }
        });
        CardView cardView3 = root.findViewById(R.id.cardView3);

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.text_home);
                // Navigate to CleanFragment using the action defined in the navigation graph
                navController.navigate(R.id.action_homeFragment_to_carFragment);
            }
        });
        CardView cardView4 = root.findViewById(R.id.cardView4);

        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.text_home);
                // Navigate to CleanFragment using the action defined in the navigation graph
                navController.navigate(R.id.action_homeFragment_to_kitchenFragment);
            }
        });
        CardView cardView5 = root.findViewById(R.id.cardView5);

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.text_home);
                // Navigate to CleanFragment using the action defined in the navigation graph
                navController.navigate(R.id.action_homeFragment_to_customFragment);
            }
        });

        // Set an OnClickListener on the CardView



       CardView dialButton = root.findViewById(R.id.cardView6);

        dialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phone number to dial
                String phoneNumber = "+923315417689"; // Replace with the desired phone number

                // Create an intent to dial the phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));

                // Check if there is an activity to handle the intent
                if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    // Start the activity
                    startActivity(intent);
                }
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