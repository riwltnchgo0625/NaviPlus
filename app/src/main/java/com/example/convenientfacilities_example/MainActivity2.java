package com.example.convenientfacilities_example;
import android.Manifest;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.convenientfacilities_example.Adapter.ViewPagerAdapter;
import com.example.convenientfacilities_example.Common.Common;

import com.example.convenientfacilities_example.ui.home.HomeFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private CoordinatorLayout coordinatorLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

        private DrawerLayout drawer;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.root_view);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            //Request permission
            Dexter.withActivity(this)
                    .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                buildLocationRequest();
                                buildLocationCallBack();


                                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity2.this);
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                            }

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            Snackbar.make(coordinatorLayout, "Permisson Denied", Snackbar.LENGTH_LONG)
                                    .show();

                        }


                    }).check();

        }

        private void buildLocationCallBack() {
            locationCallback = new LocationCallback() {


                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    Common.current_location = locationResult.getLastLocation();

                    viewPager = (ViewPager) findViewById(R.id.view_pager);
                    setupViewpager(viewPager);
                    tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(viewPager);

                    //Log
                    Log.d("Location", locationResult.getLastLocation().getLatitude() + "/" + locationResult.getLastLocation().getLongitude());
                }
            };
        }

        private void setupViewpager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(TodayWeatherFragment.getInstance(), "Today");
            adapter.addFragment(ForecastFragment.getInstance(), "5 DAYS");
            adapter.addFragment(CityFragment.getInstance(), "CITIES");
            viewPager.setAdapter(adapter);

        }

        private void buildLocationRequest() {
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setSmallestDisplacement(10.0f);

        }
    }
