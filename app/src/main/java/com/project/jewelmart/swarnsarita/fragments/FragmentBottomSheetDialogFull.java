package com.project.jewelmart.swarnsarita.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.project.jewelmart.swarnsarita.pojo.FilterObject;
import com.project.jewelmart.swarnsarita.utils.SingletonSupport;
import com.project.jewelmart.swarnsarita.widgets.FontTextView;
import com.project.jewelmart.swarnsarita.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class FragmentBottomSheetDialogFull extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;
    private AppBarLayout app_bar_layout;
    RecyclerView recyclview;
    FilterViewAdapter adapter;
    private ImageButton btnApplyFilter;
    public static HashMap<String, String> filterSendMap;
    private LinearLayout filter_range_view, filter_multiselect_view;
    private CrystalRangeSeekbar rangeFilter;
    private int checkPosition = -1;
    public static ArrayList<FilterObject> filterlist;
    private EditText etFrom, etTo;
    private FontTextView tvMin, tvMax;
    FontTextView tv_header;


    public void setPeople(ArrayList<FilterObject> people) {
        this.filterlist = people;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.fragment_bottom_sheet_dialog_full, null);

        dialog.setContentView(view);
        init(view);
        setData();
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);
        app_bar_layout = (AppBarLayout) view.findViewById(R.id.app_bar_layout);


        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    showView(app_bar_layout, getActionBarSize());
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    hideView(app_bar_layout);
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        ((ImageButton) view.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }


    public void init(View view) {
        recyclview = (RecyclerView) view.findViewById(R.id.filt_cat);
        filter_multiselect_view = (LinearLayout) view.findViewById(R.id.filter_multiselect_view);
        filter_range_view = (LinearLayout) view.findViewById(R.id.filter_range_view);
        filter_multiselect_view.setVisibility(View.GONE);
        filter_range_view.setVisibility(View.GONE);
        tv_header = (FontTextView) view.findViewById(R.id.tv_header);
        rangeFilter = (CrystalRangeSeekbar) view.findViewById(R.id.rangeFilter);
// get min and max text view
        tvMin = (FontTextView) view.findViewById(R.id.tvMin);
        tvMax = (FontTextView) view.findViewById(R.id.tvMax);
        etFrom = (EditText) view.findViewById(R.id.etFrom);
        etTo = (EditText) view.findViewById(R.id.etTo);
        btnApplyFilter = (ImageButton) view.findViewById(R.id.applyfilter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclview.setLayoutManager(llm);
        btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSendMap = new HashMap<String, String>();
                if (filterlist != null)
                    SingletonSupport.getInstance().isfilter = true;
                for (int i = 0; i < filterlist.size(); i++) {
                    FilterObject filter = (FilterObject) filterlist.get(i);
                    if (filter != null) {
                        String key = "";
                        String value = "";

                        if (filter.type.equals("range") && (filter.changedMin != null && !filter.changedMax.isEmpty())) {
                            Log.wtf("Filter Click", "RangeBar = " + filter.changedMin);
                            if (filter.mat_type_id != null && !filter.mat_type_id.isEmpty()) {
                                key = filter.key;
                                key = "min_" + filter.mat_type_id;
                                filterSendMap.put(key, filter.changedMin);
                                key = filter.key;
                                key = "max_" + filter.mat_type_id;
                                filterSendMap.put(key, filter.changedMax);
                            } else {
                                //key = filter.key;
                                key = "min_" + filter.key;
                                filterSendMap.put(key, filter.changedMin);
                                // key = filter.key;
                                key = "max_" + filter.key;
                                filterSendMap.put(key, filter.changedMax);
                            }
                        }/* else {
                                Log.wtf("Filter Click", "CheckBox ids = " + filter.changedId);
                                if (filter.changedId != null && !filter.changedId.isEmpty()) {
                                    key = filter.key;
                                    String changeIds = "";
                                    for (int j = 0; j < filter.changedId.size(); j++) {
                                        changeIds = changeIds + filter.changedId.get(j) + ",";
                                    }
                                    String changedIdString = changeIds.substring(0, changeIds.length() - 1);
                                    filterSendMap.put(key, changedIdString);
                                }
                            }*/
                    }
                }
                getActivity().finish();
                getActivity().overridePendingTransition(0, 0);
                startActivity(getActivity().getIntent());
                getActivity().overridePendingTransition(0, 0);

            }
        });
    }


    private void setData() {
        if (filterlist != null && !filterlist.isEmpty()) {
            for (int i = 0; i < filterlist.size(); i++) {
                if (filterlist.get(i).type.equals("range") &&
                        (filterlist.get(i).selectedMin != null && filterlist.get(i).selectedMax != null))
                    if ((!filterlist.get(i).selectedMin.equals(filterlist.get(i).min))
                            || (!filterlist.get(i).selectedMax.equals(filterlist.get(i).max))) {
                        filterlist.get(i).changed = true;
                    } else {
                        filterlist.get(i).changed = false;
                    }
                else {
                    if (filterlist.get(i).changedId != null &&
                            filterlist.get(i).changedId.size() > 0) {
                        filterlist.get(i).changed = true;
                    } else {
                        filterlist.get(i).changed = false;
                    }
                }
            }
            adapter = new FilterViewAdapter(getActivity(), filterlist);
            recyclview.setAdapter(adapter);
            showFilterLayoutAtPosition(0);
            adapter.SetOnItemClickListener(new FilterViewAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View v, int position) {
                    // do something with position
                    showFilterLayoutAtPosition(position);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Filter not available", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    static class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {

        Context context;
        OnItemClickListener mItemClickListener;
        private int focusedItem = 0;
        ArrayList<FilterObject> name;
        private View itemView;

        public FilterViewAdapter(Context context, ArrayList<FilterObject> name) {
            this.context = context;
            this.name = name;
        }

        @Override
        public int getItemCount() {
            return name.size();
        }


        @Override
        public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            recyclerView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                    // Return false if scrolled to the bounds and allow focus to move off the list
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            return tryMoveSelection(lm, 1);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            return tryMoveSelection(lm, -1);
                        }
                    }

                    return false;
                }
            });
        }

        private boolean tryMoveSelection(RecyclerView.LayoutManager lm, int direction) {
            int tryFocusItem = focusedItem + direction;

            // If still within valid bounds, move the selection, notify to redraw, and scroll
            if (tryFocusItem >= 0 && tryFocusItem < getItemCount()) {
                notifyItemChanged(focusedItem);
                focusedItem = tryFocusItem;
                notifyItemChanged(focusedItem);
                lm.scrollToPosition(focusedItem);
                return true;
            }

            return false;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.filter_cat_item, viewGroup, false);
//
//            if (i == checkPosition) {
//                highlightCurrentRow(itemView);
////            notifyDataSetChanged();
//            } else {
//                unhighlightCurrentRow(itemView);
////            notifyDataSetChanged();
//            }

            return new ViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            FilterObject filter = (FilterObject) name.get(position);
            if (filter != null) {
//              holder.filt_img.setImageResource(images[position]);
                String name = filter.filterOptionName;
                holder.filt_text.setText(name);
                holder.itemView.setSelected(focusedItem == position);
                if (filter.changed == true) {
                    holder.filt_check.setVisibility(View.VISIBLE);
                    holder.selection.setVisibility(View.VISIBLE);
                } else {
                    holder.filt_check.setVisibility(View.INVISIBLE);
                    holder.selection.setVisibility(View.INVISIBLE);
                }
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            //            protected ImageView filt_img;
            protected FontTextView filt_text;
            protected ImageView filt_check;
            protected RelativeLayout filter;
            protected View selection;


            public ViewHolder(View itemView) {
                super(itemView);
//                filt_img = (ImageView) itemView.findViewById(R.id.filt_img);
                filt_text = (FontTextView) itemView.findViewById(R.id.filt_text);
                filt_check = (ImageView) itemView.findViewById(R.id.filt_check);
                filter = (RelativeLayout) itemView.findViewById(R.id.filter);
                selection = (View) itemView.findViewById(R.id.selection);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyItemChanged(focusedItem);
                        focusedItem = getLayoutPosition();
                        notifyDataSetChanged();
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(v, getPosition());
                        }
                    }
                });
            }
        }


        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

    }

    private void showFilterLayoutAtPosition(final int position) {
        final FilterObject filter = filterlist.get(position);
        if (filter != null) {
            filter.rangeList = new ArrayList<CrystalRangeSeekbar>();
            filter.etFromList = new ArrayList<EditText>();
            filter.etToList = new ArrayList<EditText>();
            tv_header.setText("" + filter.filterOptionName);
            if (filter.type.equals("range")) {
                filter_range_view.setVisibility(View.VISIBLE);
                filter_multiselect_view.setVisibility(View.GONE);
                etFrom.setTag("" + position);
                etTo.setTag("" + position);
                rangeFilter.invalidate();
                rangeFilter.setTag("" + position);
                rangeFilter.setBarColor(ContextCompat.getColor(getActivity(), R.color.grey_60));
            /*    Drawable thumbR = getResources().getDrawable(R.drawable.dot_rangebar);
                rangeFilter.setLeftThumbDrawable(thumbR);
                rangeFilter.setRightThumbDrawable(thumbR);
                rangeFilter.setLeftThumbColor(buttonPrimary);
                rangeFilter.setRightThumbColor(buttonPrimary);
                rangeFilter.setLeftThumbHighlightColor(buttonPrimary);
                rangeFilter.setRightThumbHighlightColor(buttonPrimary);
                rangeFilter.setBarHighlightColor(buttonPrimary);
                rangeFilter.setCornerRadius(5.0f);*/
                filter.rangeList.add(rangeFilter);
                filter.etFromList.add(etFrom);
                filter.etToList.add(etTo);
                Log.wtf("filter.max", "filter.max: " + filter.max);

                if (filter.max != null && !filter.max.isEmpty()) {
                    rangeFilter.setMaxValue(Float.parseFloat(filter.max));
                    etTo.setText(filter.max);
                    tvMax.setText(filter.max);
                }

                Log.wtf("filter.min", "filter.min: " + filter.min);
                if (filter.min != null && !filter.min.isEmpty()) {
                    rangeFilter.setMinValue(Float.parseFloat(filter.min));
                    etFrom.setText(filter.min);
                    tvMin.setText(filter.min);
                }

                Log.wtf("SetData", "selectedMin: " + filter.selectedMin);
                if (filter.changedMin != null && filter.changedMax != null) {
                    Log.wtf("Filter", "selectedMin: " + filter.selectedMin);
                    Log.d("Change Values", "Min filterParams.get(" + position + ")= " + filter.changedMin +
                            " Max filterParams.get(" + position + ")= " + filter.changedMax);
                    if (Float.parseFloat(filter.changedMin) > Float.parseFloat(filter.min)) {
                        rangeFilter.setMinStartValue(Float.parseFloat(filter.changedMin)).
                                setMaxStartValue(Float.parseFloat(filter.changedMax)).apply();
                    } else {
                        rangeFilter.setMinStartValue(Float.parseFloat(filter.min)).
                                setMaxStartValue(Float.parseFloat(filter.max)).apply();
                    }
                } else if (filter.selectedMin != null) {
                    etFrom.setText(filter.selectedMin);
                    etTo.setText(filter.selectedMax);
                    tvMin.setText(filter.selectedMin);
                    tvMax.setText(filter.selectedMax);
                    filter.changedMin = filter.selectedMin;
                    filter.changedMax = filter.selectedMax;
                    View v = (View) recyclview.getChildAt(position);
                    if (v != null) {
                        ImageView check = (ImageView) v.findViewById(R.id.filt_check);
                        if ((!filter.changedMin.equals(filter.min)) || (!filter.changedMax.equals(filter.max))) {
                            check.setVisibility(View.VISIBLE);
                        } else {
                            check.setVisibility(View.GONE);
                        }
                    }
                    if (filter.selectedMin != null || filter.selectedMax != null) {
                        rangeFilter.setMinStartValue(Float.parseFloat(filter.selectedMin)).setMaxStartValue(Float.parseFloat(filter.selectedMax)).apply();
                    }
                } else {
                    if (filter.min != null || filter.max != null) {
                        rangeFilter.setMinStartValue(Float.parseFloat(filter.min)).setMaxStartValue(Float.parseFloat(filter.max)).apply();
                    }
                }

                etFrom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if (!etFrom.getText().toString().isEmpty()) {
                                float from = Float.parseFloat(etFrom.getText().toString());
                                if (from >= Float.parseFloat(filter.min) && from <= Float.parseFloat(filter.max)) {
                                    rangeFilter.setMinStartValue(from).setMaxStartValue(Float.parseFloat("" + rangeFilter.getSelectedMaxValue())).apply();
                                } else {
                                    etFrom.setError("Please enter value in between given range");
                                }
                            } else {
                                rangeFilter.setMinStartValue(Float.parseFloat(filter.min)).setMaxStartValue(Float.parseFloat("" + rangeFilter.getSelectedMaxValue())).apply();
                            }
                        }
                    }
                });

                etTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            if (!etTo.getText().toString().isEmpty()) {
                                float to = Float.parseFloat(etTo.getText().toString());
                                if (to >= Float.parseFloat(filter.min) && to <= Float.parseFloat(filter.max)) {
                                    rangeFilter.setMinStartValue(Float.parseFloat("" + rangeFilter.getSelectedMinValue())).setMaxStartValue(to).apply();
                                } else {
                                    etTo.setError("Please enter value in between given range");
                                }
                            } else {
                                rangeFilter.setMinStartValue(Float.parseFloat("" + rangeFilter.getSelectedMinValue())).setMaxStartValue(Float.parseFloat(filter.max)).apply();
                            }
                        }
                    }
                });

                etFrom.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            etTo.requestFocus();
                        }
                        return false;
                    }
                });

                etTo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            etFrom.requestFocus();
                        }
                        return false;
                    }
                });

                rangeFilter.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
                    @Override
                    public void valueChanged(Number leftPinValue, Number rightPinValue) {
                        int pos = Integer.parseInt(rangeFilter.getTag().toString());
                        Log.wtf("!!!leftpin", "" + leftPinValue);
                        Log.wtf("!!!rightpin", "" + rightPinValue);
                        String leftPinValuef = "" + new DecimalFormat("#0.000").format(Float.parseFloat("" + leftPinValue));
                        String rightPinValuef = "" + new DecimalFormat("#0.000").format(Float.parseFloat("" + rightPinValue));
                        Log.d("Pin Values", "left = " + leftPinValue + " right = " + rightPinValue);
                        if (filterlist != null)
                            if (filterlist.get(pos) != null) {
                                EditText from = (EditText) filterlist.get(pos).etFromList.get(0);
                                EditText to = (EditText) filterlist.get(pos).etToList.get(0);
                                etFrom.setText("" + leftPinValuef);
                                etTo.setText("" + rightPinValuef);
                                tvMin.setText("" + leftPinValuef);
                                tvMax.setText("" + rightPinValuef);
                            }
//                        to.setFocusable(false);
//                        from.setFocusable(false);
                        filterlist.get(pos).changedMin = leftPinValuef;
                        filterlist.get(pos).changedMax = rightPinValuef;

                        Log.d("Changed Values", "Min filterParams.get(" + pos + ")= " + filterlist.get(pos).changedMin +
                                " Max filterParams.get(" + pos + ")= " + filterlist.get(pos).changedMax);

                        if ((!filterlist.get(pos).changedMin.equals(filterlist.get(pos).min)) ||
                                (!filterlist.get(pos).changedMax.equals(filterlist.get(pos).max))) {
                            filterlist.get(pos).changed = true;
                        } else {
                            filterlist.get(pos).changed = false;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray styledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        return size;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!filterlist.isEmpty()) {
            filterlist.clear();
        }
    }
}
