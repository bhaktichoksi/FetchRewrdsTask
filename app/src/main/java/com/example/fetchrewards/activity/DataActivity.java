package com.example.fetchrewards.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fetchrewards.ApiInterface;
import com.example.fetchrewards.R;
import com.example.fetchrewards.adapter.DataAdapter;
import com.example.fetchrewards.model.Rewards;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DataActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DataAdapter dataAdapter;
    private List<Rewards> rewardsModelArrayList = new ArrayList<>();
    private List<Integer> uniqueListData = new ArrayList<>();
    private List<List<Rewards>> MasterArrayList1 = new ArrayList<>();
    Spinner mySpinner;
    String[] categories = {"Filter Out", "Empty", "Null"};
    LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        init();
    }

    public void init() {
        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getRewards();
        initializeViews();
    }

    private void initializeViews() {

        mySpinner = findViewById(R.id.mySpinner);
        mySpinner.setAdapter(new ArrayAdapter<>(this, R.layout.simple_list_item, categories));

    }

    private void getRewards() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        // below line is to create an instance for our retrofit api class.
        ApiInterface retrofitAPI = retrofit.create(ApiInterface.class);
        Call<String> call = retrofitAPI.getRewards();
        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        Log.e("Response Data", "" + response.body());

                        try {
                            JSONArray jsonArray = new JSONArray(response.body());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Rewards rewards = new Rewards();
                                JSONObject jsonObjectDataList = jsonArray.getJSONObject(i);

                                rewards.setId(jsonObjectDataList.getInt("id"));
                                rewards.setLid(jsonObjectDataList.getInt("listId"));
                                if (jsonObjectDataList.getString("name") != null) {
                                    rewards.setName(jsonObjectDataList.getString("name"));
                                }

                                rewardsModelArrayList.add(rewards);
                                Log.e("Rewards Data", "" + rewardsModelArrayList.toString());
                                Collections.sort(rewardsModelArrayList, new Comparator<Rewards>() {
                                    @Override
                                    public int compare(Rewards p1, Rewards p2) {
                                        return p2.getName().compareTo(p1.getName()); // Descending
                                    }
                                });

                                if (!uniqueListData.contains(rewards.getLid())) {
                                    uniqueListData.add(rewards.getLid());
                                    Collections.sort(uniqueListData);
                                }
                            }
                            Log.e("ss", "list: " + rewardsModelArrayList.size());

                            for (int i = 0; i < uniqueListData.size(); i++) {

                                ArrayList<Rewards> data = new ArrayList<>();
                                for (int j = 0; j < rewardsModelArrayList.size(); j++) {

                                    if (rewardsModelArrayList.get(j).getLid() == uniqueListData.get(i) && !rewardsModelArrayList.get(j).getName().equals("")
                                            && !rewardsModelArrayList.get(j).getName().equals("null") && rewardsModelArrayList.get(j).getName() != null) {
                                        data.add(rewardsModelArrayList.get(j));
                                    }
                                }
                                Log.e("ss", "list1: " + data.size());
                                MasterArrayList1.add(data);

                            }
                            Log.e("ss", "list2: " + MasterArrayList1.size());

                            dataAdapter = new DataAdapter(MasterArrayList1);
                            recyclerView.setAdapter(dataAdapter);
                            Log.e("uniqueListData", "getJsonFileFromLocally: " + uniqueListData.size());

                            dataAdapter.notifyDataSetChanged();

                            mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long itemID) {
                                    if (position >= 0 && position < categories.length) {
                                        getSelectedCategoryData(position);
                                        Log.e("ss", "onItemSelected: " + rewardsModelArrayList.get(position).getName());
                                    } else {
                                        Toast.makeText(DataActivity.this, "Selected Category Does not Exist!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // displaying an error message in toast
                Toast.makeText(DataActivity.this, "Fail to get the data..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(parentLayout, "Please click BACK again to exit", Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finishAffinity();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void getSelectedCategoryData(int categoryID) {
        Log.e("ss1", "getSelectedCategoryData: " + categoryID);
        List<List<Rewards>> FilterArrayList = new ArrayList<>();

        //filter out Items whose name does not have null or empty values
        if (categoryID == 0) {
            FilterArrayList.clear();
            for (int i = 0; i < uniqueListData.size(); i++) {
                ArrayList<Rewards> rewards = new ArrayList<>();
                for (int j = 0; j < rewardsModelArrayList.size(); j++) {
                    String str = rewardsModelArrayList.get(j).getName();
                    Log.e("data", "getSelectedCategoryData: " + str);
                    if (rewardsModelArrayList.get(j).getLid() == uniqueListData.get(i) && !rewardsModelArrayList.get(j).getName().equals("") && !rewardsModelArrayList.get(j).getName().equals("null") && rewardsModelArrayList.get(j).getName() != null) {
                        rewards.add(rewardsModelArrayList.get(j));
                    }
                }
                Log.e("ss", "size1: " + rewards.size());
                FilterArrayList.add(rewards);
            }

            dataAdapter.setdata(FilterArrayList);

            //filter out Id whose name has empty values only
        } else if (categoryID == 1) {
            FilterArrayList.clear();
            for (int i = 0; i < uniqueListData.size(); i++) {
                ArrayList<Rewards> rewards = new ArrayList<>();
                for (int j = 0; j < rewardsModelArrayList.size(); j++) {
                    if (rewardsModelArrayList.get(j).getLid() == uniqueListData.get(i) && rewardsModelArrayList.get(j).getName().equals("")) {
                        rewards.add(rewardsModelArrayList.get(j));
                    }
                }
                Log.e("ss", "size: " + rewards.size());
                FilterArrayList.add(rewards);
            }
            dataAdapter.setdata(FilterArrayList);

            //filter out Id whose name has null values only
        } else if (categoryID == 2) {
            FilterArrayList.clear();
            for (int i = 0; i < uniqueListData.size(); i++) {
                ArrayList<Rewards> rewards = new ArrayList<>();
                for (int j = 0; j < rewardsModelArrayList.size(); j++) {
                    if (rewardsModelArrayList.get(j).getLid() == uniqueListData.get(i) && (rewardsModelArrayList.get(j).getName() == null || rewardsModelArrayList.get(j).getName().equals("null"))) {
                        rewards.add(rewardsModelArrayList.get(j));
                    }
                }
                Log.e("ss", "size: " + rewards.size());
                FilterArrayList.add(rewards);
            }
            dataAdapter.setdata(FilterArrayList);
        }
    }
}