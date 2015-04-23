package com.zxc.biggis;

import com.zxc.biggis.view.SlidingMenu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;


public class MainActivity extends ActionBarActivity {
	private SlidingMenu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        menu=(SlidingMenu) findViewById(R.id.id_menu);
    }
    public void toggleMenu(View v){
    	menu.toggleMenu();
    }
}
