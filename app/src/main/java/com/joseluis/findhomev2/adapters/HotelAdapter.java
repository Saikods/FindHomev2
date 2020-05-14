package com.joseluis.findhomev2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.pojo.Hotel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelCard> {

    private Context context;
    private int resource;
    private ArrayList<Hotel> hotels;

    public HotelAdapter(Context context, int resource, ArrayList<Hotel> hotels) {
        this.context = context;
        this.resource = resource;
        this.hotels = hotels;
    }

    @NonNull
    @Override
    public HotelCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(resource, null);
        HotelCard hotel = new HotelCard(card);

        return hotel;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelCard holder, int position) {
        Hotel hotel = hotels.get(position);

        holder.txtHotelName.setText(hotel.getHotel_name());
        holder.txtStreetName.setText(hotel.getStreet_name());
        holder.txtDescription.setText(hotel.getDescription());

        for (int i = 0; i < hotel.getImages().size(); i++){
            Picasso.get().load(hotel.getImages().get(i)).into(holder.imgHotel);
        }
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public class HotelCard extends RecyclerView.ViewHolder {
        private TextView txtHotelName, txtStreetName, txtDescription;
        private ImageView imgHotel;

        public HotelCard(@NonNull View itemView) {
            super(itemView);
            txtHotelName = itemView.findViewById(R.id.txtHotelNCardHotel);
            txtStreetName = itemView.findViewById(R.id.txtStreetCardHotel);
            txtDescription = itemView.findViewById(R.id.txtDescriptionCardHotel);
            imgHotel = itemView.findViewById(R.id.imgCardHotel);

        }
    }
}
