package com.dd.janyltpashtar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mListview1;
    private SearchView mSvSearch;

    private void initView() {
        mListview1 = (ListView) findViewById(R.id.listview1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<Product> products = new ArrayList<>();
        for (String stringJanyltpash : databaseAccess.getList()) {
            products.add(new Product(stringJanyltpash));
        }
        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), products);
        mListview1.setAdapter(myAdapter);
        databaseAccess.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        SearchView mSvSearch = (SearchView) myActionMenuItem.getActionView();

        mSvSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // collapse the view ?
                //menu.findItem(R.id.menu_search).collapseActionView();
                Log.e("queryText", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
                databaseAccess.open();
                List<String> stringList = new ArrayList<>();
                for (String str : databaseAccess.getList()) {
                    if (str.trim().contains(newText))
                        stringList.add(str);
                }

                List<Product> products = new ArrayList<>();
                for (String stringJanyltpash : databaseAccess.getList()) {
                    if (stringJanyltpash.trim().contains(newText)){
                        products.add(new Product(stringJanyltpash));
                    }
                }
                MyAdapter myAdapter = new MyAdapter(getApplicationContext(), products);
                mListview1.setAdapter(myAdapter);
                databaseAccess.close();

                return false;
            }
        });
        return true;
    }
}
