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
import java.util.Optional;

public class OperationAdapter extends ArrayAdapter<Operation> {
    private Activity context;
    private List<Operation>operations;
    private static final String minusBalance="Wydatek";
    private String displayType;
    public OperationAdapter(Activity context, List<Operation> operations){
        super(context,R.layout.opeartion_layout, operations);
        //context.setTitle("Oceń to! - Moje oceny");
        this.context=context;
        this.operations=operations;
        this.displayType="Default";
    }

    public OperationAdapter(Activity context, List<Operation> operations, String displayType){
        super(context,R.layout.opeartion_layout, operations);
        //context.setTitle("Oceń to! - Moje oceny");
        this.context=context;
        this.operations=operations;
        this.displayType=displayType;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem= inflater.inflate(R.layout.opeartion_layout,null,true);
        TextView textViewOperationTitle=(TextView) listViewItem.findViewById(R.id.textViewOperationTitle);
        TextView textViewOperationCategory=(TextView) listViewItem.findViewById(R.id.textViewOperationCategory);
        TextView textViewOperationPrice=(TextView) listViewItem.findViewById(R.id.textViewOperationPrice);
        Operation operation=operations.get(position);
        if(displayType.equals("Default")) {
            textViewOperationTitle.setText(operation.getTitle());
            textViewOperationCategory.setText(operation.getCategory());
            if (operation.getBalance().equals(minusBalance)) {
                textViewOperationPrice.setTextColor(Color.RED);
                textViewOperationPrice.setText("- " + Double.toString(operation.getPrice()));
            } else {
                textViewOperationPrice.setTextColor(Color.rgb(115,139,40 ));
                textViewOperationPrice.setText("+ " + Double.toString(operation.getPrice()));
            }
        }
        else if(displayType.equals("History")){
            if (operation.getBalance().equals(minusBalance)) {
                textViewOperationTitle.setTextColor(Color.RED);
                textViewOperationTitle.setText("- " + Double.toString(operation.getPrice()));
            } else {
                textViewOperationTitle.setTextColor(Color.rgb(115,139,40 ));
                textViewOperationTitle.setText("+ " + Double.toString(operation.getPrice()));
            }
            textViewOperationPrice.setText(operation.getDate());
            textViewOperationCategory.setText(operation.getCategory()+ ", " + operation.getTitle());

        }
        return listViewItem;
    }
}
