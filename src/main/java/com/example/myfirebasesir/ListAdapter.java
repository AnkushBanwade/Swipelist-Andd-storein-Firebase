package com.example.myfirebasesir;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfirebasesir.listener.AdapterClickListener;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListAdapter extends BaseAdapter {
    private Context context; //    private String name;
    private Uri filePath;
    Bitmap bitmap;

    ProgressDialog progressDialog;
    FirebaseStorage storage;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    DatabaseReference reference;
    String pushKey;
    int i=0;
    ImageView image;
    int layoutResourceId;
    String FilePath;
    String name;
    String address;
    String url;
    ArrayList<User> userArrayList;
    ProgressDialog mProgressDialog;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    Button yes;
    Button no;

    User abc;

    AdapterClickListener adapterClickListener;

    ArrayList<User> arrayList = new ArrayList<User>();


    /*public ListAdapter(Context context, int resource, int textViewResourceId, List<String> objects, Activity context1, String[] name, String[] id, Bitmap[] imgid) {
        super(context, resource, textViewResourceId, objects);
        this.context = context1;
        this.name = name;
        this.id = id;
        this.imgid = imgid;
    }*/

    public ListAdapter(Context context,  ArrayList<User> arrayList, AdapterClickListener adapterClickListener) {
       // super(listdata,R.layout.litem_list,id);name
        this.context=context;
        this.adapterClickListener =adapterClickListener;
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();


        this.arrayList=arrayList;
    }


    public void setData(ArrayList<User> arrayList ){
        this.arrayList = arrayList;
    }


    @Override
    public int getCount() {

        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.litem_list, parent, false);
        }

        User currentItem = (User) getItem(position);

        //LayoutInflater inflater=listdata.getLayoutInflater();
        //rowView=inflater.inflate(R.layout.litem_list, null,true);
        TextView titleText =  convertView.findViewById(R.id.id);
        ImageView imageV =  convertView.findViewById(R.id.imge);
        TextView subtitleText = convertView.findViewById(R.id.name);
        ImageView edit= convertView.findViewById(R.id.edit);
        ImageView delete= convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterClickListener.delete(position);
            }
        });
        titleText.setText(currentItem.getName());
        subtitleText.setText(currentItem.getAddress());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                adapterClickListener.click(position);
            }
        });

       // new DownloadImage().execute(currentItem.getUri_img());

        Picasso.with(this.context)
                .load(currentItem.getUri_img())

                .centerCrop()
             .fit()
                .into(imageV);

        progressDialog.dismiss();
      //  imageV.setImageBitmap(bitmap);
        // imageView.setImageResource(imgid[position]);.transform(new CircleTransform(50,0))
        //this.getContext()
        return convertView;


    }




}
