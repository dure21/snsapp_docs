package com.studysemina.snsapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studysemina.snsapp.R;
import com.studysemina.snsapp.adapter.ChatAdapter;
import com.studysemina.snsapp.model.ChatData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db_FS = FirebaseFirestore.getInstance();
    FirebaseDatabase db_RT = FirebaseDatabase.getInstance();
    DatabaseReference commentColRef_RT = db_RT.getReference("comment");
    private CollectionReference userColRef = db_FS.collection("user");
    private CollectionReference commentColRef_RS = db_FS.collection("comment");

    private View navheaderView;
    private String FirestoreNick;
    private String FirestoreEmail;
    private TextView nav_tv_GoogleNick;
    private TextView nav_tv_GoogleEmail;
    private TextView nav_tv_FirestoreNick;
    private TextView nav_tv_FirestoreEmail;

    @BindView(R.id.Rv_Chat)
    RecyclerView recyclerView;
    private ChatAdapter adapter;
    List<ChatData> mchatData;

    @BindView(R.id.eT_Chat)
    EditText eT_Chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupUI();

//        commentColRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
//                for(DocumentChange dc : snapshots.getDocumentChanges()){
//                    if(dc.getType() == DocumentChange.Type.ADDED) {
////                        dc.getDocument().getData();
//                    }
//                }

//            }
//        });
    }

    private void setupUI() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navheaderView = navigationView.getHeaderView(0);
        nav_tv_GoogleNick = (TextView) navheaderView.findViewById(R.id.nav_tv_GoogleNick);
        nav_tv_GoogleEmail = (TextView) navheaderView.findViewById(R.id.nav_tv_GoogleEmail);
        nav_tv_FirestoreNick = (TextView) navheaderView.findViewById(R.id.nav_tv_FirestoreNick);
        nav_tv_FirestoreEmail = (TextView) navheaderView.findViewById(R.id.nav_tv_FirestoreEmail);

        StaggeredGridLayoutManager mStaggeredGridManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mStaggeredGridManager);

        mchatData = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(mchatData);
        recyclerView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user.getDisplayName() != null) {

            nav_tv_GoogleNick.setText(user.getDisplayName());
        }


        if (user.getEmail() != null) {

            nav_tv_GoogleEmail.setText(user.getEmail());
        }

        userColRef.document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                            FirestoreNick = new String(documentSnapshot.getData().get("nickname").toString());
                            FirestoreEmail = new String(documentSnapshot.getData().get("email").toString());

                            if (FirestoreNick != null) {

                                nav_tv_FirestoreNick.setText(FirestoreNick);
                            }

                            if (FirestoreEmail != null) {

                                nav_tv_FirestoreEmail.setText(FirestoreEmail);
                            }

                        } else {

                        }
                    }
                });

        commentColRef_RT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mchatData.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    ChatData chatData = dataSnapshot1.getValue(ChatData.class);
                    mchatData.add(chatData);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.ChatSend_Button)
    public void onChatSend_Button(View view) {
        if (eT_Chat.getText().toString().length() == 0) {
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("comment", eT_Chat.getText().toString());

        if(FirestoreNick == null){
            data.put("nickname", mAuth.getCurrentUser().getDisplayName());
        }
        else{
            data.put("nickname", FirestoreNick);
        }
        data.put("userId", mAuth.getUid());
        data.put("timestamp", new Date().getTime());

        // 기존db 쓰기
        writeComment(mAuth.getUid(),mAuth.getCurrentUser().getDisplayName(),eT_Chat.getText().toString(),new Date().getTime());
        // 베타db 쓰기
        commentColRef_RS
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        eT_Chat.setText("");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });

    }

    private void writeComment(String userId,String nickname,String comment,long timestamp) {
        ChatData chatData = new ChatData(userId,nickname,comment,timestamp);

        commentColRef_RT.push().setValue(chatData);

    }


    @OnClick(R.id.Calendar_Button)
    public void Calendar_Button(View view) {
        startActivity(new Intent(MainActivity.this, CalendarActivity.class));
        finish();
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
