package com.MithraAandS.projectsa2;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class CartActivity extends AppCompatActivity implements TotalListener, PaymentResultListener,OrderIdWeight, LocationListener {
    private RecyclerView blog_list_view;
    private List<BlogPost> blog_list,fraglist;
    private Toolbar CartToolbar;
    private Button Totaltext;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private CartAdapter cartAdapter;
    private Dialog addressDialog,FaildDialogbox;
    private AlertDialog dialog;
    private String randomName;
    private String currentUser,CartCount;
    private Map<String, Object> addressMap,cartMap,failMap;
    private double totalvalue,longitude,latitude;
    private Button Buybtn,locationbtn;
    private Spinner nicknameSpinner;
    private String[] nicklist={"Home","office","other"},weightList={"1","2","3","5","6","7","8","9","10"};
    private EditText customername,contactno,houseno,housename,colonyname,streetname,landmark,cityname,pincode;
    private String namestring,contactstring,housenameString,housenumberString,colonystring,StreetnameString,OtherProductString;
    private String landstring,cityString,PincodeString,nicknameString,ProductId,VariableweightString,AbourtProductString;
    private DocumentSnapshot lastVisible;
    private Boolean isFirstPageFirstLoad = true;
    Handler mHandler;
    private String[] order=new String[10];
    String mynewstring;
    String str;
    LocationManager locationManager;
    private ProgressBar mapprpgress;
    private TextView locationText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        this.mHandler = new Handler();
        m_Runnable.run();



        CartToolbar=(Toolbar)findViewById(R.id.cart_toolbar);
        setSupportActionBar(CartToolbar);
        getSupportActionBar().setTitle("Your Basket");
        //final String TotalString=getIntent().getExtras().getString("TotalPrice");

        blog_list = new ArrayList<>();
        fraglist = new ArrayList<>();
        blog_list_view = findViewById(R.id.CartList);
        Totaltext=findViewById(R.id.TotalPriceTectview);



        firebaseAuth = FirebaseAuth.getInstance();
        currentUser=firebaseAuth.getCurrentUser().getUid();
        dialog= new SpotsDialog.Builder().setContext(CartActivity.this).build();
        randomName = UUID.randomUUID().toString();


        cartAdapter = new CartAdapter(blog_list,fraglist);
        blog_list_view.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        blog_list_view.setAdapter(cartAdapter);
        blog_list_view.setHasFixedSize(true);
        cartAdapter.setListener(this);
        cartAdapter.setOrderIdWeight(this);





        if(firebaseAuth.getCurrentUser() != null) {

            firebaseFirestore = FirebaseFirestore.getInstance();

            blog_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Boolean reachedBottom = !recyclerView.canScrollVertically(1);

                    if(reachedBottom){

                      loadMorePost();

                    }


                }
            });

            String currentUser=firebaseAuth.getCurrentUser().getUid();

            Query firstQuery = firebaseFirestore.collection("Cart").document("Product")
                    .collection(currentUser).orderBy("timestamp", Query.Direction.DESCENDING).limit(3);
            firstQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        if (isFirstPageFirstLoad) {
                            try {

                                lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                                blog_list.clear();

                            }catch (Exception ex){
                                Toast.makeText(CartActivity.this, "No Post To Show", Toast.LENGTH_SHORT).show();

                            }


                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                BlogPost frag = doc.getDocument().toObject(BlogPost.class).withkey("Cart");

                                if (isFirstPageFirstLoad) {

                                    blog_list.add(blogPost);
                                    fraglist.add(frag);



                                } else {

                                    blog_list.add(0, blogPost);
                                    fraglist.add(frag);


                                }


                                cartAdapter.notifyDataSetChanged();

                            }
                        }

                        isFirstPageFirstLoad = false;

                    }else {
                        Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                        Totaltext.setText("0");

                    }

                }

            });

        }

        Totaltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addressDialog=new Dialog(CartActivity.this,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                addressDialog.setContentView(R.layout.addressdialogbox);

                customername=(EditText)addressDialog.findViewById(R.id.CustomerName);
                contactno=(EditText)addressDialog.findViewById(R.id.CustomerContact);
                houseno=(EditText)addressDialog.findViewById(R.id.HouseNO);
                housename=(EditText)addressDialog.findViewById(R.id.Housename);
                colonyname=(EditText) addressDialog.findViewById(R.id.ColonyName);
                streetname=(EditText)addressDialog.findViewById(R.id.Streetname);
                landmark=(EditText)addressDialog.findViewById(R.id.Landmark);
                cityname=(EditText)addressDialog.findViewById(R.id.Cityname);
                pincode=(EditText)addressDialog.findViewById(R.id.PinCode);
                Buybtn=(Button)addressDialog.findViewById(R.id.buybtn);
                final EditText WeightStingText=addressDialog.findViewById(R.id.Weightnumber);
                ImageButton right=addressDialog.findViewById(R.id.Rightbtn);
                final TextView Total=addressDialog.findViewById(R.id.TotalPrice);
                final String[] weightstring = new String[1];
                locationbtn=addressDialog.findViewById(R.id.locationbtn);
                locationText=addressDialog.findViewById(R.id.locationtxt);
                mapprpgress=addressDialog.findViewById(R.id.addresprogress);


                WeightStingText.setVisibility(View.GONE);
                Total.setVisibility(View.GONE);
                right.setVisibility(View.GONE);

                firebaseFirestore.collection("Users").document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                String name = task.getResult().getString("name");
                                String image = task.getResult().getString("image");
                                String cont = task.getResult().getString("contactnumber");
                                String thouse = task.getResult().getString("housename");
                                String thouseno = task.getResult().getString("houseno");
                                String tcolony = task.getResult().getString("colonyname");
                                String tland = task.getResult().getString("landmark");
                                String tcity = task.getResult().getString("cityname");
                                String tpin = task.getResult().getString("pin");
                                String tstreet = task.getResult().getString("street");

                                try {

                                    customername.setText(name);
                                    contactno.setText(cont);
                                    housename.setText(thouse);
                                    houseno.setText(thouseno);
                                    colonyname.setText(tcolony);
                                    landmark.setText(tland);
                                    cityname.setText(tcity);
                                    pincode.setText(tpin);
                                    streetname.setText(tstreet);


                                }catch (Exception e){

                                }
                            }
                        }

                    }
                });

              /* final String ProductPriceString = null;

                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //i will come back to get you

                        weightstring[0] =WeightStingText.getText().toString();
                        Float weightint=Float.valueOf(weightstring[0]),productpriceint=Float.valueOf(ProductPriceString);

                        totalvalue=weightint*productpriceint;

                        Total.setText(String.valueOf(totalvalue));


                    }
                });
*/






                nicknameSpinner = (Spinner)addressDialog.findViewById(R.id.NicknameSpinner);
                addressDialog.show();


                //Creating the ArrayAdapter instance having the country list
                ArrayAdapter aa = new ArrayAdapter(CartActivity.this,android.R.layout.simple_spinner_item,nicklist);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //Setting the ArrayAdapter data on the Spinner
                nicknameSpinner.setAdapter(aa);

                nicknameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        nicknameString=adapterView.getItemAtPosition(i).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(CartActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);



                }
                locationbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mapprpgress.setVisibility(View.VISIBLE);
                        getLocation();
                        Toast.makeText(CartActivity.this, "iam in", Toast.LENGTH_SHORT).show();
                    }
                });


                final String currentUser=firebaseAuth.getCurrentUser().getUid();


                Buybtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        namestring = customername.getText().toString();
                        contactstring = contactno.getText().toString();
                        housenameString = housename.getText().toString();
                        housenumberString = houseno.getText().toString();
                        colonystring = colonyname.getText().toString();
                        StreetnameString = streetname.getText().toString();
                        landstring = landmark.getText().toString();
                        cityString = cityname.getText().toString();
                        PincodeString = pincode.getText().toString();



                        if (!TextUtils.isEmpty(namestring) && !TextUtils.isEmpty(contactstring) &&!TextUtils.isEmpty(housenameString)
                                &&!TextUtils.isEmpty(housenumberString) &&!TextUtils.isEmpty(colonystring)
                                &&!TextUtils.isEmpty(colonystring) && !TextUtils.isEmpty(StreetnameString) &&!TextUtils.isEmpty(landstring) &&
                                !TextUtils.isEmpty(cityString) && !TextUtils.isEmpty(PincodeString)) {

                            dialog.show();

                            Float totalCartout= Float.valueOf(Integer.parseInt(Totaltext.getText().toString()));
                            addressMap = new HashMap<>();
                            addressMap.put("customername", namestring);
                            addressMap.put("phone_number", contactstring);
                            addressMap.put("housename", housenameString);
                            addressMap.put("houseno", housenumberString);
                            addressMap.put("colonyname", colonystring);
                            addressMap.put("streetname", StreetnameString);
                            addressMap.put("landmark", landstring);
                            addressMap.put("cityname", cityString);
                            addressMap.put("pincode", PincodeString);
                            addressMap.put("nickname", nicknameString);
                            addressMap.put("orderid", str);
                            addressMap.put("adresstimestamp", FieldValue.serverTimestamp());
                            addressMap.put("totalpay",String.valueOf(totalCartout));
                            addressMap.put("latitude",String.valueOf(latitude));
                            addressMap.put("longitude",String.valueOf(longitude));









                            startPayment(totalCartout);




                        }





                    }
                });








            }
        });


    }
    public void loadMorePost(){

        if(firebaseAuth.getCurrentUser() != null) {

            Query nextQuery = firebaseFirestore.collection("Cart").document("Product").collection(firebaseAuth.getCurrentUser().getUid()).orderBy("timestamp", Query.Direction.DESCENDING).startAfter(lastVisible).limit(3);


            nextQuery.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (!documentSnapshots.isEmpty()) {

                        try {

                            lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                        }catch (Exception liste){
                            Toast.makeText(CartActivity.this, "No Post To Show", Toast.LENGTH_SHORT).show();

                        }
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                String blogPostId = doc.getDocument().getId();
                                BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                                BlogPost frag = doc.getDocument().toObject(BlogPost.class).withkey("Cart");
                                fraglist.add(frag);
                                blog_list.add(blogPost);


                                cartAdapter.notifyDataSetChanged();
                            }

                        }
                    }


                }
            });

        }




    }

    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {






            CartActivity.this.mHandler.postDelayed(m_Runnable,20000);
        }

    };

    @Override
    protected void onStart() {
        cartAdapter.notifyDataSetChanged();
        super.onStart();

    }

    @Override
    public void onTotalChanged(int sum) {

        Totaltext.setText(String.valueOf(sum));

    }



    public void startPayment(Float payAmount) {
        /**
         * Instantiate Checkout
         */
        int delivery=15;
        int payInt = Math.round(payAmount)+delivery;


        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        //checkout.setImage(R.drawable.sa);
        checkout.setFullScreenDisable(true);

        /**
         * Reference to current activity
         */
        final Activity activity = CartActivity.this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Sa");

            /**
             * Description can be anything
             * eg: Order #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "SRPid12");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", payInt*100);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in starting Razorpay Checkout" + e, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onPaymentSuccess(String s) {

        Upload();

    }

    @Override
    public void onPaymentError(int i, String s) {

        failMap = new HashMap<>();
        failMap.put("customername", namestring);
        failMap.put("type_of_order", "Transcation Failed");
        failMap.put("phone_number", contactstring);
        failMap.put("housename", housenameString);
        failMap.put("houseno", housenumberString);
        failMap.put("colonyname", colonystring);
        failMap.put("streetname", StreetnameString);
        failMap.put("landmark", landstring);
        failMap.put("cityname", cityString);
        failMap.put("pincode", PincodeString);
        failMap.put("nickname", nicknameString);
        failMap.put("orderid", ProductId);
        failMap.put("adresstimestamp", FieldValue.serverTimestamp());
        failMap.put("product_name","");
        failMap.put("price","");
        failMap.put("desc","");
        failMap.put("image_url","");
        failMap.put("image_thumb","");
        failMap.put("weight", "");
        failMap.put("totalpay",String.valueOf(totalvalue));
        FaildDialogbox=new Dialog(CartActivity.this);
        FaildDialogbox.setContentView(R.layout.faildlist_dialog);



        dialog.dismiss();

        firebaseFirestore.collection("SreeOrders").document("FailOrder").collection("FailTransList").add(failMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {


                addressDialog.dismiss();
                FaildDialogbox.show();


            }
        });


    }

    public void Upload(){


        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection(currentUser).add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                dialog.dismiss();
                addressDialog.dismiss();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("IjostVW1oAfaMKgSHFGGwJniQdJ2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CartActivity.this, "Your Order is Placed to sathesh Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("1aEcTGqXcgWaeO8TLJyMhULhQ633").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CartActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("gey5LO3faNWCl5SVqaNT6bQYhGs1").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CartActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        firebaseFirestore.collection("SreeOrders").document("AllOrders").collection("YJqdm0qcfCbEhc3spXxRGOyP0jy2").add(addressMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(CartActivity.this, "Your Order is Placed sunny Successfully", Toast.LENGTH_SHORT).show();

            }
        });








    }


    @Override
    public void onOrderIdWeight(String[] total) {
        //str=Arrays.toString(total);




        List<String> list = new ArrayList<String>(Arrays.asList(total));

        list.removeAll(Collections.singleton(null));
        list.removeAll(Collections.singleton(""));



        String[] stockArr = new String[list.size()];

        stockArr = list.toArray(stockArr);

        str=Arrays.toString(stockArr);



    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
            mapprpgress.setVisibility(View.GONE);

           /* double lat = location.getLatitude(), lon = location.getLongitude();
            String la=String.valueOf(lat),lo=String.valueOf(lon);
            Uri gmmIntentUri = Uri.parse("google.navigation:q="+la+","+lo);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);*/
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(CartActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }


}
