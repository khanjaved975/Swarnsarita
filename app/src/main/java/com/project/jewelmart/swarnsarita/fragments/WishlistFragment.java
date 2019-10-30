package com.project.jewelmart.swarnsarita.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.project.jewelmart.swarnsarita.adapters.AdapterProductWishlist;
import com.project.jewelmart.swarnsarita.interfaces.APIInterface;
import com.project.jewelmart.swarnsarita.interfaces.OnLoadMoreListener;
import com.project.jewelmart.swarnsarita.networkutils.APIClient;
import com.project.jewelmart.swarnsarita.networkutils.CheckNetwork;
import com.project.jewelmart.swarnsarita.pojo.Acknowledge;
import com.project.jewelmart.swarnsarita.pojo.Cart;
import com.project.jewelmart.swarnsarita.utils.ProgressIndicator;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.utils.UserSessionManager;
import com.project.jewelmart.swarnsarita.widgets.SpacingItemDecoration;
import com.project.jewelmart.swarnsarita.R;

import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Carlos Vargas on 8/10/16.
 */

public class WishlistFragment extends Fragment implements OnLoadMoreListener {
    View view;
    APIInterface apiInterface;
    List<Cart.Datum> cartList;
    AdapterProductWishlist mAdapter;
    RecyclerView recyclerView;
    LinearLayout nodata;
    UserSessionManager userSessionManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_wishlist_layout, container, false);
        setHasOptionsMenu(true);
        userSessionManager = new UserSessionManager(getActivity());
        nodata = (LinearLayout) view.findViewById(R.id.nodata);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        if (Tools.isLandscape(getActivity())) {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL,
                    false));
            recyclerView.addItemDecoration(new SpacingItemDecoration(4, dpToPx(getActivity(), 2),
                    false));
            recyclerView.setHasFixedSize(true);
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new SpacingItemDecoration(2, dpToPx(getActivity(), 2), true));
            recyclerView.setHasFixedSize(true);
            OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        }
        apiInterface = APIClient.getClient().create(APIInterface.class);
        if (!CheckNetwork.isConnected(getActivity())) {
            Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
        } else {
            getWishlist(userSessionManager.getUserID(), "wishlist");
        }

        return view;
    }

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void getWishlist(String user_id, final String table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<Cart> countriesCall = apiInterface.getCart(user_id, table);
        countriesCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                pdg.dismiss();
                Log.d("getCart", response.code() + "");
                if (response.code() == 200) {
                    nodata.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Cart cdata = response.body();
                    final List<Cart.Datum> resource = cdata.getData();
                    cartList = resource;
                    mAdapter = new AdapterProductWishlist(getActivity(), cartList);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new AdapterProductWishlist.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, Cart.Datum obj, int position) {
                            Snackbar.make(view, "Item " + obj.getCollectionName() + " clicked", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                    mAdapter.setOnMoreButtonClickListener(new AdapterProductWishlist.OnMoreButtonClickListener() {
                        @Override
                        public void onItemClick(View view, Cart.Datum obj, MenuItem item, int position) {
                            if (item.getTitle().toString().toLowerCase().equals("delete")) {
                                if (!CheckNetwork.isConnected(getActivity())) {
                                    Tools.showCustomDialog(getActivity(), "Alert !",
                                            "No Internet Connection , Please Try after Connecting");
                                } else {
                                    mAdapter.Delete(userSessionManager.getUserID(), "wishlist",
                                            obj.getCartWishId(), position);
                                }
                            } else if (item.getTitle().toString().toLowerCase().contains("move")) {
                                if (!CheckNetwork.isConnected(getActivity())) {
                                    SingletonSupport.getInstance().cartCount=SingletonSupport.getInstance().cartCount+1;

                                    Tools.showCustomDialog(getActivity(), "Alert !",
                                            "No Internet Connection , Please Try after Connecting");
                                } else {
                                    mAdapter.Move(userSessionManager.getUserID(), "wishlist",
                                            "cart", obj.getCartWishId(), position);
                                }
                            } else {
                                Snackbar.make(view, obj.getCollectionName() + " (" + item.getTitle() + ") clicked",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                pdg.dismiss();
                Log.d("getCart", t.toString() + "");
                nodata.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                call.cancel();
            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // tilAddress = (TextInputLayout) view.findViewById(R.id.tilAddress);
        //tieAddress = (TextInputEditText) view.findViewById(R.id.tieAddress);
        //  tieAddress.addTextChangedListener(badgenew CustomTextWatcher(tieAddress));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.action_delete);
        int color = Color.parseColor(getResources().getString(R.string.color));
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).getIcon().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
        item.setVisible(true);
        menu.findItem(R.id.action_summary).setVisible(false);
        menu.findItem(R.id.action_move).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            if (!CheckNetwork.isConnected(getActivity())) {
                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                deleteCustomDialog();
            }
        } else if (item.getItemId() == R.id.action_move) {
            if (!CheckNetwork.isConnected(getActivity())) {
                Tools.showCustomDialog(getActivity(), "Alert !", "No Internet Connection , Please Try after Connecting");
            } else {
                MoveCustomDialog();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Alert !");
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText("Are you sure ? You want to delete all Wishlist Item , Click continue to proceed further ");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAll(userSessionManager.getUserID(), "wishlist");
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void MoveCustomDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Alert !");
        TextView content = (TextView) dialog.findViewById(R.id.content);
        content.setText("Are you sure ? You want to Move all Wishlist item to Cart  , Click continue to proceed further ");
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoveAll(userSessionManager.getUserID(), "wishlist", "cart");
                dialog.dismiss();
            }
        });

        ((ImageView) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void DeleteAll(String user_id, final String table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<Acknowledge> countriesCall = apiInterface.clearCartData(user_id, table);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("Delete Cart", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")) {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                    getWishlist(userSessionManager.getUserID(), "wishlist");
                } else {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d("Delete Cart", t.toString() + "");
                call.cancel();
            }
        });

    }

    public void MoveAll(String user_id, final String from_table, final String to_table) {
        final ProgressDialog pdg = ProgressIndicator.ShowLoading(getActivity());
        Call<Acknowledge> countriesCall = apiInterface.moveAllProduct(user_id, from_table, to_table);
        countriesCall.enqueue(new Callback<Acknowledge>() {
            @Override
            public void onResponse(Call<Acknowledge> call, Response<Acknowledge> response) {
                pdg.dismiss();
                Log.d("Move Cart", response.code() + "");
                final Acknowledge resource = response.body();
                if (resource.getAck().equals("1")) {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                    getWishlist("1", "wishlist");
                } else {
                    Toast.makeText(getActivity(), resource.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Acknowledge> call, Throwable t) {
                pdg.dismiss();
                Log.d("Move Cart", t.toString() + "");
                call.cancel();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //userSessionManager=badgenew UserSessionManager(getActivity());
        getWishlist(userSessionManager.getUserID(), "wishlist");

    }


    @Override
    public void onLoadMore() {
        getWishlist(userSessionManager.getUserID(), "wishlist");
    }


}
