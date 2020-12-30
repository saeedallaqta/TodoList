package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class tasks extends AppCompatActivity implements PartAdapter.ListItemClickListener{
    static List<Part> partList = new ArrayList<>();
    private FirebaseAuth mAuth;
    RecyclerView part_rv;
    PartAdapter partAdapter;
    Button addNewPart;
    EditText part;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        addNewPart = findViewById(R.id.addNewPart);
        part = findViewById(R.id.part);


        addNewPart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                Part newPart = new Part();
                newPart.setTitle(part.getText().toString());
                newPart.setCount(0);
                String partId = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").push().getKey();
                newPart.setId(partId);
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(partId).setValue(newPart);
                Toast.makeText(tasks.this,"added successfully", Toast.LENGTH_SHORT).show();
                part.setText("");
            }
        });

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        partList.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                            Part item =  snapshot.getValue(Part.class);
                            partList.add(item);
                        }
                        partAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
        part_rv = findViewById(R.id.part_rv);
        part_rv.setLayoutManager(new LinearLayoutManager(this));
        partAdapter = new PartAdapter(partList, this);
        part_rv.setAdapter(partAdapter);
    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(tasks.this, all_tasks.class);
        intent.putExtra("part_id", partList.get(position).getId());
        intent.putExtra("part_title", partList.get(position).getTitle());
        startActivity(intent);
    }
}