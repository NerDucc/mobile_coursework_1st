package com.example.coursework_2022_2nd;

import androidx.annotation.ContentView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursework_2022_2nd.data.TripDAO;
import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.FragmentEditorBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditorFragment extends Fragment {

    private FragmentEditorBinding binding;
    private RadioGroup radioGroup;
    private RadioButton radioButtonY;
    private RadioButton radioButtonN;
    private DatePickerDialog datePickerDialog;
    EditText editTextDate;
    TripDAO dao;

    @Override
    public void onResume() {
        super.onResume();
        //Drop Down
        String[] tripName = getResources().getStringArray(R.array.TripName);
        binding.autoCompleteTextView.setAdapter(new ArrayAdapter<>(requireContext(),R.layout.dropdown_tripname, tripName));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_outline_save_24);
        setHasOptionsMenu(true);

        binding = FragmentEditorBinding.inflate(inflater, container, false);


        //Calling the DAO
        dao = new TripDAO(getContext());

        //Calling the DAO and get the trip by ID sent from MainFragment and it returns the trip
//        dao.getByID(getArguments().getString("trip_id"));

        String idReceived = getArguments().getString("trip_id");
        Bundle bundleReceived = getArguments();

        if(idReceived != Constants.New_Trip_ID){
            dao.trip.setValue(dao.getByID(idReceived));
        }
        //Observing the changes and set text for the editor view from the bundle sent from main
        dao.trip.observe(
                getViewLifecycleOwner(),
                trip ->{
                    binding.autoCompleteTextView.setText(bundleReceived.getString("name"));
                    binding.riskSelected.setText(bundleReceived.getString("risk"));
                    binding.editDestination.setText(bundleReceived.getString("destination"));
                    binding.editDescription.setText(bundleReceived.getString("description"));
                    binding.editDate.setText(bundleReceived.getString("date_trip"));
                    binding.editParticipant.setText(Integer.toString(bundleReceived.getInt("participant")));
                    binding.editTransportation.setText(bundleReceived.getString("transportation"));
                    requireActivity().invalidateOptionsMenu();
                }
        );

        //CheckButton
        radioGroup = binding.radioGroup;
        binding.riskSelected.setInputType(InputType.TYPE_NULL);

        radioButtonY = binding.radioYes;
            radioButtonY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRadioButtonClicked(v);
                }
            });
        radioButtonN = binding.radioNo;
            radioButtonN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRadioButtonClicked(v);
                }
            });


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

            binding.changeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(getView()).navigate(R.id.expenseFragment, bundleReceived);
                }
            });

        return binding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (this.validate()) return saveAndReturn() ;
                else return false;
            case R.id.action_delete:
                return deleteAndReturn();
            default: return super.onOptionsItemSelected(item);
        }
    }


    private boolean validate() {
        EditText name = binding.autoCompleteTextView;
        EditText date = binding.editDate;
        EditText destination = binding.editDestination;
        EditText risk= binding.riskSelected;
        EditText participant = binding.editParticipant;

        boolean isValidated = true;

        if (name.getText().toString().equals(Constants.Empty_String)){
            name.setError("Name of the trip is required");
            isValidated = false;
        }
        if (date.getText().toString().equals(Constants.Empty_String)){
            date.setError("Date of the trip is required");
            isValidated = false;
        }
        String participantText = participant.getText().toString();
        if (participantText.equals(Constants.Empty_String) || Integer.parseInt(participantText) <= 0){
            participant.setError("Please fill in the number of people on this trip or we will assume that there is only one person on this trip");
            participant.setText("1");
            isValidated = false;
        }

        if (risk.getText().toString().equals(Constants.Empty_String)){
            risk.setError("Please fill in the risk assessment or we will assume that you do not need any risk assessment");
            binding.riskSelected.setText("No risk assessment required");
            isValidated = false;
        }
        if (destination.getText().toString().equals(Constants.Empty_String)){
            destination.setError("Please fill in the destination of this trip");
            isValidated = false;
        }
        return isValidated;
    }

    //Delete
    private boolean deleteAndReturn() {
        Log.i(this.getClass().getName(), "Delete and return");
        dao.delete(dao.trip.getValue().getId());
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {


        if(getArguments().getString("trip_id") != Constants.New_Trip_ID){
            inflater.inflate(R.menu.trip_delete, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean saveAndReturn() {
        Log.i(this.getClass().getName(), "Saved and return");

        TripEntity trip = new TripEntity();

        trip.setName(binding.autoCompleteTextView.getText().toString());
        trip.setTransportation(binding.editTransportation.getText().toString());
        trip.setRisk(binding.riskSelected.getText().toString());
        trip.setDate(binding.editDate.getText().toString());
        trip.setParticipant(Integer.parseInt(binding.editParticipant.getText().toString()));
        trip.setDestination(binding.editDestination.getText().toString());
        trip.setDescription(binding.editDescription.getText().toString());

        if (getArguments().getString("trip_id") != "0"){
            trip.setId(getArguments().getString("trip_id"));
            dao.update(trip);
        }
        else {
            dao.insert(trip);
        }
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    //Handling Radio Button Clicked
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_yes:
                if (checked)
                    binding.riskSelected.setText("Risk assessment required");
                    break;
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

    //Initiate the date picker
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

    //Convert Date to String
    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " +  dayOfMonth + "," + year;
    }

    //Convert Month to String
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

    //Showing the date picker to the edittext
    private void showDateDialog(EditText editTextDate) {
        datePickerDialog.show();
    }


}