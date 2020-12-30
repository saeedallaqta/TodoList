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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class all_tasks extends AppCompatActivity implements TaskAdapter.ListItemClickListener{
    int count;
    boolean isEnter = true;
    static List<Task> tasksList = new ArrayList<>();
    private FirebaseAuth mAuth;
    RecyclerView tasks_rv;
    TaskAdapter taskAdapter;
    Button addNewTask;
    TextView delete, tpartTitle;
    EditText taskTitle, taskDescription;
    String partId;
    String partTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        Bundle extras = getIntent().getExtras();
        partId = extras.getString("part_id");
        partTitle = extras.getString("part_title");

        addNewTask = findViewById(R.id.addNewTask);
        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDesc);
        delete = findViewById(R.id.deletePart);
        tpartTitle = findViewById(R.id.tpartTitle);
        tpartTitle.setText(partTitle);


        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(partId).removeValue();
                finish();
            }


        });

        addNewTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                Task task = new Task();
                task.setTitle(taskTitle.getText().toString());
                task.setDescription(taskDescription.getText().toString());
                task.setIsChecked(false);
                String taskId = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(partId).child("tasks").push().getKey();
                task.setId(taskId);
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(partId).child("tasks").child(taskId).setValue(task);



                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                                    Part part =  snapshot.getValue(Part.class);
                                    if(part.getId().compareTo(partId) == 0 && isEnter){
                                        count = part.getCount() + 1;
                                        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(partId).child("count").setValue(count);
                                        isEnter = false;
                                        break;
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                            }
                        });





                Toast.makeText(all_tasks.this,"added successfully", Toast.LENGTH_SHORT).show();
                taskTitle.setText("");
                taskDescription.setText("");
            }
        });

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(partId).child("tasks")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tasksList.clear();
                        for(DataSnapshot snapshot: dataSnapshot.getChildren() ){
                            Task item =  snapshot.getValue(Task.class);
                            tasksList.add(item);
                        }
                        taskAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        tasks_rv = findViewById(R.id.tasks_rv);
        tasks_rv.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this , tasksList, partId,this);
        tasks_rv.setAdapter(taskAdapter);
    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(all_tasks.this, task_information.class);
        intent.putExtra("task_id", tasksList.get(position).getId());
        intent.putExtra("task_title", tasksList.get(position).getTitle());
        intent.putExtra("task_desc", tasksList.get(position).getDescription());
        intent.putExtra("part_title", partTitle);
        intent.putExtra("part_id", partId);
        startActivity(intent);
    }
}