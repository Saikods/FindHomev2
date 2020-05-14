package com.joseluis.findhomev2.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.joseluis.findhomev2.R;
import com.joseluis.findhomev2.pojo.Rents;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.RentCard> {

    private Context context;
    private int resource;
    private ArrayList<Rents> rents;

    public RentAdapter(Context context, int resource, ArrayList<Rents> rents) {
        this.context = context;
        this.resource = resource;
        this.rents = rents;
    }

    @NonNull
    @Override
    public RentCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(context).inflate(resource, null);
        RentCard rent1 = new RentCard(card);

        return rent1;
    }

    @Override
    public void onBindViewHolder(@NonNull RentCard holder, int position) {
        final Rents rent = rents.get(position);

        holder.txtStreet.setText(rent.getStreet_name());
        holder.txtDescription.setText(rent.getDescription());
        holder.txtPrice.setText(String.valueOf(rent.getPrice()));
        holder.ratingBar.setRating(rent.getRating());

        Picasso.get().load(rent.getImages().get(0)).into(holder.imgCard);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("rent", rent);


                Navigation.findNavController(v).navigate(R.id.nav_infor, bundle);
            }
        });
    }


    @Override
    public int getItemCount() {
        return rents.size();
    }

    public class RentCard extends RecyclerView.ViewHolder {
        private TextView txtStreet, txtDescription, txtPrice;
        private ImageView imgCard;
        private RatingBar ratingBar;
        private CardView cardView;


        public RentCard(@NonNull View itemView) {
            super(itemView);
            txtStreet = itemView.findViewById(R.id.txtStreetCardRent);
            txtDescription = itemView.findViewById(R.id.txtDescriptionCardRent);
            imgCard = itemView.findViewById(R.id.imgCardRent);
            txtPrice = itemView.findViewById(R.id.txtPriceCardRent);
            ratingBar = itemView.findViewById(R.id.rBarCardRent);
            cardView = itemView.findViewById(R.id.cardViewRent);
        }
    }
}
