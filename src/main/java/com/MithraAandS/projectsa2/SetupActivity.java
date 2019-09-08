package com.MithraAandS.projectsa2;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import com.mikhaellopez.circularimageview.CircularImageView;

import id.zelory.compressor.Compressor;

public class SetupActivity extends AppCompatActivity {

    private CircularImageView setupImage;
    private Uri mainImageURI = null;
    private String user_id;
    private boolean isChanged = false;
    private EditText setupName,contactno,housename,colonyname,landmark,cityname,pincode,houseno,street;
    private Button setupBtn;
    private ProgressBar setupProgress;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Bitmap compressedImageFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar setupToolbar = findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        checkConnection();

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setupImage=findViewById(R.id.setup_image);
        setupName = findViewById(R.id.setup_name);
        setupBtn = findViewById(R.id.setup_btn);
        setupProgress = findViewById(R.id.setup_progress);
        contactno=findViewById(R.id.Rcontactnumber);
        housename=findViewById(R.id.Rhousename);

        houseno=findViewById(R.id.RhouseNo);
        colonyname=findViewById(R.id.RColonyName);
        landmark=findViewById(R.id.Rlandmark);
        cityname=findViewById(R.id.Rcityedit);
        pincode=findViewById(R.id.Rpincode);
        street=findViewById(R.id.RStreetname);


        setupProgress.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

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


                            mainImageURI = Uri.parse(image);

                            setupName.setText(name);
                            contactno.setText(cont);
                            housename.setText(thouse);
                            houseno.setText(thouseno);
                            colonyname.setText(tcolony);
                            landmark.setText(tland);
                            cityname.setText(tcity);
                            pincode.setText(tpin);
                            street.setText(tstreet);
                        }catch (Exception e){

                        }

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(SetupActivity.this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);


                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

                setupProgress.setVisibility(View.INVISIBLE);
                setupBtn.setEnabled(true);

            }
        });


        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_name = setupName.getText().toString();
                final String contactstring = contactno.getText().toString();
                final String houseString = housename.getText().toString();
                final String housenoString = houseno.getText().toString();
                final String colonystring = colonyname.getText().toString();
                final String landmarkString = landmark.getText().toString();
                final String cityString = cityname.getText().toString();
                final String streetString = street.getText().toString();
                final String pinString = pincode.getText().toString();


                if (!TextUtils.isEmpty(user_name) && mainImageURI != null && !TextUtils.isEmpty(contactstring)
                && !TextUtils.isEmpty(housenoString)&& !TextUtils.isEmpty(houseString) && !TextUtils.isEmpty(colonystring)
                        && !TextUtils.isEmpty(landmarkString)&& !TextUtils.isEmpty(cityString)&& !TextUtils.isEmpty(streetString)
                        && !TextUtils.isEmpty(pinString)) {

                    setupProgress.setVisibility(View.VISIBLE);

                    if (isChanged) {

                        user_id = firebaseAuth.getCurrentUser().getUid();

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(SetupActivity.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();

                        UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeFirestore(task, user_name,contactstring,houseString,housenoString,colonystring,landmarkString,cityString,streetString,pinString);

                                } else {

                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                                    setupProgress.setVisibility(View.INVISIBLE);

                                }
                            }
                        });

                    } else {

                        storeFirestore(null, user_name,contactstring,houseString,housenoString,colonystring,landmarkString,cityString,streetString,pinString);

                    }

                }

            }

        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               BringImagePicker();

            }

        });


    }

    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String user_name,String contactSt,String house,String houseno,String colony
    ,String land,String city,String pin,String street) {



        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getDownloadUrl();

        }  else {

            download_uri = mainImageURI;

        }



        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("image", download_uri.toString());
        userMap.put("contactnumber",contactSt);
        userMap.put("housename",house);
        userMap.put("houseno",houseno);
        userMap.put("colonyname",colony);
        userMap.put("landmark",land);
        userMap.put("cityname",city);
        userMap.put("pin",pin);
        userMap.put("street",street);

        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(SetupActivity.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

                setupProgress.setVisibility(View.INVISIBLE);

            }
        });


    }

    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                setupImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();



            }
        }

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
            Toast.makeText(SetupActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            final Dialog addressDialog=new Dialog(SetupActivity.this);
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
}
