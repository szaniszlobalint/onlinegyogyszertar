package com.example.onlinegyogyszertar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.view.View.GONE;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder>  {


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameTextView;
        public TextView priceTextView;
        public Button addButton;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productImage);
            nameTextView = itemView.findViewById(R.id.productName);
            priceTextView = itemView.findViewById(R.id.productPrice);
            addButton = itemView.findViewById(R.id.addButton);

        }
    }
    Context context;
    private ArrayList<Product> products;
    private boolean showAddButton;
    private Activity activity;

    public ProductsAdapter(Activity activity, ArrayList<Product> products, boolean showAddButton) {
        this.activity = activity;
        this.products = products;
        this.showAddButton = showAddButton;
    }

    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.product_layout, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);

        TextView nameTextView = holder.nameTextView;
        TextView priceTextView = holder.priceTextView;
        nameTextView.setText(product.getName());
        DecimalFormat formatter = new DecimalFormat("#,###");
        priceTextView.setText(formatter.format(product.getPrice()).replace(',', ' ') + " Ft");
        Button button = holder.addButton;
        //button.setEnabled(MainActivity.isSignedIn());
        if(!MainActivity.isSignedIn()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundTintList(AppCompatResources.getColorStateList(context, android.R.color.darker_gray));
            } else {
                button.setBackgroundColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundTintList(AppCompatResources.getColorStateList(context, R.color.green_500));
            } else {
                button.setBackgroundColor(ContextCompat.getColor(context, R.color.green_500));
            }
        }

        if(!showAddButton) {
            button.setVisibility(GONE);
        }
        holder.imageView.setImageDrawable(product.getImage());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.isSignedIn()){
                    CartActivity.products.add(product);
                    ((MainActivity)activity).startCartAnimation(context);
                } else {
                    ((MainActivity)activity).startLoginAnimation(context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }



}
