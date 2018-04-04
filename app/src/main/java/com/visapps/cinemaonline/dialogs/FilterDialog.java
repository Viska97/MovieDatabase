package com.visapps.cinemaonline.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.visapps.cinemaonline.R;
import com.visapps.cinemaonline.models.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Visek on 16.03.2018.
 */

public class FilterDialog extends DialogFragment {

    public interface FilterDialogCallback{
        void onFilterSelected(String name, String year, String genre, String country, String orderby);
    }

    private String[] ordervalues = {"date", "rating", "price", "year"};

    private FilterDialogCallback callback;

    private EditText name;
    private Spinner genre, country, year, orderby;
    private int genrepos=0, countrypos=0, yearpos=0, orderbypos=0;
    private String namevalue=null;
    private ArrayAdapter<String> genreadapter;
    private ArrayAdapter<String> countryadapter;
    private ArrayAdapter<String> yearadapter;
    private ArrayAdapter<String> orderadapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);
        name = view.findViewById(R.id.name);
        name.setText(namevalue);
        genre = view.findViewById(R.id.genre);
        country = view.findViewById(R.id.country);
        year = view.findViewById(R.id.year);
        orderby = view.findViewById(R.id.orderby);
        genre.setAdapter(genreadapter);
        country.setAdapter(countryadapter);
        year.setAdapter(yearadapter);
        orderby.setAdapter(orderadapter);
        year.setSelection(yearpos);
        genre.setSelection(genrepos);
        country.setSelection(countrypos);
        orderby.setSelection(orderbypos);
        builder.setView(view)
                .setTitle(getString(R.string.filterfilms))
                .setCancelable(false)
                .setNegativeButton(R.string.reset, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ResetFilter();
                    }
                })
                .setPositiveButton(R.string.apply, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ApplyFilter();
                    }
                });
        return builder.create();
    }

    public void setFilter(Filter filter, Context context){
        List<String> orderby = new ArrayList<>();
        orderby.add(context.getString(R.string.orderdateadded));
        orderby.add(context.getString(R.string.orderrating));
        orderby.add(context.getString(R.string.orderprice));
        orderby.add(context.getString(R.string.orderyear));
        List<String> year = new ArrayList<>();
        List<String> genre = new ArrayList<>();
        List<String> country = new ArrayList<>();
        year.addAll(filter.getYears());
        genre.addAll(filter.getGenres());
        country.addAll(filter.getCountries());
        year.add(0,context.getString(R.string.all));
        genre.add(0, context.getString(R.string.all));
        country.add(0,context.getString(R.string.all));
        genreadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, genre);
        genreadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countryadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, country);
        countryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, year);
        yearadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderadapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, orderby);
        orderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public void setCallback(FilterDialogCallback callback){
        this.callback = callback;
    }

    private void ApplyFilter(){
        genrepos = genre.getSelectedItemPosition();
        countrypos= country.getSelectedItemPosition();
        yearpos=year.getSelectedItemPosition();
        orderbypos = orderby.getSelectedItemPosition();
        String yearstr = year.getSelectedItem().toString();
        String genrestr = genre.getSelectedItem().toString();
        String countrystr =country.getSelectedItem().toString();
        String orderstr = ordervalues[orderby.getSelectedItemPosition()];
        if(yearpos == 0){
            yearstr = null;
        }
        if(genrepos == 0){
            genrestr = null;
        }
        if(countrypos == 0){
            countrystr = null;
        }
        if(orderbypos == 0){
            orderstr = null;
        }
        if(!name.getText().toString().equals("")){
            namevalue = name.getText().toString();
        }
        else{
            namevalue = null;
        }
        callback.onFilterSelected(namevalue,yearstr, genrestr,countrystr, orderstr);
    }

    private void ResetFilter(){
        namevalue = null;
        genrepos=0;
        countrypos=0;
        yearpos=0;
        orderbypos=0;
        callback.onFilterSelected(null, null, null,null, null);
    }

}
