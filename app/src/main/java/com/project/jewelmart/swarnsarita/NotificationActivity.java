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

import com.project.jewelmart.swarnsarita.adapters.AdapterNotificationList;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.MarketModel;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.pojo.NotificationModel;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.worker.OrderDetailsActivity;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    APIInterface apiInterface;
    List<NotificationModel.Datum> list;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    UserSessionManager userSessionManager;
    AdapterNotificationList mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        userSessionManager = new UserSessionManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        fetchNotification();
        userSessionManager.setNotifiacation("no");
    }


    public void fetchNotification() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(NotificationActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<NotificationModel> countriesCall = apiInterface.getNotification(userSessionManager.getUserID(), userSessionManager.getUserType());
        countriesCall.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                Log.d("NotificationModel", response.code() + "");
                pdg.dismiss();
                try {
                    final NotificationModel resource = response.body();
                    if (resource.getAck().equalsIgnoreCase("1")) {
                        if (resource.getData() != null) {
                            list = resource.getData();
                            setAdapter();
                        } else {
                            Tools.showCustomDialog(NotificationActivity.this, "Alert !", "No Data Found");
                        }
                    } else {
                        Tools.showCustomDialog(NotificationActivity.this, "Alert !", resource.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                Log.d("NotificationModel", t.toString() + "");
                pdg.dismiss();
                call.cancel();
            }
        });
    }

    private void setAdapter() {
        //set data and list adapter
        if (list != null) {
            mAdapter = new AdapterNotificationList(this, list, animation_type);
            recyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new AdapterNotificationList.OnItemClickListener() {
                @Override
                public void onItemClick(View view, NotificationModel.Datum obj, int position) {
                    //Tools.showCustomDialog(OrderHistoryActivity.this, "Alert", "Clicked : " + obj.getOrderId());
                    Intent intent;
                    if (SingletonSupport.getInstance().usertype.equalsIgnoreCase("client")) {
                        if (obj.getFrom_push().equalsIgnoreCase("marketing")){
                            getMarketingData(obj.getOrderId());
                        }else {
                            intent = new Intent(NotificationActivity.this, ProductDetailActivity.class);
                            intent.putExtra("mode_type", "home_products");
                            intent.putExtra("table", "product_master");
                            intent.putExtra("collection_id", "");
                            intent.putExtra("my_collection_id", "");
                            intent.putExtra("old", true);
                            intent.putExtra("collection_sku_code", obj.getProductId());
                            startActivity(intent);
                        }
                    } else {
                        if (obj.getSubTitle().toLowerCase().contains("custom")) {
                            intent = new Intent(NotificationActivity.this, CustomHistoryActivity.class);
                            //intent.putExtra("product_id", obj.getProductId());
                            startActivity(intent);
                        } else {
                            intent = new Intent(NotificationActivity.this, OrderDetailsActivity.class);
                            intent.putExtra("product_id", obj.getProductId());
                            startActivity(intent);
                        }
                    }
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            });
        } else {
            Tools.showCustomDialog(NotificationActivity.this, "Alert !", "No Order Found !");
        }

    }


    public void getMarketingData(String marketingid) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(NotificationActivity.this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MarketModel> countriesCall = apiInterface.getMarketing(marketingid);
        countriesCall.enqueue(new Callback<MarketModel>() {
            @Override
            public void onResponse(Call<MarketModel> call, Response<MarketModel> response) {
                Log.d("MarketModel", response.code() + "");
                pdg.dismiss();
                try {
                    final MarketModel resource = response.body();
                    if (resource.getAck().equalsIgnoreCase("1")) {
                        if (resource.getData() != null) {
                            Intent intent=new Intent(NotificationActivity.this, BannerDetialActivity.class);
                            intent.putExtra("title",resource.getData().getPurpose());
                            intent.putExtra("desc",resource.getData().getDescription());
                            intent.putExtra("img",resource.getData().getPath()+resource.getData().getImageName());
                            startActivity(intent);
                        } else {
                            Tools.showCustomDialog(NotificationActivity.this, "Alert !", "No Data Found");
                        }
                    } else {
                        Tools.showCustomDialog(NotificationActivity.this, "Alert !", resource.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MarketModel> call, Throwable t) {
                Log.d("MarketModel", t.toString() + "");
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
