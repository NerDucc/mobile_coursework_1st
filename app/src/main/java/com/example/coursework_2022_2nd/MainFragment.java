package com.example.coursework_2022_2nd;

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

import com.example.coursework_2022_2nd.data.SampleDataProvider;
import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.FragmentMainBinding;

import java.util.Optional;

public class MainFragment extends Fragment implements TripListAdapter.ListTripListener{

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;
    private TripListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding = FragmentMainBinding.inflate(inflater, container,  false);

        TripEntity t = SampleDataProvider.getTrips().get(0);
//        binding.myText.setText(t.toString());

        RecyclerView rv = binding.recyclerView;
        rv.setHasFixedSize(true);  //each row has equal size regardless of its content
        rv.addItemDecoration(new DividerItemDecoration(
                getContext(),
                (new LinearLayoutManager(getContext())).getOrientation())
        );

        mViewModel.tripList.observe(
                getViewLifecycleOwner(),
                tripList -> {
                    System.out.println(tripList.size());
                    adapter = new TripListAdapter(tripList, this);
                    binding.recyclerView.setAdapter(adapter);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
        );

        return binding.getRoot();
    }


    @Override
    public void onItemClick(String tripId) {
        Optional<TripEntity> be = mViewModel.tripList.getValue().stream()
                .filter(b -> b.getId() == tripId).findFirst();
        if(be.isPresent()){
            Bundle bundle = new Bundle();
            bundle.putString("TripID", tripId);
            Navigation.findNavController(getView()).navigate(R.id.editorFragment, bundle);
        }
    }
}