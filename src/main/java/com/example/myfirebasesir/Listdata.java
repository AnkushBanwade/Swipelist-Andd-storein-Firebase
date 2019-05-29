package com.example.myfirebasesir;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.myfirebasesir.listener.AdapterClickListener;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.jar.Attributes;

import static com.google.firebase.firestore.FieldValue.delete;

public class Listdata extends AppCompatActivity implements AdapterClickListener {
    SwipeMenuListView list;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    final int PIC_CROP = 3;
    private Uri picUri;
    private static final int CAMERA_REQUEST_CODE = 10;
    protected static final int CAMERA_REQUEST = 99;
    protected static final int GALLERY_REQUEST = 1;
    private static final int REQUEST_ACESS_STORAGE=3;
    private static final int REQUEST_ACESS_CAMERA=2;
    private Uri uri;
    File mPhotoFile;
    Bitmap bitmap;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;
    final int CAMERA_CAPTURE = 102;

    String userChoosenTask;
    User userinfo;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    ArrayList<String> keysf;
    FirebaseStorage storage;
    static StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    static DatabaseReference reference;
static final int REQUEST_TAKE_PHOTO=1;
    int i=0;
    File out;
    Context context=Listdata.this;
    ImageView image;
    String FilePath;
    private Uri filePath;
    String url;
    ArrayList<User> userArrayList;
    File photoFile;

    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    ImageButton yes;
    ImageButton no;
    ImageView edit;
    User abc;
    ProgressDialog progressDialog;
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdata);
        storage = FirebaseStorage.getInstance("gs://my-firebasesir.appspot.com");
        storageReference = storage.getReference();
        edit=(ImageView) findViewById(R.id.edit) ;
        list=(SwipeMenuListView) findViewById(R.id.list);
        //ref=database.getReference("Profile");
        FirebaseApp.initializeApp(Listdata.this);

        // FirebaseDatabase database = FirebaseDatabase.getInstance();
        Firebase.setAndroidContext(getApplicationContext());


        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

        reference=firebaseDatabase.getReference();
        userArrayList =new ArrayList<User>();
        keysf =new ArrayList<String>();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
int coubt=1;
        adapter= new ListAdapter(context,userArrayList,this);

        list.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu){
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xff)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setIcon(R.drawable.ic_deleteonly);
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
menu.removeMenuItem(deleteItem);
                // set a icon
                deleteItem.setIcon(R.drawable.fibal);
                // add to menu
                menu.addMenuItem(deleteItem);

            }

        };
        list.setMenuCreator(creator);

        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            private static final String TAG ="Click" ;

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                       delete(position);
                        //Log.d(TAG, "onMenuItemClick: clicked item " + index);
                        break;
                    case 1:
                        click(position);
                        Log.d(TAG, "onMenuItemClick: clicked Edit item " + index);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
            DatabaseReference childrefe=reference.child("Profile");

        reference.child("Profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting userinfo

                    userinfo = postSnapshot.getValue(User.class);

                    keysf.add(postSnapshot.getKey());
                    //  layoutResourceId= (int) dataSnapshot.getChildrenCount();

                    userArrayList.add(userinfo);
                }
                Toast.makeText(context, "DATAUPDAYE"+i, Toast.LENGTH_SHORT).show();
                adapter.setData(userArrayList);
                adapter.notifyDataSetChanged();
                i++;
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               Toast.makeText(Listdata.this, "rhfhfh", Toast.LENGTH_SHORT).show();
            }

        }
        );


        progressDialog.dismiss();

       /* childrefe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //abc=  dataSnapshot.child("Profile").getValue(name);

                dataSnapshot.getKey();
                abc=  dataSnapshot.child("Profile").getValue(User.class);
                long value=dataSnapshot.getChildrenCount();
                //Log.d(TAG,"no of children: "+value);

                for(int i=0;i<value;i++){
                    Toast.makeText(Listdata.this,"TaskTitle = "+dataSnapshot.getChildren(),Toast.LENGTH_LONG).show();
  name= String.valueOf(dataSnapshot.child(name));
  address=String.valueOf(dataSnapshot.child(address));
  url=String.valueOf(dataSnapshot.child(url));
                }
                new DownloadImage().execute(url);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Listdata.this, "Cannot Doenload to ffirebase", Toast.LENGTH_SHORT).show();

            }
        });
*/
        /*FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference reference=firebaseDatabase.getReference();

        DatabaseReference childrefe=reference.child("Profile");
*/
        //  Toast.makeText(this, "Listsddaa", Toast.LENGTH_SHORT).show();
        ArrayList<User> arrayList= new ArrayList<>();
        ArrayList<Bitmap> arrayList1= new ArrayList<>();

        User user;
        //Intent mIntent = getIntent();
        //  String imagePath = mIntent.getStringExtra("bitmap");
        //File imgFile = new  File(imagePath);


      /*  Toast.makeText(this, "intentcompleted", Toast.LENGTH_SHORT).show();
        name = (String) mIntent.getSerializableExtra("name");
        address = (String) mIntent.getSerializableExtra("address");
        arrayList= (ArrayList<User>) mIntent.getSerializableExtra("users");
        Bitmap bitmap = mIntent.getParcelableExtra("image");
      */  //Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());


    }


    public void Edit(View view) {
     /*   dialogBuilder = new AlertDialog.Builder(Listdata.this);
        View layoutView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        dialogBuilder.setView(layoutView);

        Button yes = layoutView.findViewById(R.id.Yes);
        image = layoutView.findViewById(R.id.image);
        final EditText Name = layoutView.findViewById(R.id.Name);
        final EditText Adress = layoutView.findViewById(R.id.Address);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

            }
        });


        Button no = layoutView.findViewById(R.id.No);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // uploadImage();
                String sName = Name.getText().toString();
                String address = Adress.getText().toString();

                url = FilePath;
                upDateUsers(sName, address, url);
                alertDialog.dismiss();
            }

            private void upDateUsers(String sName, String address, String url) {
                //  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Profile").child(name);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                reference = firebaseDatabase.getReference("Profile").child("name");

                //reference=reference.child("Profile").child(name);


                User user = new User(url, sName, address);
                reference.setValue(user);
                Toast.makeText(context, "Change Uplaodes", Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog = dialogBuilder.create();
        yes = layoutView.findViewById(R.id.No);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.show();*/

    }


        // negativeDialog = findViewById(R.id.No);

        // reference.getInstance().getReference().getRoot().child("locations").child(pushKey);
      //  reference.getRoot().child("Profile").child(pushKey).setValue(name);
        //reference.getRef().child("Profile").child(pushKey).child("address").setValue("Ankush");
        //reference.child("Profile").child(getString());




    @Override
    public void delete(final int position) {
        reference.getRef().child("Profile").child(keysf.get(position)).setValue(null);
userArrayList.remove(position);
adapter.notifyDataSetChanged();
/*
list.setOnClickListener(new AdapterView.OnClickListener() {
    @Override
                 public void onClick(View v) {
                  userArrayList.remove(position);
                 adapter.notifyDataSetChanged();
    }
});
*/
        // Create a storage reference from our app


// Create a reference to the file to delete
        /*StorageReference desertRef = storageRef.child("images/");
        desertRef.child().setValue(null);
        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // File deleted successfully
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
            }
        });*/


       // reference.child("Profile").child(getString());
    }

   /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
            { onSelectFromGalleryResult(data);
            filePath=data.getData();}
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            filePath=data.getData();

        }
    }
  */  /*private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(thumbnail);
    }*/

    /*private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        image.setImageBitmap(bm);
    }
    */
    private void openCameraApp() {

    }
    public static boolean checkPermission(String permission, Context context) {
        int statusCode = ContextCompat.checkSelfPermission(context, permission);
        return statusCode == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermission(AppCompatActivity activity, String[] permission, int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            Toast.makeText(activity, "Application need permission", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

    public static void requestPermission(Fragment fragment, String[] permission, int requestCode) {
        fragment.requestPermissions(permission, requestCode);
    }
    private void handleCamera(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this)) {
                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(picIntent, CAMERA_REQUEST);

                Toast.makeText(context, "Build.VERSION_CODES.M", Toast.LENGTH_SHORT).show();
            }else{
                }
        }else{
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST &&  resultCode == RESULT_OK
                && data != null && data.getData() != null) {
        /*if (resultCode ==RESULT_OK) {
            if (resultCode == PICK_IMAGE_REQUEST) {
*/

                filePath = data.getData();
                image.setImageURI(filePath);

                 uploadImage();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
                //out = new File(String.valueOf(storageReference));


          /*  try {
                try {
                    //   Toast.makeText(this, "data" + data, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);

                // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                // bitmap=(Bitmap)BitmapFactory.decodeFile(out.getAbsolutePath());
                // Bitmap bitmap = BitmapFactory.decodeFile(out.getAbsolutePath());

                //     Toast.makeText(this, "bitmap"+bitmap, Toast.LENGTH_SHORT).show();
               // image.setImageURI(filePath);
                // uploadImage();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
        if (requestCode == CAMERA_REQUEST &&  resultCode == RESULT_OK
                && data != null  ) {

            if (requestCode == CAMERA_REQUEST &&  resultCode == RESULT_OK
                    && data != null ) {
                Toast.makeText(context, "Camera onActivity"+data, Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                if(extras.keySet().contains("data"))
                {
                    bitmap = (Bitmap) extras.get("data");
                   }


                bitmap=(Bitmap)data.getExtras().get("data");
                //filePath = data.getData();
               filePath= getImageUri( context, bitmap);
                image.setImageBitmap(bitmap);
                uploadImage();
                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                Toast.makeText(context, "bitmap"+bitmap, Toast.LENGTH_SHORT).show();
                //image.setImageBitmap(bitmap);

            }
            //  uploadImage();


/*
            filePath=data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            // filePath = bitmap;
               // Bundle extras = data.getExtras();
                //bitmap = (Bitmap) extras.get("data");
                image.setImageBitmap(bitmap);
                //uploadImage();*/
                // Picasso.with(this).load(filePath)
                //.resize(60,60)
                // .centerCrop().into(image);

        }
        }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }









    @Override
    public void click(final int position) {

        i=position;

       {
        dialogBuilder = new AlertDialog.Builder(Listdata.this);
        View layoutView = getLayoutInflater().inflate(R.layout.custom_layout, null);
        dialogBuilder.setView(layoutView);
           alertDialog = dialogBuilder.create();
           no = layoutView.findViewById(R.id.No);
           dialogBuilder.setView(layoutView);
           alertDialog = dialogBuilder.create();
           alertDialog.show();


           ImageButton yes = layoutView.findViewById(R.id.Yes);


           image = layoutView.findViewById(R.id.image);
           Picasso.with(getApplicationContext())
                   .load(userArrayList.get(position).getUri_img())
                   .centerCrop()
                   .fit()
                   .into(image);

        final EditText Name = layoutView.findViewById(R.id.Name);
        Name.setText(userArrayList.get(position).getName());

        final EditText Adress = layoutView.findViewById(R.id.Address);

           Adress.setText(userArrayList.get(position).getAddress());


           image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              //  selectImage();
                final CharSequence[] items = { "Take Photo", "Choose from Library",
                        "Cancel" };
                AlertDialog.Builder builder = new AlertDialog.Builder(Listdata.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {


                        if (items[item].equals("Take Photo")) {
                            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                                if(checkPermission(Manifest.permission.CAMERA,Listdata.this)){
                                    handleCamera();
                                }else{
                                                                        //requestPermission(Listdata.this,new String[]{Manifest.permission.CAMERA},REQUEST_ACESS_CAMERA);
                                }
                            }else{
                            }


                                    try {
                                         /*   Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                                        File imageFile = new File(imageFilePath);
                                        picUri = Uri.fromFile(imageFile); // convert path to Uri
                                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                                        startActivityForResult(takePictureIntent, CAMERA_CAPTURE);*/
                                        userChoosenTask="Take Photo";
                                    }catch (ActivityNotFoundException anfe){
                                        //display an error message
                                        String errorMessage = "Whoops - your device doesn't support capturing images!";
                                        Toast.makeText(Listdata.this, errorMessage, Toast.LENGTH_SHORT).show();
                                    }
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(Listdata.this,
                                            Manifest.permission.CAMERA)) {




                               }






                            /*if(result)
                                cameraIntent();
                            */
                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); //uploadImage();

                                userChoosenTask = "Choose from Library";
                                //  galleryIntent();

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                }


                );

              //
                  //
                builder.show();

                /*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
*/

            }

                                    //    uploadImage();
               /*private void cameraIntent()
               {
                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(intent, REQUEST_CAMERA);
               }
               private void galleryIntent()
               {
                   Intent intent = new Intent();
                   intent.setType("image/*");
                   intent.setAction(Intent.ACTION_GET_CONTENT);//
                   startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
               }*/

        }



        );


           ImageButton no = layoutView.findViewById(R.id.No);
          // uploadImage();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String furl=FilePath;
                Log.e(furl,"FurlValue" );

                String sName = Name.getText().toString();
                String address = Adress.getText().toString();
                upDateUsers(sName, address, furl);
                userArrayList.get(position).setName(sName);
                userArrayList.get(position).setAddress(address);
               // uploadImage();


             //   userArrayList.get(position).setUri_img(url );

                //String tpaddress=  userArrayList.get(position).getAddress();

                // final String url= userArrayList.get(position).getUri_img();

                alertDialog.dismiss();

               // uploadImage();
                userArrayList.get(position).setUri_img(furl );

            }

            private void upDateUsers(String tpName, String tpaddress, String furl) {
                //uploadImage();
                reference.getRef().child("Profile").child(keysf.get(position));

                String key=keysf.get(position);
                User post = new User(tpName, tpaddress, furl);
                Map<String, Object> postValues = post.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/Profile/" + key, postValues);
                //childUpdates.put("/Profile/" + name + "/" + key, postValues);

                reference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "data updated" , Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
no.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        alertDialog.dismiss();
    }
});
    } }
    private void uploadImage() {

        final StorageReference ref = storage.getReference().child("images/" + UUID.randomUUID());
        // Toast.makeText(this, "the result comes" + filePath, Toast.LENGTH_SHORT).show();
        //Log.e(TAG, "errormessage: " + filePath);
        filePath.getPath();

     /*   ref.getDownloadUrl();
        ref.child("Photos").child(filePath.getLastPathSegment());*/
        ref.getDownloadUrl();
        ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                //progressDialog.dismiss();


                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        FilePath = uri.toString();

                        url=FilePath;
progressDialog.dismiss();
                        //String image = taskSnapshot.getDownloadUrl().toString());

                        //Log.e(TAG, "onSuccess: "+filePath );
                        Toast.makeText(Listdata.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


             /*   taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {


                        FilePath = uri.toString();

                        url=FilePath;

                        //String image = taskSnapshot.getDownloadUrl().toString());

                        //Log.e(TAG, "onSuccess: "+filePath );
                        Toast.makeText(Listdata.this, "Uploaded", Toast.LENGTH_SHORT).show();


                    }
                });*/


            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //  progressDialog.dismiss();
                        Toast.makeText(Listdata.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
               /* .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        //progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });*/
    }

}
/*  private void uploadImage() {

        final StorageReference ref =  storageReference.child("images/" + UUID.randomUUID().toString()+"/Photos");

        ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downUri = task.getResult();
                    url = downUri.toString();
                    //Log.d(TAG, "onComplete: Url: "+ downUri.toString());
                }
            }
        });

    }*/