package com.example.coursework_2022_2nd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import com.example.coursework_2022_2nd.data.ExpenseDAO;
import com.example.coursework_2022_2nd.data.ExpenseEntity;
import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.FragmentExpenseEditorBinding;

import java.util.Calendar;

public class ExpenseEditorFragment extends Fragment {

    private FragmentExpenseEditorBinding  binding;
    private DatePickerDialog datePickerDialog;
    EditText editTextDate;
    Button button;
    ExpenseDAO expenseDAO;

    @Override
    public void onResume() {
        super.onResume();
        String[] expenseName = getResources().getStringArray(R.array.ExpenseName);
        binding.autoCompleteTextViewExpense.setAdapter(new ArrayAdapter<>(requireContext(), R.layout.dropdown_tripname, expenseName));
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

        binding = FragmentExpenseEditorBinding.inflate(inflater, container, false);


        String tripId = getArguments().getString("trip_id");
        Bundle bundleReceived = getArguments();

        expenseDAO = new ExpenseDAO(getContext(), tripId);
        String expenseIdReceived = getArguments().getString("expenseID");
        if(!expenseIdReceived.equals(Constants.New_Trip_ID)){
            expenseDAO.expense.setValue(expenseDAO.getByID(expenseIdReceived));
        }
        expenseDAO.expense.observe(
                getViewLifecycleOwner(),
                expense -> {
                    binding.autoCompleteTextViewExpense.setText(getArguments().getString("expense_name"));
                    binding.editDateExpense.setText(getArguments().getString("expense_date"));
                    binding.editAmount.setText(getArguments().getString("expense_amount"));

                    requireActivity().invalidateOptionsMenu();
                }
        );

        editTextDate = binding.editDateExpense;
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setText(getTodayDate());
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker();
                showDateDialog(editTextDate);
            }
        });
//        button = binding.actionDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public boolean onClick(View v) {
//                deleteAndReturn();
//            }
//        });

        return binding.getRoot();
    }

    private boolean deleteAndReturn() {
        Log.i(this.getClass().getName(), "Delete and return");
        expenseDAO.delete(expenseDAO.expense.getValue().getE_ID());
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (this.ExpenseValidate()) return expenseSaveAndReturn() ;
                else return false;
            case R.id.action_delete_expense:
                return ExpenseDeleteAndReturn();
            default: return super.onOptionsItemSelected(item);
        }
    }

    private boolean ExpenseDeleteAndReturn() {
        Log.i(this.getClass().getName(), "Delete and return");
        expenseDAO.delete(expenseDAO.expense.getValue().getE_ID());
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }

    private boolean expenseSaveAndReturn() {
        Log.i(this.getClass().getName(), "Saved and return");

        ExpenseEntity expense = new ExpenseEntity();

        expense.setType(binding.autoCompleteTextViewExpense.getText().toString());
        expense.setDate(binding.editDateExpense.getText().toString());
        expense.setAmount(Integer.parseInt(binding.editAmount.getText().toString()));
        expense.setT_ID(getArguments().getString("trip_id"));
        if (getArguments().getString("expenseID") != "0"){
            expense.setE_ID(getArguments().getString("expenseID"));
            expenseDAO.update(expense);
            System.out.println(expense);

        }
        else {
            System.out.println(expense);
            expenseDAO.insert(expense);
        }
        Navigation.findNavController(getView()).navigateUp();
        return true;
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if(getArguments().getString("expenseID") != Constants.New_Trip_ID){
            inflater.inflate(R.menu.expense_delete, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private boolean ExpenseValidate() {
        EditText name = binding.autoCompleteTextViewExpense;
        EditText date = binding.editDateExpense;
        EditText amount = binding.editAmount;
        boolean isValidated = true;

        if (name.getText().toString().equals(Constants.Empty_String)){
            name.setError("Name of the expense is required");
            isValidated = false;
        }
        if (date.getText().toString().equals(Constants.Empty_String)){
            date.setError("Date of the expense is required");
            isValidated = false;
        }
        String amount1 = amount.getText().toString();
        if (amount1.equals(Constants.Empty_String) || Integer.parseInt(amount1) <= 0){
            amount.setError("Please fill in the amount");
            isValidated = false;
        }
        return isValidated;
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
                binding.editDateExpense.setText(date);
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