package com.demarkelabs.android_geolocation_queries_java;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button queryNear, queryWithinKm, queryWithinPolygon, clearResults;
    private ResultAdapter adapter;
    private ProgressDialog progressDialog;
    private RecyclerView resultList;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queryNear = findViewById(R.id.queryNear);
        queryWithinKm = findViewById(R.id.queryWithinKm);
        queryWithinPolygon = findViewById(R.id.queryWithinPolygon);
        resultList = findViewById(R.id.resultList);
        clearResults = findViewById(R.id.clearResults);
        progressDialog = new ProgressDialog(this);


        queryNear.setOnClickListener(view -> {
            progressDialog.show();
            ParseQuery<ParseObject> query = new ParseQuery<>("City");
            query.whereNear("location",new ParseGeoPoint(18.018086950599134, -76.79894232253473));
            query.findInBackground((objects, e) -> {
                progressDialog.dismiss();
                if (e==null){
                    initData(objects);
                } else {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        queryWithinKm.setOnClickListener(view -> {
            progressDialog.show();
            ParseQuery<ParseObject> query = new ParseQuery<>("City");
            query.whereWithinKilometers("location",new ParseGeoPoint(18.018086950599134, -76.79894232253473),3000);
            query.findInBackground((objects, e) -> {
                progressDialog.dismiss();
                if (e==null){
                    initData(objects);
                } else {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
        queryWithinPolygon.setOnClickListener(view -> {
            progressDialog.show();
            ParseQuery<ParseObject> query = new ParseQuery<>("City");

            ParseGeoPoint geoPoint1 = new ParseGeoPoint(15.822238344514378, -72.42845934415942);
            ParseGeoPoint geoPoint2 = new ParseGeoPoint(-0.7433770196268968, -97.44765968406668);
            ParseGeoPoint geoPoint3 = new ParseGeoPoint(-59.997149373299166, -76.52969196322749);
            ParseGeoPoint geoPoint4 = new ParseGeoPoint(-9.488786415007201, -18.346101586021952);
            ParseGeoPoint geoPoint5 = new ParseGeoPoint(15.414859532811047, -60.00625459569375);
            List<ParseGeoPoint> list = new ArrayList<>();
            list.add(geoPoint1);
            list.add(geoPoint2);
            list.add(geoPoint3);
            list.add(geoPoint4);
            list.add(geoPoint5);
            query.whereWithinPolygon("location",list);

            query.findInBackground((objects, e) -> {
                progressDialog.dismiss();
                if (e==null){
                    initData(objects);
                } else {
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

        clearResults.setOnClickListener(view -> adapter.clearList());
    }

    private void initData(List<ParseObject> objects) {
        adapter = new ResultAdapter(this, objects);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(adapter);
    }
}