package com.zy.android.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.Menu;
import android.support.v4.view.MenuItem;
import android.support.v4.view.MenuItem.OnMenuItemClickListener;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.viewpagerindicator.TitlePageIndicator;
import com.zy.android.R;
import com.zy.android.dal.impl.ItemsDAO;
import com.zy.android.dos.Item;

public class EpicPicReaderActivity extends FragmentActivity {
	
	private PicsPagerAdapter adapter;
	private ViewPager pager;
	private ProgressDialog dialog;
	private List<String> urls = new ArrayList<String>();
	static final int SETTINGS = 1234;
//	private AdView adView;
	
	public static final String PREFS_NAME = "UrlsPrefsFile";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        adapter = new PicsPagerAdapter(getSupportFragmentManager(), this);
//
//        pager = (ViewPager)findViewById(R.id.pager);
//        pager.setAdapter(adapter);
        
//        TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
//        titleIndicator.setViewPager(pager);
        
        // init preference
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String initFlag = settings.getString("initFlag", null);
        if (initFlag != null) {
        	Map<String, ?> prefs = settings.getAll();
        	for (Object s : prefs.values()) {
        		if (!s.toString().equals("flag")) {
        			urls.add(s.toString());
        		}
        	}
        } else {
        	SharedPreferences.Editor editor = settings.edit();
        	// init default urls
        	editor.putString("jandan", "http://jandan.net/pic/feed");
        	editor.putString("9gag", "http://tumblr.9gag.com/rss");
        	editor.putString("imgur", "http://feeds.feedburner.com/ImgurGallery?format=xml");
            editor.putString("initFlag", "flag");
            editor.commit();
            
            urls.add("http://feeds.feedburner.com/ImgurGallery?format=xml");
            urls.add("http://tumblr.9gag.com/rss");
            urls.add("http://jandan.net/pic/feed");
        }

        
        dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
        new RefreshPicsTask().execute();
        
        // Look up the AdView as a resource and load a request.
        AdView adView = (AdView)this.findViewById(R.id.adView);
        adView.loadAd(new AdRequest());
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("Refresh")
		.setIcon(R.drawable.ic_action_refresh)
		.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				dialog = ProgressDialog.show(EpicPicReaderActivity.this, "", 
		                "Loading. Please wait...", true);
				new RefreshPicsTask().execute();
				return true;
			}
		})
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    	
    	menu.add("Setting")
		.setIcon(R.drawable.ic_action_setting)
		.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent();
				intent.setClass(EpicPicReaderActivity.this, SettingsActivity.class);
				
				startActivityForResult(intent, SETTINGS);
				return true;
			}
			
		})
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//            	this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == SETTINGS) {
            if (resultCode == RESULT_OK) {
            	urls = new ArrayList<String>();
            	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            	Map<String, ?> prefs = settings.getAll();
            	for (Object s : prefs.values()) {
            		if (!s.toString().equals("flag")) {
            			urls.add(s.toString());
            		}
            	}
            }
        }
    }
    
    public void onShareClicked(View v) {
    	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    	sharingIntent.setType("text/plain");
    	
    	int pos = pager.getCurrentItem();
    	Item item = adapter.getItemByPos(pos);
    	sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, item.getTitle());
    	sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, item.getImageUrl());
    	startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    
    public void onPicClicked(View v) {
    	int pos = pager.getCurrentItem();
    	Item item = adapter.getItemByPos(pos);
    	
    	Intent intent = new Intent();
		intent.setClass(this, PicWebViewActivity.class);
		intent.putExtra("url", item.getImageUrl());
		startActivity(intent);
    }
    
	private class RefreshPicsTask extends AsyncTask<Void, Integer, List<Item>> {
        protected List<Item> doInBackground(Void... params) {
//        	String url = getResources().getString(R.string.api_messages);
//        	String url = "http://jandan.net/pic/feed";
//        	String url = "http://tumblr.9gag.com/rss";
//        	String url = "http://www.reddit.com/r/pics/.rss";
//        	String url = "http://www.reddit.com/r/ragecomics/.rss";
        	
        	List<Item> items = new ArrayList<Item>(); 
        	try {
        		for (String url : urls) {
        			items.addAll(ItemsDAO.getInstance().getNewItems(url));
        		}
			} catch (Exception e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
        	
        	return items;
        }

        protected void onPostExecute(List<Item> items) {
        	if (items == null || items.isEmpty()) {
        		dialog.dismiss();
        		Toast.makeText(EpicPicReaderActivity.this, "No content, please refresh later", Toast.LENGTH_LONG).show();
				if(!this.isOnline()) {
	    	    	createNetworkDisabledAlert();
	    	    } 
        		return;
        	}
        	
        	adapter = new PicsPagerAdapter(getSupportFragmentManager(), EpicPicReaderActivity.this);

            pager = (ViewPager)findViewById(R.id.pager);
            pager.setAdapter(adapter);
            
        	for(Item item : items) {
        		adapter.addItem(item);
        	}
        	
        	adapter.notifyDataSetChanged();
        	TitlePageIndicator titleIndicator = (TitlePageIndicator)findViewById(R.id.titles);
            titleIndicator.setViewPager(pager);
            dialog.dismiss();
        	super.onPostExecute(items);
        }
        
        private boolean isOnline() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
            return false;
        }
        
        private void createNetworkDisabledAlert() {
        	AlertDialog.Builder builder = new AlertDialog.Builder(EpicPicReaderActivity.this);  
        	builder.setMessage("Your Network is disabled! Would you like to enable it?")  
        	     .setCancelable(false)  
        	     .setPositiveButton("Enable network",  
        	          new DialogInterface.OnClickListener(){  
        	          public void onClick(DialogInterface dialog, int id){  
        	        	  showNetworkOptions();  
        	          }  
        	     });  
        	     builder.setNegativeButton("Do nothing",  
        	          new DialogInterface.OnClickListener(){  
        	          public void onClick(DialogInterface dialog, int id){  
        	               dialog.cancel();  
        	          }  
        	     });  
        	AlertDialog alert = builder.create();  
        	alert.show();  
        }
        
        private void showNetworkOptions(){  
    	    Intent networkOptionsIntent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);  
    	    startActivity(networkOptionsIntent);  
    	}  
    }
}