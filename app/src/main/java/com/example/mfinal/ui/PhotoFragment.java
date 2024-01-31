package com.example.mfinal.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfinal.R;
import com.example.mfinal.models.Posts;
import com.example.mfinal.models.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


public class PhotoFragment extends Fragment {

    private static final int CAMERA_REQUEST = 1888;
    ImageView img;
    Button upload,camera;
    LinearLayout photo_layout;
    Bitmap photo;
    ArrayList<String> selected = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    String photo_url,uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo,container,false);
        storageReference = FirebaseStorage.getInstance().getReference();
        camera = root.findViewById(R.id.camera);
        upload = root.findViewById(R.id.upload);
        photo_layout = root.findViewById(R.id.photo_layout);
        img= root.findViewById(R.id.photo);
        getLabel();

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto(view);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkSelectedcheckbox();
                uploadImageToFirebaseStorage(photo);
            }
        });
        return root;
    }
    public void takePhoto(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }
    public void onActivityResult(int requestCode,int resultCode , Intent data){
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            img.setImageBitmap(photo);
            upload.setEnabled(true);

        }
    }
    public void getLabel(){
        photo_layout.removeAllViews();
        db.collection("Labels").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CheckBox checkBox = new CheckBox(requireContext());
                        checkBox.setText(document.getString("label"));
                        checkBox.setTag(document.getId());
                        photo_layout.addView(checkBox);
                    }
                }else{
                    Toast.makeText(getContext(),"Huh? This isn't supposed to happen.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void uploadImageToFirebaseStorage(Bitmap bitmap) {
        StorageReference imageRef = storageReference.child("images/"+ UUID.randomUUID().toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Hey nice photo!", Toast.LENGTH_SHORT).show();
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {
                        photo_url = downloadUrl.toString();
                        System.out.println(photo_url);

                        addPost();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getContext(), "Where link?", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Where Photo?", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void addPost(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Timestamp timestamp = new Timestamp(new Date());
        uid = user.getUid();
        Posts post = new Posts(photo_url,uid,timestamp,selected);
        db.collection("Posts").add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getContext(), "And yet another post.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No post for you!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void checkSelectedcheckbox(){
        for(int i=0 ; i<photo_layout.getChildCount();i++){
            View childview = photo_layout.getChildAt(i);
            if(childview instanceof  CheckBox){
                CheckBox checkBox2 = (CheckBox) childview;
                if(checkBox2.isChecked()){
                    String checklabel = (String) checkBox2.getText();
                    selected.add(checklabel);
                }
            }
        }
    }
}