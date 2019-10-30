package com.project.jewelmart.swarnsarita;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.project.jewelmart.swarnsarita.adapters.AdapterSubCategoriesList;
import com.project.jewelmart.swarnsarita.fragments.SubcatFragment2;
import com.project.jewelmart.swarnsarita.pojo.Collection;
import com.project.jewelmart.swarnsarita.utils.ItemAnimation;
import com.project.jewelmart.swarnsarita.utils.Tools;
import com.project.jewelmart.swarnsarita.widgets.FontBoldTextView;

import java.io.Serializable;
import java.util.List;

public class SubCatagoryActivity extends AppCompatActivity {
    List<Collection.Subcategory> catlist;
    List<Collection.Subcategory> subCatList;
    String title,table="product_master";
    RecyclerView recyclerView;
    private int animation_type = ItemAnimation.BOTTOM_UP;
    FragmentManager frgManager;
    FragmentTransaction frgTransaction;
    private SubcatFragment2 fragment1;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catagory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FontBoldTextView textView = (FontBoldTextView) findViewById(R.id.toolbar_title);
        Bundle getBundle;
        if (getIntent() != null) {
            getBundle = this.getIntent().getExtras();
            catlist = (List<Collection.Subcategory>) getBundle.getSerializable("subcat");
            title = getBundle.getString("selectedvalue", "Categories");
            table = getBundle.getString("table", "product_master");

        }

        if (Tools.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }
        textView.setText(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.rv_cat);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setNestedScrollingEnabled(false);
        AdapterSubCategoriesList adapterSubCategoriesList = new AdapterSubCategoriesList(this, catlist, animation_type);
        recyclerView.setAdapter(adapterSubCategoriesList);
        adapterSubCategoriesList.setOnItemClickListener(new AdapterSubCategoriesList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Collection.Subcategory obj, int position) {
                selectItem(position);
            }
        });

    }

    public void selectItem(int position) {
        String catId = "", selected = "";
        fragment = null;
        //—args = badgenew Bundle();
        Log.d("In MainActivity", "Position :: " + position);
        String category_names[] = null;
        String[] image_type = null;
        if (position < catlist.size()) {
            selected = catlist.get(position).getColName();
            subCatList = catlist.get(position).getSubcategory();
            catId = catlist.get(position).getId();
            //catProductCount = "" + parentlist.get(position).count;
            //imagetype = "" + catMainList.get(position).image_type;
            // sortPosition = "" + catMainList.get(position).default_sort;
            if (subCatList != null && !subCatList.isEmpty()) {
                addFragWithArray(subCatList, selected);
            } else {
                Intent intent = new Intent(SubCatagoryActivity.this, ProductGridActivity.class);
                intent.putExtra("collection_id", catId);
                intent.putExtra("name", selected);
                intent.putExtra("mode_type", "normal");
                intent.putExtra("table", table);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
           // invalidateOptionsMenu();
        }
    }

    private void addFragWithArray(List<com.project.jewelmart.swarnsarita.pojo.Collection.Subcategory> subCatList,
                                  String selected) {
        frgManager = getSupportFragmentManager();
        fragment1 = new SubcatFragment2();
        frgTransaction = frgManager.beginTransaction();
        //frgManager.popBackStack();
        Bundle bundle = new Bundle();
        bundle.putSerializable("subcat", (Serializable) subCatList);
        bundle.putString("selectedCatName", selected);
        bundle.putString("table", table);
        fragment1.setArguments(bundle);
        frgTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                R.anim.slide_in_left, R.anim.slide_out_right);
        frgTransaction.replace(R.id.mainListFrame, fragment1);
        frgTransaction.addToBackStack("subcat1");
        frgTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();
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
