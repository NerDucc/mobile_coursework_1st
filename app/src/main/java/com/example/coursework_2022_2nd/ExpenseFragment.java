package com.example.coursework_2022_2nd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.coursework_2022_2nd.data.ExpenseDAO;
import com.example.coursework_2022_2nd.data.ExpenseEntity;
import com.example.coursework_2022_2nd.databinding.FragmentExpenseBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ExpenseFragment extends Fragment implements ExpenseListAdapter.ListExpenseListener{

    private FragmentExpenseBinding binding;
    private ExpenseListAdapter adapter1;
    ExpenseDAO dao;
    String serviceLink = "https://cwservice1786.herokuapp.com/sendPayLoad";

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
        String nameTrip = getArguments().getString("name");
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
                if(dao.getAll().toString() == "[]")return;
                try {

                    JSONArray array = new JSONArray();
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    for(ExpenseEntity e : dao.getAll()){
                        array.put(e.toMap());
                    }
                    map.put("userId", "GCH190154");
                    map.put("detailList", array);
                    JSONObject jsonObject = new JSONObject(map);
                    String jsonString = jsonObject.toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("json", jsonString);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Information:");
                    builder.setMessage(
                       jsonString
                    );
                    builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Navigation.findNavController(getView()).navigate(R.id.jsonWeb, bundle);
                        }
                    });
                    builder.show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
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
        bundle.putString("location", expense.getLocation());
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