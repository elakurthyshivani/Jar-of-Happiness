package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.Objects;

public class SearchActivity extends AppCompatActivity {
    private int userID;
    private FlexboxLayout keywordsContainer;
    private LinearLayout newKeywordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userID=Objects.requireNonNull(getIntent().getExtras()).getInt("userID");

        keywordsContainer=findViewById(R.id.keywords_container);
        newKeywordView=findViewById(R.id.new_keyword_view);

        enterPressed();
    }

    private void enterPressed() {
        findViewById(R.id.keyword_input).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER)
                    addKeyword(view);
                return true;
            }
        });
    }

    public void addKeyword(View v)  {
        EditText keywordInput=findViewById(R.id.keyword_input);
        String keyword=keywordInput.getText().toString().trim();
        if(keyword.equals(""))
            return;
        addKeyword(keyword);
        keywordInput.setText("");
        if(keywordsContainer.getChildCount()==31) // NewKeywordView is also child of the container.
            newKeywordView.setVisibility(View.GONE);
    }

    private void addKeyword(String keyword) {
        LinearLayout keywordView=(LinearLayout)getLayoutInflater().inflate(R.layout.item_keyword,
                keywordsContainer, false);
        ((TextView)keywordView.findViewById(R.id.keyword_view)).setText(keyword);
        keywordsContainer.addView(keywordView, keywordsContainer.getChildCount()-1);
    }

    public void removeKeyword(View v)   {
        keywordsContainer.removeView((LinearLayout)v.getParent());
        if(keywordsContainer.getChildCount()<=31 && newKeywordView.getVisibility()==View.GONE)
            newKeywordView.setVisibility(View.VISIBLE);
    }

    public void openFilters(View v) {
        Intent i=new Intent(this, FilterActivity.class);
        i.putExtra("userID", userID);
        i.putExtra("filtersFor", FilterActivity.SEARCH);
        startActivityForResult(i, 1);
    }

    public void gotoMenu(View v)    {
        startActivity(new Intent(this, MenuActivity.class));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK) {
            /*int searchByOption=data.getIntExtra("searchByOption", ViewByFragment.MEMORY);
            switch(searchByOption) {
                default: case SearchByFragment.MEMORY:
                    //data.getStringArrayListExtra("lockOptions");
                    //data.getStringArrayListExtra("searchInOptions");
                    break;
                case SearchByFragment.WHEN:
                    break;
                case SearchByFragment.LOCATION:
                    break;
                case SearchByFragment.MOOD:
                    break;
                case SearchByFragment.TAG:
                    break;
            }*/
        }
    }

}