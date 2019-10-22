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

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListView searchListView;
    public static ArrayList<String> building2Marker= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Intent intent = getIntent();
        Log.d("search", "intent");
        building2Marker = intent.getStringArrayListExtra("building");

        searchView = findViewById(R.id.searchview);
        searchListView = findViewById(R.id.searchlistview);

        //show the submission button
        searchView.setSubmitButtonEnabled(true);
        //show hint to enter query
        searchView.setQueryHint("please enter ...");
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchView.setIconifiedByDefault(false);
        //setup adapter for listview
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.search_list, building2Marker);
        searchListView.setAdapter(adapter);
        searchListView.setTextFilterEnabled(false);
        Filter filter = adapter.getFilter();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("text", "onQueryTextSubmit");
                filter.filter(query);
                if (searchListView.getAdapter().getCount() == 0){
                    Toast.makeText(SearchActivity.this, "No Results" , Toast.LENGTH_SHORT).show();
                }
                else{
                    String result_query = (String)searchListView.getItemAtPosition(0);
                    Intent result_intent = new Intent();
                    result_intent.putExtra("Query", result_query);
                    setResult(RESULT_OK,result_intent);
                    finish();
                }

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    filter.filter("");
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
                Intent resulti = new Intent();
                resulti.putExtra("Query", searchListView.getAdapter().getItem(i).toString());
                setResult(RESULT_OK,resulti);
                finish();
            }
        });
    }

}
