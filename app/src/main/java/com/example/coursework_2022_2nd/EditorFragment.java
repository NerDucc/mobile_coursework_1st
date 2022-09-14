package com.example.coursework_2022_2nd;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.coursework_2022_2nd.databinding.FragmentEditorBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditorFragment extends Fragment {

    private FragmentEditorBinding binding;
    private EditorViewModel mViewModel;
    private RadioGroup radioGroup;
    private RadioButton radioButtonY;
    private RadioButton radioButtonN;
    private TextView textView;
    private DatePickerDialog datePickerDialog;
    EditText editTextDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        binding = FragmentEditorBinding.inflate(inflater, container, false);

        //CheckButton
        radioGroup = binding.radioGroup;
        binding.riskSelected.setInputType(InputType.TYPE_NULL);
        radioButtonY = binding.radioYes;
        radioButtonY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioYesButtonClicked(v);
            }
        });
        radioButtonN = binding.radioNo;
        radioButtonN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRadioNoButtonClicked(v);
            }
        });



        //Drop Down
        Resources res = getResources();
        String[] tripName = res.getStringArray(R.array.TripName);
        ArrayAdapter<String> TripNameArrayAdapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_tripname , tripName);
        binding.autoCompleteTextView.setAdapter(TripNameArrayAdapter);

        //DatePicker
        editTextDate = binding.editDate;
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setText(getTodayDate());
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                showDateDialog(editTextDate);
            }
        });


        String tripID = getArguments().getString("TripID");
        binding.editDescription.setText(tripID);

        return binding.getRoot();
    }

    //Handling Radio Button Clicked
    public void onRadioYesButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    binding.riskSelected.setText("Risk assessment required");
                    break;
        }
    }

    //Handling Radio Button Clicked
    public void onRadioNoButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_no:
                if (checked)
                    binding.riskSelected.setText("No risk assessment required");
                break;
        }
    }

    //Get Date Today
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = makeDateString(dayOfMonth, month, year);
                binding.editDate.setText(date);
            }

        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireContext(), style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " +  dayOfMonth + "," + year;
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "January";
        if(month == 2)
            return "February";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "August";
        if(month == 9)
            return "September";
        if(month == 10)
            return "October";
        if(month == 11)
            return "November";
        if(month == 12)
            return "December";

        //default should never happen
        return "January";

    }

    private void showDateDialog(EditText editTextDate) {
        datePickerDialog.show();
    }


}