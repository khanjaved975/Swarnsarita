package com.project.jewelmart.swarnsarita.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.project.jewelmart.swarnsarita.CustomHistoryActivity;
import com.project.jewelmart.swarnsarita.NotificationActivity;
import com.project.jewelmart.swarnsarita.adapters.AdapterWorkerList;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.WorkerModel;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private List<WorkerModel.DataList> listOrder;
    private AdapterWorkerList mAdapter;
    UserSessionManager userSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Orders");
        userSessionManager = new UserSessionManager(this);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        if (!CheckNetwork.isConnected(this)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Tools.showCustomDialog(WorkerActivity.this, "Alert !", "No Internet Connection , Please Try after Connecting");
                }
            });
        } else {
            GetOrderWorker();
        }
    }

    public void GetOrderWorker() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(WorkerActivity.this);
        Call<WorkerModel> countriesCall = apiInterface.WorkerOrder(SingletonSupport.getInstance().user_id);
        countriesCall.enqueue(new Callback<WorkerModel>() {
            @Override
            public void onResponse(Call<WorkerModel> call, Response<WorkerModel> response) {
                pdg.dismiss();
                try {
                    Log.d("GetOrderWorker", response.code() + "");
                    final WorkerModel resource = response.body();
                    if (resource != null) {
                        if (resource.getDataList() != null) {
                            listOrder = new ArrayList<>();
                            listOrder = resource.getDataList();
                            setAdapter();
                        } else {
                            Tools.showCustomDialog(WorkerActivity.this, "Alert !", "No data found");
                        }
                    } else {
                        Tools.showCustomDialog(WorkerActivity.this, "Alert !", "No data found");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WorkerModel> call, Throwable t) {
                pdg.dismiss();
                Log.d("GetOrderWorker", t.toString() + "");
                call.cancel();
            }
        });
    }

    private void setAdapter() {
        //set data and list adapter
        if (listOrder.size() > 0) {
            mAdapter = new AdapterWorkerList(WorkerActivity.this, listOrder);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new AdapterWorkerList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, WorkerModel.DataList obj, int pos) {
                    Intent intent = new Intent(WorkerActivity.this, com.project.jewelmart.swarnsarita.worker.OrderDetailsActivity.class);
                    intent.putExtra("product_id", obj.getProduct_id());
                    startActivity(intent);
                }
            });
        } else {
            Tools.showCustomDialog(WorkerActivity.this, "Alert !", "No data found");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_worker_home, menu);
        int color = Color.parseColor(getResources().getString(R.string.color));
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               /* finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);*/
                return true;

            case R.id.action_logout:
                Tools.showDialogLogout(WorkerActivity.this, "Alert !", "Are you sure you want to logout ?");
                return true;

            case R.id.action_notification:
                if (!CheckNetwork.isConnected(getApplicationContext())) {
                    Tools.showCustomDialog(getApplicationContext(), "Alert !", "No Internet Connection , Please Try after Connecting");
                } else {
                    Intent intent = new Intent(WorkerActivity.this, NotificationActivity.class);
                    startActivity(intent);
                    //getDrawerList(userSessionManager.getUserID());
                }
                return true;

            case R.id.action_refresh:
                GetOrderWorker();
                return true;

            case R.id.action_custom_order:
                Intent intent = new Intent(WorkerActivity.this, CustomHistoryActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
