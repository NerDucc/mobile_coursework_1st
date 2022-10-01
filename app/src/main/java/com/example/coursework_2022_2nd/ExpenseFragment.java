package com.example.coursework_2022_2nd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coursework_2022_2nd.data.ExpenseDAO;
import com.example.coursework_2022_2nd.data.ExpenseEntity;
import com.example.coursework_2022_2nd.data.SampleDataProvider;
import com.example.coursework_2022_2nd.databinding.FragmentExpenseBinding;
import com.example.coursework_2022_2nd.databinding.FragmentMainBinding;

import org.json.JSONObject;

import java.util.Optional;

public class ExpenseFragment extends Fragment implements ExpenseListAdapter.ListExpenseListener{

    private FragmentExpenseBinding binding;
    private ExpenseListAdapter adapter1;
    ExpenseDAO dao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity aca = (AppCompatActivity) getActivity();
        aca.getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        binding = FragmentExpenseBinding.inflate(inflater, container,  false);



        RecyclerView rv2 = binding.recyclerViewExpense;
        rv2.setHasFixedSize(true);  //each row has equal size regardless of its content
        rv2.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );
        String tripID = getArguments().getString("trip_id");
        dao = new ExpenseDAO(getContext(), tripID);
        dao.expenseList.setValue(dao.getAll());

        dao.expenseList.observe(
                getViewLifecycleOwner(),
                expenseList -> {
                    adapter1 = new ExpenseListAdapter(expenseList, this);
                    binding.recyclerViewExpense.setAdapter(adapter1);
                    binding.recyclerViewExpense.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );
//        System.out.println(dao.expenseList);
        //Set listener for the adding button
        binding.fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("expenseID", Constants.New_Trip_ID);
                bundle.putString("trip_id", getArguments().getString("trip_id"));

                Navigation.findNavController(getView()).navigate(R.id.expenseEditorFragment, bundle);
            }
        });

        return binding.getRoot();

    }


    @Override
    public void onItemClick(ExpenseEntity expenseInput) {
        Bundle bundle = new Bundle();
        ExpenseEntity expense = dao.getByID(expenseInput.getE_ID());
        bundle.putString("expenseID", expense.getE_ID());
        bundle.putString("expense_name", expense.getType());
        bundle.putString("expense_amount", expense.getAmount().toString());
        bundle.putString("expense_date", expense.getDate());
        bundle.putString("trip_id", expense.getT_ID());
        bundle.putString("comment", expense.getNotes());
        Navigation.findNavController(getView()).navigate(R.id.expenseEditorFragment, bundle);
//        System.out.println(bundle);
    }
}