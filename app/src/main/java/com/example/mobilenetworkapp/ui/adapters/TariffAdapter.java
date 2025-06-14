package com.example.mobilenetworkapp.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilenetworkapp.R;
import com.example.mobilenetworkapp.models.TariffContract;
import com.example.mobilenetworkapp.models.TariffPrepaid;

import java.util.List;

public class TariffAdapter extends RecyclerView.Adapter<TariffAdapter.TariffViewHolder> {

    private static final String TAG = "TariffAdapter";

    public interface OnTariffSelectListener {
        void onTariffSelected(TariffPrepaid tariff);
    }

    private List<TariffPrepaid> tariffs;
    private OnTariffSelectListener listener;

    public TariffAdapter(List<TariffPrepaid> tariffs) {
        this.tariffs = tariffs;
        Log.d(TAG, "Adapter initialized with " + tariffs.size() + " tariffs");
    }

    public void setOnTariffSelectListener(OnTariffSelectListener listener) {
        this.listener = listener;
        Log.d(TAG, "Tariff select listener set");
    }

    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "Creating new ViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tariff, parent, false);
        return new TariffViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {
        TariffPrepaid tariff = tariffs.get(position);
        Log.d(TAG, "Binding tariff: " + tariff.getName() + " at position " + position);

        holder.name.setText(tariff.getName());
        holder.price.setText(String.valueOf(tariff.getMonthlyFee()));
        holder.internetService.setText(tariff.getInternetGigabytesCount() + " ГБ мобільного інтернету");
        holder.phoneCallsService.setText(tariff.getCallMinutesCount() + " хв дзвінки на інші мережі");
        holder.smsService.setText(tariff.getSmsCount() + " шт SMS по Україні");

        // Додаткові послуги тільки для TariffContract
        if (tariff instanceof TariffContract) {
            String services = ((TariffContract) tariff).getAdditionalServices();
            holder.additionalServices.setText("Додаткові послуги: " + services);
            holder.additionalServices.setVisibility(View.VISIBLE);
        } else {
            holder.additionalServices.setVisibility(View.GONE);
        }

        holder.chooseButton.setOnClickListener(v -> {
            Log.d(TAG, "Choose button clicked for tariff: " + tariff.getName());
            if (listener != null) {
                listener.onTariffSelected(tariff);
            } else {
                Log.w(TAG, "Listener is null when chooseButton clicked");
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = tariffs != null ? tariffs.size() : 0;
        Log.d(TAG, "Item count: " + count);
        return count;
    }

    public void updateList(List<TariffPrepaid> filteredList) {
        Log.d(TAG, "Updating list with " + filteredList.size() + " items");
        tariffs = filteredList;
        notifyDataSetChanged();
    }

    static class TariffViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, internetService, phoneCallsService, smsService, additionalServices;
        Button chooseButton;

        public TariffViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tariffName);
            price = itemView.findViewById(R.id.tariffPrice);
            internetService = itemView.findViewById(R.id.internetService);
            phoneCallsService = itemView.findViewById(R.id.phoneCallsService);
            smsService = itemView.findViewById(R.id.smsService);
            additionalServices = itemView.findViewById(R.id.additionalServices);
            chooseButton = itemView.findViewById(R.id.chooseButton);
            Log.d(TAG, "ViewHolder created");
        }
    }
}