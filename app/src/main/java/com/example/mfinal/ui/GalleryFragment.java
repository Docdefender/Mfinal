package com.example.mfinal.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.mfinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GalleryFragment extends Fragment {

    FirebaseFirestore firestore;
    LinearLayout gallerylayout;
    Spinner spinner;

    String first,last;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery,container,false);

        firestore = FirebaseFirestore.getInstance();
        gallerylayout = root.findViewById(R.id.gallerylayout);
        spinner = root.findViewById(R.id.spinner);
        getData();
        return root;
    }
    public void getData(){
        firestore.collection("Labels").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> labels = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String label = document.getString("label");
                                labels.add(label);
                            }
                            if(!labels.isEmpty()){
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                                        android.R.layout.simple_spinner_item,labels);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(adapter);
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        String selectedlabel = spinner.getSelectedItem().toString();
                                        Toast.makeText(getContext(), selectedlabel, Toast.LENGTH_SHORT).show();
                                        getPost(selectedlabel);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }else{
                                Toast.makeText(getContext(), "Something is not correct!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No, no. Try Again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void getPost(String labels){
        gallerylayout.removeAllViews();
        CollectionReference postscollection = firestore.collection("Posts");
        Query query = postscollection.whereArrayContains("labels",labels);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String uid = document.getString("uid");
                    String url = document.getString("url");

                    ImageView img = new ImageView(requireContext());
                    LinearLayout yan = new LinearLayout(requireContext());
                    TextView user = new TextView(requireContext());

                    DocumentReference userdocref = firestore.collection("Users").
                            document(uid);
                    userdocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()){
                                first = documentSnapshot.getString("firstname");
                                last = documentSnapshot.getString("lastname");
                                user.setText(first+" "+last);
                            }
                        }
                    });
                    Glide.with(requireContext()).load(url).transition(DrawableTransitionOptions.withCrossFade()).into(img);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(600,600);
                    img.setLayoutParams(layoutParams);

                    yan.setLayoutParams(new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
                    yan.addView(img);
                    yan.addView(user);
                    gallerylayout.addView(yan);

                }

            }
        });
    }
}