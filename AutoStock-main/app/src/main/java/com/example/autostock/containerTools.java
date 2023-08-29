package com.example.autostock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class containerTools extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private LottieAnimationView animationView1;
    private LottieAnimationView animationView2;
    private LottieAnimationView animationView3;


    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView profileImageView;
    private String profileImagePath;
    SharedPreferences sharedPreferencesimg;
    private static final String SHARED_PREF_IMG = "img";
    private static final String KEY_IMG = "profile_image";

    SharedPreferences sharedPreferencesout;
    private static final String SHARED_PREF_OUT = "out";
    private static final String KEY_OUT = "isLoggedIn";


    SharedPreferences sharedPreferences1;

    private static final String SHARED_PREF = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_tools);

        animationView1 = findViewById(R.id.animationViewT1);
        animationView2 = findViewById(R.id.animationViewT2);
        animationView3 = findViewById(R.id.animationViewT3);

        sharedPreferencesout = getSharedPreferences(SHARED_PREF_OUT, MODE_PRIVATE);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0); // Obtén la vista del encabezado del Navigation Drawer
        TextView nameTextView = headerView.findViewById(R.id.nameView); // TextView para el nombre del usuario
        TextView emailTextView = headerView.findViewById(R.id.emailView); // TextView para el correo electrónico del usuario

        sharedPreferences1 = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        String name = sharedPreferences1.getString(KEY_NAME,null);
        String email = sharedPreferences1.getString(KEY_EMAIL,null);

        if (name != null && email != null) {
            nameTextView.setText(name);
            emailTextView.setText(email);
        }


        profileImageView = headerView.findViewById(R.id.ivProfile);

        sharedPreferencesimg = getSharedPreferences(SHARED_PREF_IMG, MODE_PRIVATE);
        // Recuperar la URI de la imagen guardada y mostrarla
        String savedImageUriString = sharedPreferencesimg.getString(KEY_IMG, null);
        if (savedImageUriString != null) {
            Uri savedImageUri = Uri.parse(savedImageUriString);
            profileImageView.setImageURI(savedImageUri);
        }

        drawerLayout = findViewById(R.id.drawer_layout1);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkStoragePermission();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        loadFragment(new fragment_calculator());

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.menu_Calculadora:
                        loadFragment(new fragment_calculator());
                        if (animationView1.isAnimating()) {
                            animationView1.cancelAnimation();
                        } else {
                            animationView1.setRepeatCount(0);
                            animationView1.playAnimation();
                        }
                        return true;

                    case R.id.menu_Conversor:
                        loadFragment(new fragment_converter());
                        if (animationView2.isAnimating()) {
                            animationView2.cancelAnimation();
                        } else {
                            animationView2.setRepeatCount(0);
                            animationView2.playAnimation();
                        }
                        return true;

                    case R.id.menu_lista:
                        loadFragment(new fragment_list());
                        if (animationView3.isAnimating()) {
                            animationView3.cancelAnimation();
                        } else {
                            animationView3.setRepeatCount(0);
                            animationView3.playAnimation();
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_herramientasD:
                loadFragment(new fragment_calculator());
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.menu_signout:

                SharedPreferences.Editor out = sharedPreferencesout.edit();
                out.clear();
                out.commit();
                out.apply();

                Intent intent3 = new Intent(containerTools.this, MainActivity.class);
                startActivity(intent3);
                loadFragment(new fragment_users());
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;


            case R.id.menu_homeD:
                Intent intent2 = new Intent(containerTools.this, container.class);
                startActivity(intent2);
                loadFragment(new fragment_newUser());
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.menu_perfilD:
                Intent intent1 = new Intent(containerTools.this, container_profile.class);
                startActivity(intent1);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PICK_IMAGE_REQUEST
            );
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);

            // Guarda la imagen en el almacenamiento interno
            saveImageToInternalStorage(selectedImageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                // Los permisos fueron denegados
            }
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(imageUri);
            File outputDir = getExternalFilesDir(null);
            File imageFile = new File(outputDir, "profile_image.jpg");
            outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            profileImagePath = imageFile.getAbsolutePath();
            saveImagePathToSharedPreferences(profileImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImagePathToSharedPreferences(String imagePath) {
        SharedPreferences.Editor editor = sharedPreferencesimg.edit();
        editor.putString(KEY_IMG, imagePath);
        editor.apply();
    }
}