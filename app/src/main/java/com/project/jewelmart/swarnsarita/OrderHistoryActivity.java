package com.project.jewelmart.swarnsarita;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.project.jewelmart.swarnsarita.adapters.AdapterOrderHistoryList;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.OrderHistory;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Status;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;

import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private View parent_view;
    APIInterface apiInterface;
    List<OrderHistory> list;
    private RecyclerView recyclerView;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    UserSessionManager userSessionManager;
    AdapterOrderHistoryList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            if (SingletonSupport.getInstance().isLogin) {
                getOrderHistory(userSessionManager.getUserID(), "product_master");
            } else {
                Tools.showCustomDialog(OrderHistoryActivity.this, "Alert !", "Please Login");
            }
        }
    }

    public void getOrderHistory(String userid, String table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(OrderHistoryActivity.this);
        Call<List<OrderHistory>> countriesCall = apiInterface.OrderHistory(userid, table);
        countriesCall.enqueue(new Callback<List<OrderHistory>>() {
            @Override
            public void onResponse(Call<List<OrderHistory>> call,
                                   Response<List<OrderHistory>> response) {
                try {
                    Log.d("OrderHistory", response.code() + "");
                    pdg.dismiss();
                    final List<OrderHistory> resource = response.body();
                    list = resource;
                    setAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<OrderHistory>> call, Throwable t) {
                Log.d("OrderHistory", t.toString() + "");
                pdg.dismiss();
                call.cancel();
            }
        });
    }

    private void setAdapter() {
        //set data and list adapter
        if (list != null) {
            mAdapter = new AdapterOrderHistoryList(this, list, animation_type);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new AdapterOrderHistoryList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, OrderHistory obj, int position) {
                    //Tools.showCustomDialog(OrderHistoryActivity.this, "Alert", "Clicked : " + obj.getOrderId());
                    Intent intent = new Intent(OrderHistoryActivity.this, ProductDetailActivity.class);
                    intent.putExtra("mode_type", "home_products");
                    intent.putExtra("table", "product_master");
                    intent.putExtra("collection_id", "");
                    intent.putExtra("my_collection_id", "");
                    intent.putExtra("old", true);
                    intent.putExtra("collection_sku_code", obj.getProductId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });

            mAdapter.setOnItemDeleteClickListener(new AdapterOrderHistoryList.OnItemDeleteClickListener() {
                @Override
                public void onItemDeleteClick(View view, OrderHistory obj, int position) {
                    getOrderDelete(obj.getOrderId(), position);
                }
            });

            mAdapter.setOnItemReOrderClickListener(new AdapterOrderHistoryList.OnItemReOrderClickListener() {
                @Override
                public void onItemReOrderClick(View view, OrderHistory obj, int position) {
                    getReOrder(SingletonSupport.getInstance().user_id, obj.getOrderId());
                }
            });

        } else {
            Tools.showCustomDialog(OrderHistoryActivity.this, "Alert !", "No Order Found !");
        }

    }

    public void getOrderDelete(String orderid, final int position) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(OrderHistoryActivity.this);

        Call<Status> countriesCall = apiInterface.OrderDeleteHistory(orderid);
        countriesCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call,
                                   Response<Status> response) {
                try {
                    Log.d("OrderHistory", response.code() + "");
                    pdg.dismiss();
                    Status resource = response.body();
                    if (resource.getAck().toString().equalsIgnoreCase("1")) {
                        mAdapter.removeAt(position);
                        Toast.makeText(OrderHistoryActivity.this, resource.getError(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OrderHistoryActivity.this, resource.getError(), Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("getOrderDelete", t.toString() + "");
                pdg.dismiss();
                call.cancel();
            }
        });

    }

    public void getReOrder(String userid, String orderid) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(OrderHistoryActivity.this);
        Call<Status> countriesCall = apiInterface.ReOrderHistory(orderid, userid);
        countriesCall.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call,
                                   Response<Status> response) {
                try {
                    Log.d("OrderHistory", response.code() + "");
                    pdg.dismiss();
                    final Status resource = response.body();
                    Toast.makeText(OrderHistoryActivity.this, resource.getError(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                Log.d("getReOrder", t.toString() + "");
                pdg.dismiss();
                call.cancel();
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
