package com.MithraAandS.projectsa2;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import com.github.javiersantos.appupdater.AppUpdater;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton Post_btn;
    private FirebaseFirestore firebaseFirestore;
    private String current_userId;
    private BottomNavigationView mainBottomNav;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment accountFragment;
    private RiceFragment riceFragment;
    private RiceCatagory riceCatagory;
    private FruitsFragment fruitsFragment;
    private ShareActionProvider shareActionProvider;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainToolbar=(Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("Main Page");


        mAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        checkConnection();



        if (mAuth.getCurrentUser()!=null) {
            final String Admin=mAuth.getCurrentUser().getUid();

            Post_btn = (FloatingActionButton) findViewById(R.id.add_Product_btn);

            if (Admin.equals("o9O0P429OPPrVM5f8GzcYQ0nfNn1") || Admin.equals("1aEcTGqXcgWaeO8TLJyMhULhQ633") || Admin.equals("YJqdm0qcfCbEhc3spXxRGOyP0jy2")|| Admin.equals("7Olx1zllvFSbltT9ibKlNuU4TU72") ){

                Post_btn.show();

            }



            mainBottomNav = findViewById(R.id.mainbottomNavView);

            homeFragment = new HomeFragment();
            accountFragment = new AccountFragment();
            notificationFragment = new NotificationFragment();
            riceFragment=new RiceFragment();
            riceCatagory=new RiceCatagory();
            fruitsFragment=new FruitsFragment();


            replaceFragment(homeFragment);


            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.bottom_home:
                            replaceFragment(homeFragment);
                            return true;

                        case R.id.bottom_Account:
                            replaceFragment(accountFragment);
                            return true;

                        case R.id.bottom_Notification:
                            replaceFragment(notificationFragment);
                            return true;

                        case R.id.bottom_rice:
                            replaceFragment(riceCatagory);
                            return true;

                        case R.id.bottom_fruits:
                            replaceFragment(fruitsFragment);
                            return true;
                        default:
                            return false;


                    }


                }
            });


            Post_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Post_intent = new Intent(MainActivity.this, Post_Acivity.class);
                    startActivity(Post_intent);
                }
            });
        }




    }

    @Override
    protected void onStart() {
        super.onStart();

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.start();

        FirebaseUser currentuser= FirebaseAuth.getInstance().getCurrentUser();

        if(currentuser==null){
           sendToLogin();

        }else {
            current_userId=mAuth.getCurrentUser().getUid();
            firebaseFirestore.collection("Users").document(current_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()){
                         if (!task.getResult().exists()){
                            Intent setIntent=new Intent(MainActivity.this,SetupActivity.class);
                            startActivity(setIntent);
                            finish();

                        }

                    }
                    else {
                        String error = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_logoutbtn:
                logout();
                return true;

            case R.id.action_settingbtn:
                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);

                return true;

            case R.id.CartOption:
                startActivity(new Intent(MainActivity.this,CartActivity.class));
                return true;
            case R.id.Aboutus:
                startActivity(new Intent(MainActivity.this,aboutus.class));
                return true;
            case R.id.contantus:
                startActivity(new Intent(MainActivity.this,ContactUS.class));
                return true;
            case R.id.Term:
                startActivity(new Intent(MainActivity.this,TermsandConditions.class));
                return true;
            case R.id.refund:
                startActivity(new Intent(MainActivity.this,Refund.class));
                return true;
            case R.id.Privacy:
                startActivity(new Intent(MainActivity.this,PrivacyPolicy.class));
                return true;
            case R.id.Ordersop:
                startActivity(new Intent(MainActivity.this,OrderActivity.class));
                return true;
            case R.id.menu_item_share:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String message = "\nLet me recommend you this application https://play.google.com/store/apps/details?id=com.MithraAandS.projectsa2 \n\n";
                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "choose one"));
                return true;

            default:
                return false;

        }


    }

    private void logout() {
        mAuth.signOut();
        sendToLogin();

    }

    private void sendToLogin() {
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){
            Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
           final Dialog addressDialog=new Dialog(MainActivity.this);
            addressDialog.setContentView(R.layout.nointernet);
            addressDialog.show();

            Button exit=(Button)addressDialog.findViewById(R.id.exitbtn);

            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addressDialog.dismiss();
                }
            });

        }
    }

    private void setShareIntent(Intent shareIntent) {
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent);
        }
    }


}
