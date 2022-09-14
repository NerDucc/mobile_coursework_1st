package com.example.coursework_2022_2nd;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.coursework_2022_2nd.data.SampleDataProvider;
import com.example.coursework_2022_2nd.data.TripEntity;

import java.util.List;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<List<TripEntity>> tripList = new MutableLiveData<List<TripEntity>>();
    {
        tripList.setValue(SampleDataProvider.getTrips());

    }
}