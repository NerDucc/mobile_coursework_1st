package com.example.coursework_2022_2nd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.coursework_2022_2nd.data.DBHelper;
import com.example.coursework_2022_2nd.data.SampleDataProvider;
import com.example.coursework_2022_2nd.data.TripDAO;
import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainFragment extends Fragment implements TripListAdapter.ListTripListener{

    private FragmentMainBinding binding;
    private TripListAdapter adapter;
    TripDAO dao;
    SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Init the menu bar
        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        //Binding
        binding = FragmentMainBinding.inflate(inflater, container,  false);

        //Init the DAO class to get the data
        dao = new TripDAO(getContext());

        dao.tripList.setValue(dao.getAll());

        TextView empty1 = binding.empty1;
        TextView empty2 = binding.empty2;

        //Creating the recycler view
        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);  //each row has equal size regardless of its content
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );
 
        //Observe the change of list item for the recycler view
        dao.tripList.observe(
                getViewLifecycleOwner(),
                tripList -> {
                    System.out.println(tripList.size());
                    adapter = new TripListAdapter(tripList, this);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if(tripList.size() == 0){
                        empty1.setVisibility(View.VISIBLE);
                        empty2.setVisibility(View.VISIBLE);
                        rv.setVisibility(View.GONE);
                    }
                    else{
                        empty1.setVisibility(View.GONE);
                        empty2.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                    }
                }
        );

        //Set listener for the adding button
        binding.fabAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("trip_id", Constants.New_Trip_ID);
                bundle.putString("name",Constants.Empty_String);
                bundle.putString("destination",Constants.Empty_String);
                bundle.putString("date_trip",Constants.Empty_String);
                bundle.putInt("participant", 1);
                bundle.putString("description",Constants.Empty_String);
                bundle.putString("risk",Constants.Empty_String);
                bundle.putString("transportation", Constants.Empty_String);

                Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundle);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(this.getClass().getName(), "On Resume");
        dao.getAll();
    }


    //Set click event for a specific trip and navigate to the edit form
    @Override
    public void onItemClick(TripEntity tripInput) {

        //Get the specific ID of the trip by the click listener
        TripEntity trip = dao.getByID(tripInput.getId());

        //Prepare the bundle
        Bundle bundle = new Bundle();
        bundle.putString("trip_id", trip.getId());
        bundle.putString("name",trip.getName());
        bundle.putString("destination",trip.getDestination());
        bundle.putString("date_trip",trip.getDate());
        bundle.putInt("participant", trip.getParticipant());
        bundle.putString("description",trip.getDescription());
        bundle.putString("risk",trip.getRisk());
        bundle.putString("transportation", trip.getTransportation());

        //Sending the bundle to the edit form
        Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundle);
    }

    //Init the listener for the search menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dao.search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                System.out.println(newText);
                dao.search(newText);
                return false;
            }
        });
    }
}