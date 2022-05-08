package com.example.onlinegyogyszertar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    static ArrayList<Product> products = new ArrayList<>();
    RecyclerView rvProducts;
    ProductsAdapter adapter;
    int priceOfProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        for (Product product: products
        ) {
            priceOfProducts+= product.getPrice();
        }
        rvProducts = findViewById(R.id.recycleview);
        Button buyButton = findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(priceOfProducts == 0){
                    Toast.makeText(CartActivity.this, "You have no products in your cart",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                sendNotification();
            }
        });

        adapter = new ProductsAdapter(this, products, false);
        rvProducts.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        rvProducts.setLayoutManager(gridLayoutManager);
        TextView priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setText("Összesen: " + priceOfProducts + " Ft");
    }

    public void sendNotification() {

    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        CharSequence name = "Vásárlás";
        String description = "Fejlemény a vásárlásáról";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("buy", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "buy")
            .setSmallIcon(R.drawable.ic_baseline_shopping_cart_24)
            .setContentTitle("Sikeres vásárlás!")
            .setContentText("Köszönjük hogy nálunk vásárolt!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }
}
