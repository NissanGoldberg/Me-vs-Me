package com.example.student.trivia_app.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.student.trivia_app.R;
import com.example.student.trivia_app.activities.CategoriesActivity;
import com.example.student.trivia_app.activities.QuestionsActivity;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Student on 23/12/2018.
 */

public class CategoryAdapter extends BaseAdapter {

    private final Context mContext;
    private final NewCat[] categories;
    final Map<String,Integer> images = new HashMap<>();

    // 1
    public CategoryAdapter(Context context, NewCat[] categories) {
        this.mContext = context;
        this.categories = categories;
        images.put("CPP",R.drawable.cpp_logo);
        images.put("C",R.drawable.c_logo);
        images.put("Java",R.drawable.java_logo);
        images.put("Python",R.drawable.python_logo);
    }

    // 2
    @Override
    public int getCount() {
        return categories.length;
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView dummyTextView = new TextView(mContext);
//        dummyTextView.setText(String.valueOf(position));
//        return dummyTextView;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final NewCat cat = categories[position];

        // 2
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.category_item, null);
        }

        // 3
        final ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
        final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);

        // 4
//        Picasso.get().load(cat.getUrl()).into(imageView);
//        nameTextView.setText(mContext.getString(book.getName()));
        if(cat.getUrl().equals(""))
            imageView.setImageResource(images.get(cat.getName()));
        else
            Picasso.get().load(cat.getUrl()).into(imageView);
//        imageView.setImageResource(R.drawable.cpp_logo);
        nameTextView.setText(cat.getName());
//        authorTextView.setText(cat.getName());
        authorTextView.setText("");
        final LinearLayout linearLayout_cat = (LinearLayout) convertView.findViewById(R.id.ll_cat_grid);
        linearLayout_cat.setTag(cat.getName());
//        imageView.setTag(cat.getName()); //to send to intent TODO check if it works

        return convertView;
    }
}