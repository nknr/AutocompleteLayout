package com.artisanglory.autocompletelayout;

import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    SearchView.SearchAutoComplete searchAutoComplete;
    CustomAdapter adapter;
    ArrayList<Exercise> mExercises;

    // place base url
    public final static String GET_CALORIE_LIST = /*BASE_URL +*/ "search_calorie_item_list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, mExercises.get(i).getName()+ " "+ mExercises.get(i).getCalorie(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() >0){
            updateList(newText.trim());
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem searchMenu = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        searchAutoComplete.setOnItemClickListener(this);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    private void updateList(final String query){
        mExercises = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.POST,GET_CALORIE_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MainActivity ",response);
                try {
                    JSONObject main = new JSONObject(response);
                    JSONObject jsonStatus = main.getJSONObject("response");
                    String status = jsonStatus.getString("status");
                    if (status.equals("yes")) {
                        JSONArray jsonList = jsonStatus.getJSONArray("list");
                        for (int i = 0; i < jsonList.length(); i++) {
                            JSONObject object = jsonList.getJSONObject(i);
                            String name = object.getString("name");
                            double calorie = object.getDouble("calorie");
                            mExercises.add(new Exercise(name,calorie));
                        }
                        adapter = new CustomAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line,mExercises);
                        searchAutoComplete.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("item_name",query);
                params.put("item_type","activity");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
