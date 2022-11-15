package com.example.coursework_2022_2nd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.ListTripBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TripListAdapter extends RecyclerView.Adapter<TripListAdapter.TripViewHolder> {



    public interface ListTripListener{
        void onItemClick(TripEntity tripId);
    }

    private List<TripEntity> trips;
    private ListTripListener listener;

    public TripListAdapter(List<TripEntity> tripList, ListTripListener listener) {
        this.trips = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        TripEntity tData = trips.get(position);
        holder.bindData(tData);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder{

        private final ListTripBinding itemViewBinding;

        public TripViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListTripBinding.bind(itemView);
        }

        public void bindData(TripEntity tData){
            itemViewBinding.tripName.setText(tData.getName() + "\n" + tData.getDate());
            itemViewBinding.getRoot().setOnClickListener(
                    v -> {
                        listener.onItemClick(tData);
                    }
            );
        }
    }



}
