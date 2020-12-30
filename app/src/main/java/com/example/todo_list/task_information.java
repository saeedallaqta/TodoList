package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class task_information extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView title, description, partTitle, delete;
    String inTitle, inDesc, inId, inPart, inPartId;
    Integer count;
    boolean isEnter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_information);

        Bundle extras = getIntent().getExtras();
        inTitle = extras.getString("task_title");
        inDesc = extras.getString("task_desc");
        inId = extras.getString("task_id");
        inPart = extras.getString("part_title");
        inPartId = extras.getString("part_id");

        title = findViewById(R.id.ititle);
        description = findViewById(R.id.idescription);
        partTitle = findViewById(R.id.ipart);
        delete = findViewById(R.id.delete);

        title.setText(inTitle);
        description.setText(inDesc);
        partTitle.setText(inPart);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(inPartId).child("tasks").child(inId).removeValue();




                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                                    Part part =  snapshot.getValue(Part.class);
                                    if(part.getId().compareTo(inPartId) == 0 && isEnter == true){
                                        count = part.getCount() - 1;
                                        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(inPartId).child("count").setValue(count);
                                        isEnter  = false;
                                        break;
                                    }

                                }

                                finish();


                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });


            }
        });

    }
}