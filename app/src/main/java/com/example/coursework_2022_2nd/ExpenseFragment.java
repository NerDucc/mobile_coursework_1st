package com.example.coursework_2022_2nd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        binding = FragmentExpenseBinding.inflate(inflater, container,  false);

        TextView empty3 = binding.empty3;
        TextView empty4 = binding.empty4;
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
                    if(expenseList.size() == 0){
                        empty3.setVisibility(View.VISIBLE);
                        empty4.setVisibility(View.VISIBLE);
                        rv2.setVisibility(View.GONE);
                    }
                    else{
                        empty3.setVisibility(View.GONE);
                        empty4.setVisibility(View.GONE);
                        rv2.setVisibility(View.VISIBLE);
                    }
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

        binding.fabUploadExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.expense_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                return backHome();
            default: return super.onOptionsItemSelected(item);}
    }

    private boolean backHome() {
        Navigation.findNavController(getView()).navigate(R.id.mainFragment);
        return true;
    }
}