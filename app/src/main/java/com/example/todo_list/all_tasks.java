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

public class all_tasks extends AppCompatActivity implements TaskAdapter.ListItemClickListener{

    static List<Task> tasksList = new ArrayList<>();
    private FirebaseAuth mAuth;
    RecyclerView tasks_rv;
    TaskAdapter taskAdapter;
    Button addNewTask;
    EditText taskTitle, taskDescription;
    String categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        Bundle extras = getIntent().getExtras();
        categoryId = extras.getString("CATEGORY_ID");

        addNewTask = findViewById(R.id.addNewTask);
        taskTitle = findViewById(R.id.taskTitle);
        taskDescription = findViewById(R.id.taskDesc);
        addNewTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String uid = user.getUid();
                Task newTask = new Task();
                newTask.setTitle(taskTitle.getText().toString());
                newTask.setDescription(taskDescription.getText().toString());
                newTask.setIsChecked(false);
                String taskId = FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(categoryId).child("tasks").push().getKey();
                newTask.setId(taskId);
                FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(categoryId).child("tasks").child(taskId).setValue(newTask);
                Toast.makeText(all_tasks.this,"added successfully", Toast.LENGTH_SHORT).show();
                taskTitle.setText("");
                taskDescription.setText("");
            }
        });

        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase.getInstance().getReference("Users").child(uid).child("part").child(categoryId).child("tasks")
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
        taskAdapter = new TaskAdapter(this , tasksList, this);
        tasks_rv.setAdapter(taskAdapter);
    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(all_tasks.this, task_information.class);
        intent.putExtra("taskId", tasksList.get(position).getId());
        intent.putExtra("taskTitle", tasksList.get(position).getTitle());
        intent.putExtra("taskDescription", tasksList.get(position).getDescription());
        startActivity(intent);
    }
}