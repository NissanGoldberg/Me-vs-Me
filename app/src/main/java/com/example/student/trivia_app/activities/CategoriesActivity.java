package com.example.student.trivia_app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.trivia_app.R;
import com.example.student.trivia_app.model.Category;
import com.example.student.trivia_app.model.CategoryAdapter;
import com.example.student.trivia_app.model.NewCat;
import com.example.student.trivia_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesActivity extends AppCompatActivity {
    String permissions;
    final Map<String,Integer> images = new HashMap<>();
    final ArrayList<NewCat> cats = new ArrayList<NewCat>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        permissions = intent.getStringExtra("permissions");

        setContentView(R.layout.activity_category);
        final GridView gallery = findViewById(R.id.gallery);

//        populateImages();

        if(gallery.getChildCount() != 0){
//            gallery.removeAllViewsInLayout();
            gallery.removeAllViews();
        }

        final LayoutInflater inflater = LayoutInflater.from(this);

        System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dbRef =mDatabase.getReference("/subjects");

        //Read from database
        //constantly checks for changes
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    cats.add(new NewCat(ds.getKey(), ds.child("imageURL").getValue().toString()));

                }
                NewCat newCat_array[] = cats.toArray(new NewCat[cats.size()]);
                CategoryAdapter booksAdapter = new CategoryAdapter(getApplicationContext(), newCat_array);
                gallery.setAdapter(booksAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> adapter_view, View view, int position,
                                    long id)
            {
                if(position == 0)
                {
                    //your code
                }
                Toast.makeText(getApplicationContext(),position+"!!!",Toast.LENGTH_SHORT).show();
                String str = (String)view.getTag().toString();
                categorySelect(str);
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.stats:
                startActivity(new Intent(CategoriesActivity.this, StatisticsActivity.class));
                finish();
                return true;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CategoriesActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.about_app:
                Toast.makeText(getApplicationContext(),"Created by Aviv, Nissan & Moshe",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void categorySelect(String string){
//        String string = view.getTag().toString();

//        Toast.makeText(getApplicationContext(),string , Toast.LENGTH_LONG).show();
        System.out.println(string);
        Intent intent = new Intent(CategoriesActivity.this, QuestionsActivity.class);
        //will send the category_item to other activity
        intent.putExtra("category_item", string);
        intent.putExtra("permissions", permissions);

        startActivity(intent);
    }
    //FIXME adds double the list
//    void removeAllChildViews(ViewGroup viewGroup) {
//        for (int i = 0; i < viewGroup.getChildCount(); i++) {
//            View child = viewGroup.getChildAt(i);
//            if (child instanceof ViewGroup) {
//                if (child instanceof AdapterView) {
//                    viewGroup.removeView(child);
//                    return;
//                }
//                removeAllChildViews(((ViewGroup) child));
//            } else {
//                viewGroup.removeView(child);
//            }
//        }
//    }

}
