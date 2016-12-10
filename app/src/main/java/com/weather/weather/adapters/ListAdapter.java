package com.weather.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weather.weather.R;
import com.weather.weather.helper.GetColors;
import com.weather.weather.helper.TypeWeather;


public class ListAdapter extends BaseAdapter {

    private  Context mContext;
    private  LayoutInflater lInflater;
    private  String[][] values;



    static class ViewHolder {
        TextView txItemTemp;
        TextView txItemPress;
        TextView txDate;
        ImageView imItem;
        LinearLayout linearLayout;
    }

    public ListAdapter(String[][] values) {


        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            mContext = parent.getContext();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list_weather, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txItemTemp = (TextView) convertView.findViewById(R.id.tx_temperature);
            viewHolder.txItemPress = (TextView) convertView.findViewById(R.id.tx_pressure);
            viewHolder.txDate = (TextView) convertView.findViewById(R.id.tx_date_list);
            viewHolder.imItem = (ImageView) convertView.findViewById(R.id.im_weather);
            viewHolder.linearLayout = (LinearLayout)convertView.findViewById(R.id.li_info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String[] infoWeather = getItem(position);
        viewHolder.txDate.setText(infoWeather[0]);
        viewHolder.txItemTemp.setText(infoWeather[1]);
        viewHolder.txItemPress.setText(infoWeather[2]);


        GetColors getColors = new GetColors();
        long[] colors = getColors.GetImage(TypeWeather.getValue(infoWeather[3]));


                viewHolder.imItem.setImageResource((int)colors[0]);
                viewHolder.imItem.setBackgroundResource((int)colors[1]);
                viewHolder.linearLayout.setBackgroundResource((int)colors[2]);



        return convertView;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public String[] getItem(int i) {
        return values[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
