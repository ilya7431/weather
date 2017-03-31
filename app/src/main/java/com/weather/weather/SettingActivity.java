package com.weather.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.weather.weather.data.model.City;
import com.weather.weather.helper.PrefUtils;

public class SettingActivity extends AppCompatActivity  {
    private Context mContext;

    private EditText exEnterCity;
    private Switch swUpdateWIFI;
    private Switch swEnterCity;
    private Switch swUpdateTime;
    private Spinner spUpdateTime;
    private Button enter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        mContext = this;


        exEnterCity = (EditText) findViewById(R.id.ac_enter_city);
        swUpdateWIFI = (Switch)findViewById(R.id.sw_update_wifi);
        swEnterCity = (Switch)findViewById(R.id.sw_enable_enter_city);
        swUpdateTime = (Switch)findViewById(R.id.sw_update_time);
        spUpdateTime = (Spinner)findViewById(R.id.sp_time_update);

        enter = (Button)findViewById(R.id.bt_Enter);
        enter.setOnClickListener(listenerEnter);

            swUpdateWIFI.setChecked(PrefUtils.getPrefUpdateWIFI(mContext));

            swEnterCity.setChecked(PrefUtils.getPrefSwitchAutoSearch(mContext));

        spUpdateTime.setAdapter(new ArrayAdapter<String>(mContext,
                R.layout.spinner_item,
                new String[]{getString(R.string.title_spinner_update_time), "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"}));
        spUpdateTime.setVisibility(View.INVISIBLE);


        if (PrefUtils.getPrefSwitchAutoSearch(mContext)){
            exEnterCity.setEnabled(false);
            exEnterCity.setHint(R.string.Auto_Search);
            startLocation(true,"");
            }else{
            exEnterCity.setEnabled(true);
            exEnterCity.setHint(R.string.Enter_City);
            }

        swUpdateWIFI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PrefUtils.setPrefUpdateWIFI(mContext,b);
            }
        });
        if (PrefUtils.getPrefSwitchTimeUpdate(mContext)) swUpdateTime.setChecked(true);

        swEnterCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                PrefUtils.setPrefSwitchAutoSearch(mContext,b);
                if (b){
                    exEnterCity.setEnabled(false);
                    exEnterCity.setHint(R.string.Auto_Search);
                    enter.setVisibility(View.INVISIBLE);
                    enter.setEnabled(false);
                    startLocation(true,"");


                }else{
                    exEnterCity.setEnabled(true );
                    exEnterCity.setHint(R.string.Enter_City);
                    enter.setVisibility(View.VISIBLE);
                    enter.setEnabled(true);
                }

            }
        });

        swUpdateTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    spUpdateTime.setVisibility(View.VISIBLE);
                    PrefUtils.setPrefSwitchTimeUpdate(mContext,true);
                }else {
                    spUpdateTime.setVisibility(View.INVISIBLE);
                    PrefUtils.setPrefSwitchTimeUpdate(mContext,false);
                }
            }
        });

        spUpdateTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    PrefUtils.setPrefTimeUpdate(mContext,position);

                }else swUpdateTime.setChecked(false);
                //TODO start services update on time
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                swUpdateTime.setChecked(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            setResult(RESULT_OK);
            //TODO
        }else {
            swEnterCity.setChecked(false);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id) {

            case android.R.id.home: {

                City city = PrefUtils.getPrefLocation(mContext);
                if (!city.name.equals("")) {

                    setResult(RESULT_OK);
                    finish();


                } else Toast.makeText(mContext, R.string.Enter_City, Toast.LENGTH_LONG).show();


                break;
            }

        }


        return super.onOptionsItemSelected(item);
    }


    View.OnClickListener listenerEnter = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                if (exEnterCity.getText().length() > 2) {
                    startLocation(false,exEnterCity.getText().toString());

                }else Toast.makeText(mContext, R.string.Nope_City, Toast.LENGTH_LONG).show();

            } catch (Exception e) {

            }
        }
    };

    /**
     *
     * @param searchGPS
     * @param nameCity if searchGPS false then need send name
     */
    private  void startLocation(boolean searchGPS,String nameCity){
        Intent intent = new Intent(mContext,LocationUtilActivity.class);
        intent.putExtra(getString(R.string.Intent_searchGPS),searchGPS);
        if (!searchGPS) intent.putExtra(mContext.getString(R.string.Location_name_city),nameCity);
        startActivityForResult(intent,1);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            City city = PrefUtils.getPrefLocation(mContext);
            if (!city.name.equals("")) {

                setResult(RESULT_OK);
                finish();

            } else Toast.makeText(mContext, R.string.Enter_City, Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }



}
