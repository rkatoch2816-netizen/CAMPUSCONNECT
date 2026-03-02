package com.example.login;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class FeesActivity extends AppCompatActivity {

    TextView txtTotalFees, txtFeesPaid, txtBalanceFees;

    class FeeData {
        int total, paid;

        FeeData(int total, int paid) {
            this.total = total;
            this.paid = paid;
        }

        int getBalance() {
            return total - paid;
        }
    }

    HashMap<String, FeeData> yearFees = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);

        txtTotalFees = findViewById(R.id.txtTotalFees);
        txtFeesPaid = findViewById(R.id.txtFeesPaid);
        txtBalanceFees = findViewById(R.id.txtBalanceFees);

        // Year data
        yearFees.put("I", new FeeData(45000, 20000));
        yearFees.put("II", new FeeData(48000, 25000));
        yearFees.put("III", new FeeData(50000, 30000));

        // Year Dropdown setup
        String[] years = {"I", "II", "III"};
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, years);

        AutoCompleteTextView yearSpinner = findViewById(R.id.autoCompleteYear);
        yearSpinner.setAdapter(yearAdapter);

        yearSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String selectedYear = parent.getItemAtPosition(position).toString();
            updateFees(selectedYear);
        });

        // Receipt Type Dropdown setup
        String[] receiptTypes = {"Tuition Fees", "Exam Fees", "Bus Fees", "Other Fees"};
        ArrayAdapter<String> receiptAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, receiptTypes);

        AutoCompleteTextView receiptSpinner = findViewById(R.id.autoCompleteReceiptType);
        receiptSpinner.setAdapter(receiptAdapter);

        // Default load
        updateFees("I");
    }

    private void updateFees(String year) {
        FeeData data = yearFees.get(year);
        if (data != null) {
            txtTotalFees.setText(data.total + "\nTotal Fees");
            txtFeesPaid.setText(data.paid + "\nFees Paid");
            txtBalanceFees.setText(data.getBalance() + "\nBalance");
        }
    }
}
