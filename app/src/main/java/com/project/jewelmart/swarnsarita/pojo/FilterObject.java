package com.project.jewelmart.swarnsarita.pojo;

import android.widget.EditText;


import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;

/**
 * Created by t3d on 04/07/16.
 */
public class FilterObject {
    public String min;
    public String changedMin;
    public String max;
    public String type;
    public ArrayList<String> id = new ArrayList<String>();
    public ArrayList<Boolean> checked = new ArrayList<Boolean>();
    public ArrayList<String> value = new ArrayList<String>();
    public ArrayList<String> valueSelected = new ArrayList<String>();
    public String filterOptionName;
    public String changedMax;
    public ArrayList<String> changedId = new ArrayList<String>();
    public ArrayList<EditText> etFromList;
    public ArrayList<EditText> etToList;
    public String key;
    public String mat_type_id;
    public String mat_calc_type;
    public ArrayList<CrystalRangeSeekbar> rangeList = new ArrayList<CrystalRangeSeekbar>();
    public String selectedMin;
    public String selectedMax;
    public boolean changed;
}
