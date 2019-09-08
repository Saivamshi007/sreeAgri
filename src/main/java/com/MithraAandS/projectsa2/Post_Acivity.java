package com.MithraAandS.projectsa2;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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
import java.util.Random;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;

public class Post_Acivity extends AppCompatActivity {

    private Toolbar newPostToolbar;

    private ImageView newPostImage;
    private EditText newPostDesc,Product_name,Price,Quantity,PricePer5,PricePer1,AboutProduct,ProductInfo,WeightPolicy,UsesText;
    private Button newPostBtn;

    private Uri postImageUri = null;
    private TextView SelectedTextview;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;
    String[] country = { "organic","In-organic","Flowers","Rice","OrganicRice","Fruits"};
    private String SelectedCatagory;
    private  Uri downloadUri,downloadthumbUri;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__acivity);
        newPostToolbar = findViewById(R.id.Post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();


        newPostImage = findViewById(R.id.Product_img);
        newPostDesc = findViewById(R.id.Product_description);
        newPostBtn = findViewById(R.id.Upload_btn);
        newPostProgress = findViewById(R.id.Post_Progressbar);
        Product_name=(EditText)findViewById(R.id.Product_Name);
        Price=(EditText)findViewById(R.id.ProductPrice);
        Quantity=(EditText)findViewById(R.id.Quantity);

        AboutProduct=(EditText)findViewById(R.id.AboutProduct);
        ProductInfo=(EditText)findViewById(R.id.OtherProductInfo);
        WeightPolicy=(EditText)findViewById(R.id.variableWeigtht);
        UsesText=(EditText)findViewById(R.id.UsesEdittext);


        Spinner spin = (Spinner) findViewById(R.id.spinner);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SelectedCatagory= adapterView.getItemAtPosition(i).toString();



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setInitialCropWindowPaddingRatio(0)
                        .start(Post_Acivity.this);

            }
        });
        final AlertDialog dialog= new SpotsDialog.Builder().setContext(Post_Acivity.this).build();

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String desc = newPostDesc.getText().toString();
                final String Postname=Product_name.getText().toString();
                final String PriceString=Price.getText().toString();
                final String QuantityString=Quantity.getText().toString();
                final String aboutString=AboutProduct.getText().toString();
                final String ProdctInfoString=ProductInfo.getText().toString();
                final String WeightString=WeightPolicy.getText().toString();
                final String UsesString=UsesText.getText().toString();


                if(!TextUtils.isEmpty(UsesString)&&!TextUtils.isEmpty(WeightString)&&!TextUtils.isEmpty(ProdctInfoString)&&!TextUtils.isEmpty(aboutString)&&!TextUtils.isEmpty(Postname)&&!TextUtils.isEmpty(desc) && !TextUtils.isEmpty(PriceString)&&!TextUtils.isEmpty(QuantityString) &&postImageUri!=null){
                    Toast.makeText(Post_Acivity.this, "U r In", Toast.LENGTH_SHORT).show();
                    newPostProgress.setVisibility(View.VISIBLE);
                    dialog.show();

                    final String randomName = UUID.randomUUID().toString();

                    // PHOTO UPLOAD
                    File newImageFile = new File(postImageUri.getPath());
                    try {

                        compressedImageFile = new Compressor(Post_Acivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    // PHOTO UPLOAD


                    UploadTask filePath = storageReference.child("post_images").child(randomName + ".jpg").putBytes(imageData);
                    filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                            final String downloadUri = task.getResult().getDownloadUrl().toString();

                            if(task.isSuccessful()){

                                File newThumbFile = new File(postImageUri.getPath());
                                try {

                                    compressedImageFile = new Compressor(Post_Acivity.this).setQuality(1)
                                            .compressToBitmap(newThumbFile);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] thumbData = baos.toByteArray();
                                final int min =10 ;
                                final int max = 1000;
                                final int random = new Random().nextInt((max - min) + 1) + min;

                                UploadTask uploadTask = storageReference.child("post_images/thumbs")
                                        .child(randomName + ".jpg").putBytes(thumbData);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put("image_url", downloadUri.toString());
                                        postMap.put("image_thumb", downloadthumbUri.toString());
                                        postMap.put("desc", desc);
                                        postMap.put("user_id", current_user_id);
                                        postMap.put("timestamp", FieldValue.serverTimestamp());
                                        postMap.put("quantity",QuantityString);
                                        postMap.put("price",PriceString);
                                        postMap.put("product_name",Postname);
                                        postMap.put("aboutString",aboutString);
                                        postMap.put("ProductInfo",ProdctInfoString);
                                        postMap.put("WeightString",WeightString);
                                        postMap.put("UsesString",UsesString);
                                        postMap.put("category",SelectedCatagory);
                                        postMap.put("Orderid","SAPID"+random);


                                        /*firebaseFirestore.collection("Posts").document("allRice").collection("Product").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                Toast.makeText(Post_Acivity.this, "yes", Toast.LENGTH_SHORT).show();


                                            }
                                        });*/





                                        firebaseFirestore.collection("Posts").document(SelectedCatagory).collection("Product").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                if(task.isSuccessful()){


                                                    Toast.makeText(Post_Acivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(Post_Acivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                } else {


                                                }

                                                newPostProgress.setVisibility(View.INVISIBLE);

                                                dialog.dismiss();

                                            }
                                        });

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        //Error handling

                                    }
                                });


                            } else {

                                newPostProgress.setVisibility(View.INVISIBLE);

                            }

                        }
                    });


                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


}
