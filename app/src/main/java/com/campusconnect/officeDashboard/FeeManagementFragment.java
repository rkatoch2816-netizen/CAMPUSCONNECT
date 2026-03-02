package com.campusconnect.officeDashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class FeeManagementFragment extends Fragment {
    
    private TextView feeStatusTextView;
    private TextView totalFeeTextView;
    private TextView paidFeeTextView;
    private TextView dueFeeTextView;

    public FeeManagementFragment() {
        // Required empty public constructor
    }

    public static FeeManagementFragment newInstance() {
        return new FeeManagementFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fee_management, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        feeStatusTextView = view.findViewById(R.id.fee_status);
        totalFeeTextView = view.findViewById(R.id.total_fee);
        paidFeeTextView = view.findViewById(R.id.paid_fee);
        dueFeeTextView = view.findViewById(R.id.due_fee);
        
        // Load fee data
        loadFeeData();
    }

    private void loadFeeData() {
        // TODO: Fetch fee data from API or local database
        // For now, displaying placeholder data
        feeStatusTextView.setText("Fee Status: Pending");
        totalFeeTextView.setText("Total Fee: ₹50,000");
        paidFeeTextView.setText("Paid: ₹0");
        dueFeeTextView.setText("Due: ₹50,000");
    }
}