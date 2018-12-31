package com.example.student.trivia_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.example.student.trivia_app.model.QuestionsAnswered;
import com.example.student.trivia_app.model.User;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;


public class StatisticsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference dbRootRef;
    final HashMap<String,int[]> questions_answered_map = new HashMap<String,int[]>();
    final HashMap<String,Integer> subjectsCount = new HashMap<String,Integer>();

    BarChart barChart;
    BarChart barChart2;
    BarChart barChart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //firebase
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRootRef = mDatabase.getReference();
        String user_email = mAuth.getCurrentUser().getEmail().replace(".", "|");


        if (mAuth.getCurrentUser() != null) {

            DatabaseReference dbRef = mDatabase.getReference("users/" + user_email);
            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                   go through all users and send to next activity the hash of the user
                    int countAnswered = 0;
                    if (dataSnapshot.child("questions_answered").exists()) {
                        for (DataSnapshot ds : dataSnapshot.child("questions_answered").getChildren()) {
                            String subject = ds.getKey().toString().replace("question ", "").replaceAll("_\\d+", "");

                            if (questions_answered_map.keySet().contains(subject)) {

                                int[] properties = questions_answered_map.get(subject);
                                properties[0]++; //properties[0] = 1; // Counter for question that solved in this subject

                                properties[1] += Integer.parseInt(ds.getValue().toString()); // score of the question
                                questions_answered_map.put(subject, properties);

                            } else {

                                int[] properties = new int[2];
                                properties[0] = 1; // Counter for question that solved in this subject
                                properties[1] = Integer.parseInt(ds.getValue().toString()); // score of the question
                                questions_answered_map.put(subject, properties);
                            }
                        }

                    }

                    dbRootRef = mDatabase.getReference();

                    dbRootRef = mDatabase.getReference("subjects/");
                    dbRootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                      go through all users and send to next activity the hash of the user
                            int countAnswered = 0;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                System.out.println("======================" + ds.getKey().toString() + " ----------------------" +  ds.child("questions").exists());
                                if (ds.child("questions").exists()) {
                                    subjectsCount.put(ds.getKey().toString(), (int)ds.child("questions").getChildrenCount());
                                    System.out.println(questions_answered_map.get("C")[0]);

                                    String subject = ds.getKey().toString();
                                }
                            }


                            barChart = (BarChart) findViewById(R.id.barchart);
                            barChart.setDrawBarShadow(false);
                            barChart.setDrawValueAboveBar(true);
                            barChart.setMaxVisibleValueCount(50);
                            barChart.setPinchZoom(false);
                            barChart.setDrawGridBackground(true);



                            ArrayList<BarEntry> barEntries = new ArrayList<>();

                            // score for subject
                            int i=1;
                            for (String key :questions_answered_map.keySet() )
                            {
                                barEntries.add(new BarEntry(i++, questions_answered_map.get(key)[1]));

                            }
                            

                            BarDataSet barDataSet = new BarDataSet(barEntries, "Score In Each Subject");
                            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData data = new BarData(barDataSet);
                            data.setBarWidth(0.9f);

                            barChart.setData(data);

                            XAxis xAxis = barChart.getXAxis();
                            /* String[] month = {"C", "CPP", "Java", "Python", "NAM"};*/
                            Set<String> s = questions_answered_map.keySet();
                            String[] subjectsName = s.toArray(new String[0]);
                            xAxis.setValueFormatter(new MyXAxisValueFormatter(subjectsName)); // subjectsName must be >= 4 !!!
                            xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

                            ////////////////////////////////////////////////////////////////////////////

                            barChart2 = (BarChart) findViewById(R.id.barchart2);
                            barChart2.setDrawBarShadow(false);
                            barChart2.setDrawValueAboveBar(true);
                            barChart2.setMaxVisibleValueCount(50);
                            barChart2.setPinchZoom(false);
                            barChart2.setDrawGridBackground(true);



                            ArrayList<BarEntry> barEntries2 = new ArrayList<>();


                            // questions_answered for subject
                            int j=1;
                            for (String key :questions_answered_map.keySet() )
                            {
                                barEntries2.add(new BarEntry(j++, questions_answered_map.get(key)[0]));

                            }



                            BarDataSet barDataSet2 = new BarDataSet(barEntries2, "questions_answered In Each Subject");
                            barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData data2 = new BarData(barDataSet2);
                            data2.setBarWidth(0.9f);

                            barChart2.setData(data2);

                            XAxis xAxis2 = barChart2.getXAxis();
                            /* String[] month = {"C", "CPP", "Java", "Python", "NAM"};*/
                            Set<String> s2 = questions_answered_map.keySet();
                            String[] subjectsName2 = s2.toArray(new String[0]);
                            xAxis2.setValueFormatter(new MyXAxisValueFormatter(subjectsName2)); // subjectsName must be >= 4 !!!
                            xAxis2.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

                            ////////////////////////////////////////////////////////////////

                            barChart3 = (BarChart) findViewById(R.id.barchart3);
                            barChart3.setDrawBarShadow(false);
                            barChart3.setDrawValueAboveBar(true);
                            barChart3.setMaxVisibleValueCount(50);
                            barChart3.setPinchZoom(false);
                            barChart3.setDrawGridBackground(true);



                            ArrayList<BarEntry> barEntries3 = new ArrayList<>();



                            ///(questions_answered for subject / all question about subject ) *100

                            int k=1;
                            for (String key :questions_answered_map.keySet() )
                            {

                                int x=questions_answered_map.get(key)[0];
                                int y=subjectsCount.get(key);
                                float ratio=(float)(x)/y *100;
                                barEntries3.add(new BarEntry(k++, ratio));

                            }

                            BarDataSet barDataSet3 = new BarDataSet(barEntries3, "% Question That Answered In Each Subject");
                            barDataSet3.setColors(ColorTemplate.COLORFUL_COLORS);

                            BarData data3 = new BarData(barDataSet3);
                            data3.setBarWidth(0.9f);

                            barChart3.setData(data3);

                            XAxis xAxis3 = barChart3.getXAxis();
                            /* String[] month = {"C", "CPP", "Java", "Python", "NAM"};*/
                            Set<String> s3 = questions_answered_map.keySet();
                            String[] subjectsName3 = s3.toArray(new String[0]);
                            xAxis3.setValueFormatter(new MyXAxisValueFormatter(subjectsName3)); // subjectsName must be >= 4 !!!
                            xAxis3.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

                            ////////////////////////////////////////////////////////////////////////////



                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            System.out.println();

//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            setSupportActionBar(toolbar);
//
//            barChart = (BarChart) findViewById(R.id.barchart);
//
//            barChart.setDrawBarShadow(false);
//            barChart.setDrawValueAboveBar(true);
//            barChart.setMaxVisibleValueCount(50);
//            barChart.setPinchZoom(false);
//            barChart.setDrawGridBackground(true);
//
//            ArrayList<BarEntry> barEntries = new ArrayList<>();
//            barEntries.add(new BarEntry(1,questions_answered_map.get("C")[1]));
//            barEntries.add(new BarEntry(2, 44f));
//            barEntries.add(new BarEntry(3, 30f));
//            barEntries.add(new BarEntry(4, 36f));
//
//            BarDataSet barDataSet = new BarDataSet(barEntries, "data set 1");
//            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
//
//            BarData data = new BarData(barDataSet);
//            data.setBarWidth(0.9f);
//
//            barChart.setData(data);
//
//            String[] month = {"C", "CPP", "Java","aviv","moshe","nissan"};
//            XAxis xAxis = barChart.getXAxis();
//            xAxis.setValueFormatter(new MyXAxisValueFormatter(month));

            System.out.println();
        }

//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }


//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.stats:
//                Toast.makeText(getApplicationContext(),"Already in Stats",Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.sign_out:
//                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(StatisticsActivity.this, MainActivity.class));
//                finish();
//                return true;
//            case R.id.about_app:
//                Toast.makeText(getApplicationContext(),"Created by Aviv, Nissan & Moshe",Toast.LENGTH_LONG).show();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);

    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter{

        private String[] mVlaue;

        public MyXAxisValueFormatter(String[] mVlaue) {
            this.mVlaue = mVlaue;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mVlaue[(int) value];
        }
    }

}


