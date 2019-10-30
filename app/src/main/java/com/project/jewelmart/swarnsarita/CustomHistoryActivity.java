package com.project.jewelmart.swarnsarita;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jewelmart.swarnsarita.adapters.AdapterCustomHistory;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.CustomHistory;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomHistoryActivity extends AppCompatActivity {
    APIInterface apiInterface;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<CustomHistory.Data.DataList> listOrder;
    AdapterCustomHistory mAdapter;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_custom_history);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Custom Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        if (CheckNetwork.isConnected(CustomHistoryActivity.this)) {
            CustomizedOrder();
        } else {
            Tools.showCustomDialog(CustomHistoryActivity.this, "Alert !", "No Internet Connection , Please Try after Connecting");
        }
    }

    public void CustomizedOrder() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(CustomHistoryActivity.this);
        Call<CustomHistory> countriesCall = apiInterface.CustomizedOrder(SingletonSupport.getInstance().user_id,
                SingletonSupport.getInstance().usertype);
        countriesCall.enqueue(new Callback<CustomHistory>() {
            @Override
            public void onResponse(Call<CustomHistory> call, Response<CustomHistory> response) {
                pdg.dismiss();
                try {
                    Log.d("CustomizedOrder", response.code() + "");
                    final CustomHistory resource = response.body();
                    if (resource != null) {
                        if (resource.getAck().equals("1")) {
                            path = resource.getPath();
                            if (!resource.getData().getDataList().isEmpty()) {
                                listOrder = resource.getData().getDataList();
                                setAdapter();
                            }else {
                                Tools.showCustomDialog(CustomHistoryActivity.this, "Alert !", "No data found");
                            }
                        } else {
                            Toast.makeText(CustomHistoryActivity.this, resource.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CustomHistory> call, Throwable t) {
                pdg.dismiss();
                Log.d("CustomizedOrder", t.toString() + "");
                call.cancel();
            }
        });
    }

    public void setAdapter() {
        //set data and list adapter
        if (listOrder.size() > 0) {
            mAdapter = new AdapterCustomHistory(CustomHistoryActivity.this, listOrder,path);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new AdapterCustomHistory.OnItemClickListener() {
                @Override
                public void onItemClick(View view, CustomHistory.Data.DataList obj, int pos) {
                   /* Intent intent = badgenew Intent(CustomHistoryActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("product_id", obj.getValue().get(0));
                    startActivity(intent);*/
                }
            });
        } else {
            Tools.showCustomDialog(CustomHistoryActivity.this, "Alert !", "No data found");
        }

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
