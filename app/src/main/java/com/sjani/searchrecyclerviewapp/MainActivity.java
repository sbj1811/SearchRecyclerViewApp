package com.sjani.searchrecyclerviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.sjani.searchrecyclerviewapp.data.ApiConnection;
import com.sjani.searchrecyclerviewapp.model.Pizza;
import com.sjani.searchrecyclerviewapp.model.PizzaItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    public static final String TAG = MainActivity.class.getName();
    public static final String NAME = "name";
    public static final String POSITION = "position";

    @BindView(R.id.rv)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        adapter = new ListAdapter(this);
        layoutManager =  new LinearLayoutManager(this);

        ApiConnection.getApi().getPizzas().enqueue(new Callback<List<Pizza>>() {
            @Override
            public void onResponse(Call<List<Pizza>> call, Response<List<Pizza>> response) {
                List<PizzaItem> pizzaItems =  new ArrayList<>();
                pizzaItems.addAll(response.body().get(0).getChefSChoice());
                pizzaItems.addAll(response.body().get(0).getSignature());
                pizzaItems.addAll(response.body().get(0).getClassics());
                pizzaItems.addAll(response.body().get(0).getVegetarian());
                recyclerView.setLayoutManager(layoutManager);
                adapter.swapResults(pizzaItems);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<List<Pizza>> call, Throwable t) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void itemClick(int position,String name) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(NAME,name);
        intent.putExtra(POSITION,position);
        startActivity(intent);
    }
}
