package com.example.coursework_2022_2nd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
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
import android.widget.SearchView;


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
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity app = (AppCompatActivity)getActivity();
        ActionBar ab = app.getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        binding = FragmentMainBinding.inflate(inflater, container,  false);
        dao = new TripDAO(getContext());

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

    @Override
    public void onItemClick(TripEntity tripInput) {
        TripEntity trip = dao.getByID(tripInput.getId());
        Bundle bundle = new Bundle();
        bundle.putString("trip_id", trip.getId());
        bundle.putString("name",trip.getName());
        bundle.putString("destination",trip.getDestination());
        bundle.putString("date_trip",trip.getDate());
        bundle.putInt("participant", trip.getParticipant());
        bundle.putString("description",trip.getDescription());
        bundle.putString("risk",trip.getRisk());
        bundle.putString("transportation", trip.getTransportation());

        Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundle);
    }
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:  {

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

}