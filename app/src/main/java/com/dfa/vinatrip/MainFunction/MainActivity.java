package com.dfa.vinatrip.MainFunction;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dfa.vinatrip.CheckNetwork;
import com.dfa.vinatrip.IntroApp.CheckFirstTimeLaunch;
import com.dfa.vinatrip.MainFunction.Location.LocationFragment;
import com.dfa.vinatrip.MainFunction.Me.MeFragment;
import com.dfa.vinatrip.MainFunction.Memory.MemoryFragment;
import com.dfa.vinatrip.MainFunction.Plan.PlanFragment;
import com.dfa.vinatrip.MainFunction.Share.ShareFragment;
import com.dfa.vinatrip.R;

public class MainActivity extends AppCompatActivity {

    private LocationFragment locationFragment;
    private PlanFragment planFragment;
    private ShareFragment shareFragment;
    private MemoryFragment memoryFragment;
    private MeFragment meFragment;
    private BottomNavigationView bnvMenu;
    private boolean doubleBackPress = false;
    private int selectedItemId;
    private Toolbar toolbar;
    private android.support.v7.app.ActionBar actionBar;
    private CheckFirstTimeLaunch prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActionBar();
        changeColorStatusBar();

        bnvMenu = (BottomNavigationView) findViewById(R.id.activity_main_bnv_menu);

        // When more than 3 icons, ShiftMode happen, use this to back to normal
        StopShiftModeBottomNavView.disableShiftMode(bnvMenu);

        bnvMenu.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragment(item);
                        // Must true so item in bottom bar can transform
                        return true;
                    }
                });

        addNewFragments();

        // load fragment_location first
        MenuItem selectedItem;
        if (savedInstanceState != null) {
            selectedItem = bnvMenu.getMenu().findItem(savedInstanceState.getInt("arg_selected_item", 0));
        } else {
            selectedItem = bnvMenu.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

        if (!CheckNetwork.isNetworkConnected(MainActivity.this)) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.activity_main),
                            "Không có kết nối Internet",
                            Snackbar.LENGTH_LONG);
            View viewSnackbar = snackbar.getView();
            TextView tvSnackbar =
                    (TextView) viewSnackbar.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbar.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    public void changeColorStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorStatusBar));
        }
    }

    public void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setIcon(R.drawable.ic_symbol);
        }
    }

    public void selectFragment(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.iconLocation:
                getSupportFragmentManager().beginTransaction().show(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconPlan:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().show(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconShare:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().show(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconMemory:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().show(memoryFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(meFragment).commit();
                break;
            case R.id.iconMe:
                getSupportFragmentManager().beginTransaction().hide(locationFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(planFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(shareFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(memoryFragment).commit();
                getSupportFragmentManager().beginTransaction().show(meFragment).commit();
                break;
        }
        selectedItemId = item.getItemId();
    }

    public void addNewFragments() {
        locationFragment = new LocationFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, locationFragment, "locationFragment")
                .commit();

        planFragment = new PlanFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, planFragment, "planFragment")
                .commit();

        shareFragment = new ShareFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, shareFragment, "shareFragment")
                .commit();

        memoryFragment = new MemoryFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, memoryFragment, "memoryFragment")
                .commit();

        meFragment = new MeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_fl_container, meFragment, "meFragment")
                .commit();
    }

    @Override
    public void onBackPressed() {
        MenuItem homeItem = bnvMenu.getMenu().getItem(0);
        if (selectedItemId != homeItem.getItemId()) {
            selectFragment(homeItem);
            // if not, it will not back to home icon when press
            for (int i = 0; i < bnvMenu.getMenu().size(); i++) {
                MenuItem menuItem = bnvMenu.getMenu().getItem(i);
                menuItem.setChecked(menuItem.getItemId() == homeItem.getItemId());
            }
        } else {
            if (doubleBackPress) {
                super.onBackPressed();
            } else {
                doubleBackPress = true;
                Toast.makeText(MainActivity.this, "Nhấn BACK thêm một lần nữa để thoát", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackPress = false;
                    }
                    // không nhấn nhanh thì phải nhấn 2 lần lại
                }, 2000);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        // Expand searchView, if not, it just show icon
        searchView.setIconifiedByDefault(false);

        searchView.setQueryHint("Tìm kiếm...");

        return true;
    }
}

