package com.example.coursework_2022_2nd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.ListTripBinding;

import java.util.List;

public class TripListAdapter  extends RecyclerView.Adapter<TripListAdapter.TripViewHolder>  {

    public interface ListTripListener{
        void onItemClick(String tripId);
    }

    private List<TripEntity> tripList;
    private ListTripListener listener;

    public TripListAdapter(List<TripEntity> tripList, ListTripListener listener) {
        this.tripList = tripList;
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
        TripEntity tData = tripList.get(position);
        holder.bindData(tData);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder{

        private final ListTripBinding itemViewBinding;

        public TripViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListTripBinding.bind(itemView);
        }

        public void bindData(TripEntity tData){
            itemViewBinding.tripName.setText(tData.getId());
            itemViewBinding.getRoot().setOnClickListener(
                    v -> {
                        listener.onItemClick(tData.getId());
                    }
            );
        }
    }
}
