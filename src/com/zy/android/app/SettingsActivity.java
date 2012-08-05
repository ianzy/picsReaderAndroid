package com.zy.android.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.zy.android.R;

public class SettingsActivity extends FragmentActivity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        SharedPreferences settings = getSharedPreferences(EpicPicReaderActivity.PREFS_NAME, 0);
        if (settings.getString("9gag", null) != null) {
        	((CheckBox) findViewById(R.id.ninegag)).setChecked(true);
        }
        if (settings.getString("imgur", null) != null) {
        	((CheckBox) findViewById(R.id.imgur)).setChecked(true);
        }
        if (settings.getString("jandan", null) != null) {
        	((CheckBox) findViewById(R.id.jandan)).setChecked(true);
        }
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
            	this.setResult(RESULT_CANCELED);
            	this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	public void onCheckBoxClicked(View v) {
		// do nothing for now
	}
	
	public void onSaveClicked(View v) {
		SharedPreferences settings = getSharedPreferences(EpicPicReaderActivity.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		if ( ((CheckBox) findViewById(R.id.ninegag)).isChecked() ) {
			editor.putString("9gag", "http://tumblr.9gag.com/rss");
		} else {
			editor.remove("9gag");
		}
		if ( ((CheckBox) findViewById(R.id.imgur)).isChecked() ) {
			editor.putString("imgur", "http://feeds.feedburner.com/ImgurGallery?format=xml");
		} else {
			editor.remove("imgur");
		}
		if ( ((CheckBox) findViewById(R.id.jandan)).isChecked() ) {
			editor.putString("jandan", "http://jandan.net/pic/feed");
		} else {
			editor.remove("jandan");
		}
		
		editor.commit();
		this.setResult(RESULT_OK);
		this.finish();
	}
	
	public void onCancelClicked(View v) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}
}
