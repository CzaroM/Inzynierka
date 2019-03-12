package com.example.czaro.inzynierka;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private Activity context;
    private List<Category> categories;
    private List<Double> categoryBalance;
    private final static int minusBalance = 0;

    public CategoryAdapter(Activity context, List<Category> categories, List<Double> categoryBalance){
        super(context,R.layout.category_layout, categories);
        this.context=context;
        this.categories=categories;
        this.categoryBalance=categoryBalance;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem= inflater.inflate(R.layout.category_layout,null,true);
        TextView textViewCategoryTitle=(TextView) listViewItem.findViewById(R.id.textViewCategoryTitle);
        TextView textViewCategoryBalance=(TextView) listViewItem.findViewById(R.id.textViewCategoryBalance);
        Category category=categories.get(position);
        Double categoryBalance = this.categoryBalance.get(position);
        textViewCategoryTitle.setText(category.getName());
        if(categoryBalance < minusBalance){
            textViewCategoryBalance.setTextColor(Color.RED);
            textViewCategoryBalance.setText("" + categoryBalance);
        }
        else if(categoryBalance == minusBalance){
            textViewCategoryBalance.setTextColor(Color.BLACK);
            textViewCategoryBalance.setText("" + categoryBalance);
        }
        else{
            textViewCategoryBalance.setTextColor(Color.rgb(115,139,40 ));
            textViewCategoryBalance.setText("+ " + categoryBalance);
    }
        return listViewItem;
    }
}
