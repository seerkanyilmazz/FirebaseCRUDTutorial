package com.seerkanyilmazz.firebasecrudtutorial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    EditText input_user;
    EditText input_email;
    ListView list_Data;
    ProgressBar circularProgress;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private User selectedUser;
    private List<User> list_user = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.menu_toolbar);
        toolbar.setTitle("FireBase CRUD Tutorial");
        setSupportActionBar(toolbar);

        input_user       = findViewById(R.id.editTextName);
        input_email      = findViewById(R.id.editTextEmail);
        list_Data        = findViewById(R.id.list_data);
        circularProgress = findViewById(R.id.circular_progress);

//        Firebase
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

//         ProgressBar
        circularProgress.setVisibility(View.VISIBLE);
        list_Data.setVisibility(View.INVISIBLE);

        list_Data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User userOnUserList = (User) parent.getItemAtPosition(position);
                selectedUser = userOnUserList;
                input_user.setText(selectedUser.getName());
                input_email.setText(selectedUser.getEmail());
            }
        });

//        FireBase Listener
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (list_user.size() > 0)
                {
                    list_user.clear();
                }

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    User user = postSnapShot.getValue(User.class);
                    list_user.add(user);
                }

                ListViewAdapter adapter = new ListViewAdapter(MainActivity.this, list_user);

                list_Data.setAdapter(adapter);

                circularProgress.setVisibility(View.INVISIBLE);
                list_Data.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_add)
        {
            userAdd();
        }

        else if (item.getItemId() == R.id.menu_update)
        {
            userUpdate();
        }

        else if (item.getItemId() == R.id.menu_delete)
        {
            userDelete(selectedUser);
        }

        return true;
    }

    private void userDelete(User selectedUser) {
//        deleting
        databaseReference.child("users").child(selectedUser.getUserID()).removeValue();

        controlClean();
    }

    private void userUpdate() {
//        UserUpdating
        User user = new User(selectedUser.getUserID(), input_user.getText().toString(), input_email.getText().toString());
//        database post
        databaseReference.child("users").child(user.getUserID()).child("Name").setValue(user.getName());
        databaseReference.child("users").child(user.getUserID()).child("Email").setValue(user.getEmail());

        controlClean();
    }

    private void userAdd() {
//        User Adding
        User user = new User(UUID.randomUUID().toString(), input_user.getText().toString(), input_email.getText().toString());

//        Database Post
        databaseReference.child("users").child(user.getUserID()).setValue(user);
        controlClean();
    }

    private void controlClean() {
        input_user.setText("");
        input_email.setText("");
    }
}