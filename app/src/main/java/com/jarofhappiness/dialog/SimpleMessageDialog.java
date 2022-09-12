package com.jarofhappiness.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jarofhappiness.R;

import java.util.Objects;

public class SimpleMessageDialog extends Dialog implements android.view.View.OnClickListener {
    private Result result;
    private int stringResource;

    public SimpleMessageDialog(Activity activity, int stringResource)  {
        super(activity);

        this.stringResource=stringResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_simple_message);
        Objects.requireNonNull(getWindow()).setBackgroundDrawableResource(R.color.transparent);

        findViewById(R.id.dialog_no).setOnClickListener(this);
        findViewById(R.id.dialog_yes).setOnClickListener(this);
        findViewById(R.id.dialog_close).setOnClickListener(this);
        ((TextView)findViewById(R.id.simple_message)).setText(stringResource);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dialog_yes)   {
            result.getResult();
        }
        dismiss();
    }

    public void setResult(Result result)    {
        this.result=result;
    }

    public interface Result {
        void getResult();
    }
}
