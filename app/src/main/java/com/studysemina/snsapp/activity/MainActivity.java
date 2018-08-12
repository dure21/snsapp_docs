package com.studysemina.snsapp.activity;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.studysemina.snsapp.R;
import com.studysemina.snsapp.adapter.ChatAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userColRef = db.collection("user");

    private View navheaderView;
    private String FirestoreNick;
    private String FirestoreEmail;
    private TextView nav_tv_GoogleNick;
    private TextView nav_tv_GoogleEmail;
    private TextView nav_tv_FirestoreNick;
    private TextView nav_tv_FirestoreEmail;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupUI();

        dialog = new ProgressDialog(MainActivity.this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setMessage("등록중..");
        dialog.setCancelable(false);


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
