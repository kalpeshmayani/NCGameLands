package com.example.ncgamelands.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.ncgamelands.R;
import com.example.ncgamelands.model.CustomInfo;
import com.example.ncgamelands.ui.adapter.ItemRecyclerViewAdapter;
import com.example.ncgamelands.ui.inteface.ItemCallback;
import com.example.ncgamelands.util.IntentUtil;
import com.example.ncgamelands.util.RuntimeData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private boolean permissionsAllowed;
    public static final int PERMISSION_REQUEST = 101;

    @BindView(R.id.etsearch)
    EditText etsearch;
    @BindView(R.id.rvlist)
    RecyclerView rvlist;

    List<Object> itemList = new ArrayList<>();
    private ItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkPermission();
        if (!permissionsAllowed) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        } else {
            startApp();
        }
    }

    public void checkPermission() {
        permissionsAllowed =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission();
        if (!permissionsAllowed) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        } else {
            startApp();
        }
    }

    private void startApp() {
        rvlist.setHasFixedSize(true);

        // Specify a linear layout manager.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvlist.setLayoutManager(layoutManager);

        adapter = new ItemRecyclerViewAdapter(this, new ItemCallback() {
            @Override
            public void onItem(CustomInfo item) {
                RuntimeData.getInstance().setItem(item);
                IntentUtil.startMapActivity(MainActivity.this);
            }
        });
        rvlist.setAdapter(adapter);

        etsearch.addTextChangedListener(new Filter());

        getData();
    }

    private class Filter implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                adapter.setItems(itemList);
            } else if (s.length() > 0) {
                adapter.applyFilter(etsearch.getText().toString());
            }
        }
    }

    private void getData() {
        itemList.add(new CustomInfo(1, getResources().getString(R.string.south_mountain), "35.5555701", "-81.7455148"));
        itemList.add(new CustomInfo(2, getResources().getString(R.string.alcoa_game_lands), "35.7824291", "-80.4893328"));
        itemList.add(new CustomInfo(3, getResources().getString(R.string.second_creek_game_land), "35.680191", "-80.636114"));

        adapter.setItems(itemList);
    }

}