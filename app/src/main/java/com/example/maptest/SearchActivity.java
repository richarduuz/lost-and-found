package com.example.maptest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListView searchListView;
    private ArrayList<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Intent intent = getIntent();
        list.clear();
        list = intent.getStringArrayListExtra("building");

        searchView = findViewById(R.id.searchview);

        //show the submission button
        searchView.setSubmitButtonEnabled(true);
        //show hint to enter query
        searchView.setQueryHint("please enter ...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        searchListView = findViewById(R.id.searchlistview);

        //setup adapter for listview
        System.out.println("New Adapter");
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.search_list, list);
        searchListView.setAdapter(adapter);
        searchListView.setTextFilterEnabled(false);
        //filter for searching
        Filter filter = adapter.getFilter();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("text", "onQueryTextSubmit");
                Intent resultintent = new Intent();
                resultintent.putExtra("Query", query);
                setResult(RESULT_OK,resultintent);
                finish();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    filter.filter("?");
                    Log.d("text", "empty");
                }else{
                    filter.filter(newText);
                    Log.d("text", "newText: "+newText);
                }
                return true;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Search", searchListView.getAdapter().getItem(i).toString());
                Intent resultintent = new Intent();
                resultintent.putExtra("Query", searchListView.getAdapter().getItem(i).toString());
                setResult(RESULT_OK,resultintent);
                finish();
            }
        });
    }

}
