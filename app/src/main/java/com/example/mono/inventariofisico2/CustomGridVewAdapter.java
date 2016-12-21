package com.example.mono.inventariofisico2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomGridViewAdapter extends BaseAdapter {

    private Context context;
    private String[] items;
    LayoutInflater inflater;
    private String cell;

    public CustomGridViewAdapter(Context context, String[] items,String cell) {
        this.context = context;
        this.items = items;
        this.cell=cell;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            if (cell == "grid") {
                convertView = inflater.inflate(R.layout.row_grid, null);
            }else{
                convertView = inflater.inflate(R.layout.row_grid_titulos, null);
            }
        }
        TextView button = (TextView) convertView.findViewById(R.id.item_text);
        button.setText(items[position]);

        return convertView;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}