package com.example.myfirebasesir;


//import android.content.Intent;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TextView etValue;
    ProgressDialog progressDialog;
    EditText address,fetch,name,mobile;
    ArrayList<User> users = new ArrayList<User>();
    private static final Object Manifest = null;
    private static final String TAG = "Main";
    private static final Object Context = "abc";
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    File out;
    String uri_img;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    DatabaseReference reference;
    User abc;
    Bitmap bitmap;
    Button button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button2= (Button)findViewById(R.id.btnSubmit2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Listdata.class);
                startActivity(intent);

            }
        });


        FirebaseApp.initializeApp(MainActivity.this);

        name = (EditText)findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        btnUpload = (Button) findViewById(R.id.btnSubmit);

        //DatabaseReference childrefe=reference.child("User");

        imageView = (ImageView)findViewById(R.id.img);

        Firebase.setAndroidContext(getApplicationContext());


        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();

        DatabaseReference childrefe=reference.child("Profile");




        storage = FirebaseStorage.getInstance("gs://my-firebasesir.appspot.com");
        storageReference = storage.getReference();


        imageView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

            }
        });
    }

    /*private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, 1);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            filePath = data.getData();
            out = new File(String.valueOf(storageReference));


            try {
                try {
                    Toast.makeText(this, "data" + data, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // bitmap=(Bitmap)BitmapFactory.decodeFile(out.getAbsolutePath());
                // Bitmap bitmap = BitmapFactory.decodeFile(out.getAbsolutePath());

                Toast.makeText(this, "bitmap"+bitmap, Toast.LENGTH_SHORT).show();
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

/*        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();*/
        StorageReference ref = storage.getReference().child("images/" + UUID.randomUUID().toString());
        // Toast.makeText(this, "the result comes" + filePath, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "errormessage: " + filePath);
        filePath.getPath();
        ref.getDownloadUrl();
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //progressDialog.dismiss();
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String FilePath = uri.toString();
                        uri_img=FilePath;

                        //String image = taskSnapshot.getDownloadUrl().toString());

                        Log.e(TAG, "onSuccess: "+filePath );
                        Toast.makeText(MainActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        save();
                    }
                });

                // Uri downloadUri = taskSnapshot.getUploadSessionUri();


                //Intent myIntent = new Intent(MainActivity.this, Listdata.class);
                //  myIntent.putExtra("name", name);
//        myIntent.putExtra("id", id);
//        //myIntent.putExtra("bitmap",bitmap);
//        myIntent.putExtra("bitmap", bitmap.extractAlpha());
//
//        myIntent.putExtra("users",users);

                //  startActivity(myIntent);


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        //progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
    }
    public void save(){


        int i=1;
        //int id= Integer.parseInt(ide.getText().toString());
        String name1=name.getText().toString();

        String address1=address.getText().toString();
        String uri_img1=uri_img;
        if(TextUtils.isEmpty(name1)){
            name.setError("Name Required");
        }
        if(TextUtils.isEmpty(address1)){
            address.setError("Address Required");
        }

        insertItem(name1, address1,uri_img1);


        User user=new User(name1, address1,uri_img1);

        user.setName(name1);
        user.setAddress(address1);
        user.setUri_img(uri_img1);
        //user.setId(id);

       /* Intent myIntent = new Intent(MainActivity.this, Listdata.class);
        myIntent.putExtra("name", name1);
        myIntent.putExtra("address", address1);
        //myIntent.putExtra("bitmap",bitmap);
        myIntent.putExtra("bitmap", bitmap);

        myIntent.putExtra("users",users);
//
        startActivity(myIntent);*/
        //   Firebase db = new Firebase(Config.url);

        reference.child("Profile").push().setValue(user);

        //reference.child("Profile2").push().setValue(user);

        Toast.makeText(this, "CompleteActivitMAIN", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(MainActivity.this, Listdata.class);
        progressDialog.dismiss();
        startActivity(myIntent);
        //storageReference.child("User").child("person"+id);
        i++;
    }
    private void insertItem(String name1,String line1, String line2) {

        users.add(new User(name1, line1, line2));

    }


}
