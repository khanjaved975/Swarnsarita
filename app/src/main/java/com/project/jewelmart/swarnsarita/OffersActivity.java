package com.project.jewelmart.swarnsarita;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.project.jewelmart.swarnsarita.adapters.AdapterOffersList;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.OfferModel;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;
import com.project.jewelmart.swarnsarita.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersActivity extends AppCompatActivity {
    APIInterface apiInterface;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        textView.setText("Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        if (CheckNetwork.isConnected(this)) {
            getOffers();
        } else {
            Tools.showCustomDialog(this, "Alert !", "No Internet Connection , Please Try after Connecting");
        }
    }


    public void getOffers() {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(OffersActivity.this);
        Call<OfferModel> countriesCall = apiInterface.GetOffers();
        countriesCall.enqueue(new Callback<OfferModel>() {
            @Override
            public void onResponse(Call<OfferModel> call, Response<OfferModel> response) {
                pdg.dismiss();
                try {
                    Log.d("CustomizedOrder", response.code() + "");
                    final OfferModel resource = response.body();
                    if (resource != null) {
                        if (resource.getAck().toString().equalsIgnoreCase("1")) {
                            List<OfferModel.GiftCoupan> listOrder;
                            if (!resource.getData().getGiftCoupans().isEmpty()) {
                                listOrder = resource.getData().getGiftCoupans();
                                setAdapter(listOrder, resource.getData().getPath());
                            } else {
                                Tools.showCustomDialog(OffersActivity.this, "Alert !", "No data found");
                            }
                        } else {
                            Toast.makeText(OffersActivity.this, resource.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OfferModel> call, Throwable t) {
                pdg.dismiss();
                Log.d("CustomizedOrder", t.toString() + "");
                call.cancel();
            }
        });
    }

    public void setAdapter(List<OfferModel.GiftCoupan> listOrder, String path) {

        if (listOrder.size() > 0) {
            AdapterOffersList mAdapter;
            mAdapter = new AdapterOffersList(OffersActivity.this, listOrder, path, ItemAnimation.BOTTOM_UP);
            recyclerView.setAdapter(mAdapter);
        } else {
            Tools.showCustomDialog(OffersActivity.this, "Alert !", "No data found");
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
