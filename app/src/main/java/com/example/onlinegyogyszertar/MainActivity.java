package com.example.onlinegyogyszertar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Product> products = new ArrayList<>();
    RecyclerView rvProducts;
    private FirebaseAuth mAuth;
    private static boolean isSignedIn;
    ProductsAdapter adapter;
    View cartItem;
    View loginItem;
    private Vibrator vibrator;

    public void startCartAnimation(Context context) {
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.cart_animation);
        cartItem.startAnimation(animation);
    }

    public void startLoginAnimation(Context context) {
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.login_icon_animation);
        loginItem.startAnimation(animation);
        vibrator.vibrate(200);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvProducts = findViewById(R.id.recycleview);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products").orderBy("name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.get("name").toString();
                                int price = Integer.parseInt(document.get("price").toString());
                                products.add(new Product (name, price, getImage(name)));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


        adapter = new ProductsAdapter(this, products, true);
        rvProducts.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvProducts.setLayoutManager(gridLayoutManager);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    private Drawable getImage(String name) {
        switch(name){
            case "Algoflex":
                return ContextCompat.getDrawable(this, R.drawable.algoflex);
            case "Aqua Maris":
                return ContextCompat.getDrawable(this, R.drawable.aqua);
            case "C-vitamin":
                return ContextCompat.getDrawable(this, R.drawable.cvitamin);
            case "Wick":
                return ContextCompat.getDrawable(this, R.drawable.wick);
            default:
                return null;
        }
    }

    public static boolean isSignedIn() {
        return isSignedIn;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            this.isSignedIn = true;
            adapter.notifyDataSetChanged();
            Snackbar snackbar = Snackbar.make(this.findViewById(R.id.coordinator),
                    "Ön jelenleg be van jelentkezve", Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            this.isSignedIn = false;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                cartItem = findViewById(R.id.cartItem);
                loginItem = findViewById(R.id.loginItem);
                }});
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if(itemid == R.id.loginItem){
            if(isSignedIn) {
                Snackbar snackbar = Snackbar.make(this.findViewById(R.id.coordinator),
                        "Ön már be van jelentkezve, ha ki szeretne jelentkezni: ", Snackbar.LENGTH_LONG);
                snackbar.setAction( "Kijelentkezés", view -> {
                    mAuth.signOut();
                    isSignedIn = false;
                    adapter.notifyDataSetChanged();
                });
                snackbar.show();
            } else {
                showPopUp();
            }
        } else if(itemid == R.id.cartItem) {
            Intent i = new Intent(this, CartActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopUp() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.login_layout, null);

        // create the popup window
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView,
                width - getResources().getInteger(R.integer.popup_horizontal_margin),
                height - getResources().getInteger(R.integer.popup_horizontal_margin), focusable);
        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(this.rvProducts, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
        TextInputEditText email = popupView.findViewById(R.id.emailTextInputEditText);
        TextInputEditText password = popupView.findViewById(R.id.passwordTextInputEditText);
        Button loginButton = popupView.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Login failed",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    MainActivity.this.isSignedIn = true;
                                    MainActivity.this.adapter.notifyDataSetChanged();
                                    popupWindow.dismiss();
                                    Snackbar snackbar = Snackbar.make(MainActivity.this.findViewById(R.id.coordinator),
                                            "Sikeres bejelentkezés!", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        Button registerButton = popupView.findViewById(R.id.registrationButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                popupWindow.dismiss();
                startActivity(i);
            }
        });
    }





}