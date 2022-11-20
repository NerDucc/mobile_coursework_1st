package com.example.coursework_2022_2nd;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coursework_2022_2nd.data.ExpenseEntity;
import com.example.coursework_2022_2nd.data.TripEntity;
import com.example.coursework_2022_2nd.databinding.ListExpenseBinding;
import com.example.coursework_2022_2nd.databinding.ListTripBinding;

import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder> {
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_expense, parent, false);
        return new ExpenseListAdapter.ExpenseViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        ExpenseEntity e = expenseList.get(position);
        holder.bindData(e);
    }
    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public interface ListExpenseListener{
        void onItemClick(ExpenseEntity expenseId);
    }
    public class ExpenseViewHolder extends RecyclerView.ViewHolder{

        private final ListExpenseBinding itemViewBinding;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            itemViewBinding = ListExpenseBinding.bind(itemView);
        }

        public void bindData(ExpenseEntity tData){
            itemViewBinding.expenseName.setText(tData.getType());
            itemViewBinding.getRoot().setOnClickListener(
                    v -> {
                        listener.onItemClick(tData);
                    }
            );
        }
    }

    private List<ExpenseEntity> expenseList;
    private ListExpenseListener listener;

    public ExpenseListAdapter(List<ExpenseEntity> expenseList, ListExpenseListener listener) {
        this.expenseList = expenseList;
        this.listener = listener;
    }

}
