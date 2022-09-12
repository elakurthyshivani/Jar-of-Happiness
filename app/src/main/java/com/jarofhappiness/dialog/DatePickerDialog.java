package com.jarofhappiness.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;

import com.jarofhappiness.R;

import java.util.Locale;

public class DatePickerDialog extends Dialog implements android.view.View.OnClickListener {
    private DateResult dateResult;
    public DatePickerDialog(Activity activity)  {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_picker);
        findViewById(R.id.dialog_back).setOnClickListener(this);
        findViewById(R.id.dialog_select).setOnClickListener(this);
        findViewById(R.id.dialog_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dialog_select)   {
            DatePicker picker=findViewById(R.id.date_picker);
            dateResult.getSelectedDate(String.format(Locale.ENGLISH,
                    "%02d-%02d-%4d", picker.getDayOfMonth(), picker.getMonth()+1,
                    picker.getYear()));
        }
        dismiss();
    }

    public void setDateResult(DateResult dateResult)    {
        this.dateResult=dateResult;
    }

    public interface DateResult {
        void getSelectedDate(String date);
    }

}
