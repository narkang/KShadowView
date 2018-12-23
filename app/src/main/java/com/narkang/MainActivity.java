package com.narkang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.narkang.list.ListActivity;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener,
        View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_show_v1:
                startActivity(new Intent(MainActivity.this, ShowV1Activity.class));
                break;
            case R.id.tv_to_show_v2:
                startActivity(new Intent(MainActivity.this, ShowV2Activity.class));
                break;
            case R.id.tv_to_dynamic_change:
                startActivity(new Intent(MainActivity.this, DynamicChangeActivity.class));
                break;
            case R.id.tv_to_list:
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_about:
                break;
        }
        return true;
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        findViewById(R.id.tv_to_show_v1).setOnClickListener(this);
        findViewById(R.id.tv_to_show_v2).setOnClickListener(this);
        findViewById(R.id.tv_to_dynamic_change).setOnClickListener(this);
        findViewById(R.id.tv_to_list).setOnClickListener(this);
    }
}
