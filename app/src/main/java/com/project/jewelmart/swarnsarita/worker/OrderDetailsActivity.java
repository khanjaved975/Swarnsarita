package com.project.jewelmart.swarnsarita.worker;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.project.jewelmart.swarnsarita.adapters.AdapterImageSlider;
import com.project.jewelmart.swarnsarita.adapters.AdapterWorkerOrderDetailsList;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.models.OrderDetailModel;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private APIInterface apiInterface;
    private RecyclerView recyclerView;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    private ViewPager viewPager;
    private List<OrderDetailModel.ProductDatum> listOrder;
    private AdapterWorkerOrderDetailsList mAdapter;
    String prod_id = "";
    private LinearLayout layout_dots;
    public AdapterImageSlider adapterImageSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Order Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent() != null) {
            prod_id = getIntent().getStringExtra("product_id");
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        layout_dots = (LinearLayout) findViewById(R.id.layout_dots);
        viewPager = (ViewPager) findViewById(R.id.pager);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if (!CheckNetwork.isConnected(this)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Tools.showCustomDialog(OrderDetailsActivity.this, "Alert !", "No Internet Connection , Please Try after Connecting");
                }
            });
        } else {
            if (!prod_id.isEmpty()) {
                GetOrderDetails();
            }
        }
    }

    public void GetOrderDetails() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(OrderDetailsActivity.this);
        Call<OrderDetailModel> orderdetails = apiInterface.WorkerOrderDetails(prod_id,
                SingletonSupport.getInstance().user_id);
        orderdetails.enqueue(new Callback<OrderDetailModel>() {
            @Override
            public void onResponse(Call<OrderDetailModel> call, Response<OrderDetailModel> response) {
                pdg.dismiss();
                try {
                    Log.d("GetOrderWorker", response.code() + "");
                    final OrderDetailModel resource = response.body();
                    if (resource!=null) {
                        listOrder = new ArrayList<>();
                        listOrder = resource.getProductData();
                        if (listOrder != null) {
                            setAdapter();
                        } else {
                            Tools.showCustomDialog(OrderDetailsActivity.this, "Alert !", "No data found");
                        }
                    }else{
                        Tools.showCustomDialog(OrderDetailsActivity.this, "Alert !", "No data found");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OrderDetailModel> call, Throwable t) {
                pdg.dismiss();
                Log.d("GetOrderWorker", t.toString() + "");
                call.cancel();
            }
        });
    }

    private void setAdapter() {
        //set data and list adapter
        mAdapter = new AdapterWorkerOrderDetailsList(OrderDetailsActivity.this, listOrder, animation_type);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterWorkerOrderDetailsList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, OrderDetailModel.ProductDatum obj, int position) {

            }
        });

        adapterImageSlider = new AdapterImageSlider(this, new ArrayList<String>(),"public/backend/product_images/zoom_image/");

        List<String> items = new ArrayList<>();
        for (int i = 0; listOrder.get(0).getImageName().size() > i; i++) {
            String obj;
            obj = listOrder.get(0).getImageName().get(i).getImageName();
            items.add(obj);
        }
        adapterImageSlider.setItems(items);
        viewPager.setAdapter(adapterImageSlider);

        addBottomDots(layout_dots, adapterImageSlider.getCount(), 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(layout_dots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(ContextCompat.getColor(this, R.color.overlay_dark_10), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
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
