package com.project.jewelmart.swarnsarita;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.project.jewelmart.swarnsarita.R;
import com.project.jewelmart.swarnsarita.adapters.AdapterCollectionList;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.MyCollection;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;

import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCollectionActivity extends AppCompatActivity {

    private View parent_view;
    APIInterface apiInterface;
    List<MyCollection> list;
    private RecyclerView recyclerView;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    UserSessionManager userSessionManager;
    AdapterCollectionList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_my_collection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("My Catalogue");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Tools.isTablet(MyCollectionActivity.this)) {
            MyCollectionActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            MyCollectionActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        userSessionManager = new UserSessionManager(this);
        parent_view = findViewById(android.R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        if (!CheckNetwork.isConnected(getApplicationContext())) {
            Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
        } else {
            getCollection(userSessionManager.getUserID());
        }
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(badgenew View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    public void getCollection(String userid) {
        Call<List<MyCollection>> countriesCall = apiInterface.doGetCollection(userid);
        countriesCall.enqueue(new Callback<List<MyCollection>>() {
            @Override
            public void onResponse(Call<List<MyCollection>> call,
                                   Response<List<MyCollection>> response) {
                Log.d("getCollection", response.code() + "");
                final List<MyCollection> resource = response.body();
                if (resource != null) {
                    list = resource;
                    setAdapter();
                }
            }

            @Override
            public void onFailure(Call<List<MyCollection>> call, Throwable t) {
                Log.d("getCollection", t.toString() + "");
                call.cancel();
            }
        });

    }

    private void setAdapter() {
        //set data and list adapter
        mAdapter = new AdapterCollectionList(this, list, animation_type);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterCollectionList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, MyCollection obj, int position) {
                // Snackbar.make(parent_view, "Item " + obj.getCollectionName() + " clicked", Snackbar.LENGTH_SHORT).show();
                Intent intent = new Intent(MyCollectionActivity.this, ProductGridActivity.class);
                intent.putExtra("collection_id", "");
                intent.putExtra("name", obj.getCollectionName());
                intent.putExtra("my_collection_id", obj.getId());
                intent.putExtra("table", "product_master");
                intent.putExtra("mode_type", "my_collection");
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
