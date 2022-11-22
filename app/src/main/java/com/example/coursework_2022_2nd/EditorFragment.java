package com.example.coursework_2022_2nd;

import androidx.annotation.ContentView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
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

    private Switch aSwitch;
    private FragmentEditorBinding binding;
    private DatePickerDialog datePickerDialog;
    EditText editTextDate;
    TripDAO dao;
    boolean riskCheck;
    SharedPreferences sharedPreferences;

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
        String idReceived = getArguments().getString("trip_id");
        Bundle bundleReceived = getArguments();

        //Set condition for the update
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


        binding.riskSelected.setInputType(InputType.TYPE_NULL);
        aSwitch = binding.switchRisk;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        binding.riskSelected.setText("Risk assessment required");
                        sharedPreferences.edit().putBoolean("Notification", true).apply();
                    } else {
                        binding.riskSelected.setText("No risk assessment required");
                        sharedPreferences.edit().putBoolean("Notification", false).apply();
                    }
                }
            });
        riskCheck = sharedPreferences.getBoolean("Notification", true);
        aSwitch.setChecked(riskCheck);



        //DatePicker
        editTextDate = binding.editDate;
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setText(DateHandling.getTodayDate());
            editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                showDateDialog();
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
            case R.id.expenseDetail:
                return toExpense();
            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean toExpense() {
        Bundle bundleReceived = getArguments();
        Navigation.findNavController(getView()).navigate(R.id.expenseFragment, bundleReceived);
        return true;
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete this trip");
        builder.setMessage("This trip will be permanently deleted ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dao.delete(dao.trip.getValue().getId());
                Navigation.findNavController(getView()).navigateUp();
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Information of this trip:");
        builder.setMessage("Name of the trip: " + binding.autoCompleteTextView.getText().toString() + "\n" +
                "Transportation: " + binding.editTransportation.getText().toString() + "\n" +
                "Risk assessment: " + binding.riskSelected.getText().toString() + "\n" +
                "Date of the trip: " + binding.editDate.getText().toString() + "\n" +
                "Participant: "  + binding.editParticipant.getText().toString() + "\n" +
                "Destination: " + binding.editDestination.getText().toString() + "\n" +
                "Description: " + binding.editDescription.getText().toString() + "\n" );
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getArguments().getString("trip_id") != "0"){
                    trip.setId(getArguments().getString("trip_id"));
                    dao.update(trip);
                }
                else{
                    dao.insert(trip);
                }
                dialog.dismiss();
                Navigation.findNavController(getView()).navigateUp();
            }
        });

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        return true;
    }


    //Initiate the date picker
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = DateHandling.makeDateString(dayOfMonth, month, year);
                binding.editDate.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(requireActivity(), style,dateSetListener, year, month, day);
    }

    //Showing the date picker to the edittext
    private void showDateDialog() {
        datePickerDialog.show();
    }

}

