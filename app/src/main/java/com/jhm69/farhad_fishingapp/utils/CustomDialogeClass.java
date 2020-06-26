package com.jhm69.farhad_fishingapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.jhm69.farhad_fishingapp.R;

public class CustomDialogeClass extends Dialog{

    public Activity c;
    public Dialog d;

    public CustomDialogeClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE);
        setContentView( R.layout.progress_bar);

    }
}