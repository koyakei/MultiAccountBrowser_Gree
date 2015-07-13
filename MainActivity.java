package com.koyakei.MUB;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static ArrayAdapter<String> adapter;
	private static ArrayList<Tasks> tasks;
	private static ListView listView;
	private ArrayList<WebViewClient> webClientList = new ArrayList();
	private int selected = 0;

	private WebView  webView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		webClientList.add(new WebViewClient());
		showUsersList();
		//showWebView(null);
	}

	private void showUsersList() {
		setContentView(R.layout.activity_main);
		DatabaseHelper helper = new DatabaseHelper(this);
		SQLiteDatabase db = null;
		Cursor c = null;
		try {
			db = helper.getReadableDatabase();
			c = db.query("emp", new String[] { "name", "url", "cookie" }, null, null, null, null, null);

			listView = (ListView)findViewById(R.id.listView1);

			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
			tasks = new ArrayList<Tasks>();

			//adapterにDBから文字列を追加
			boolean isEof = c.moveToFirst();
			while(isEof){
				Tasks t = new Tasks(c.getString(0), c.getString(1), c.getString(2));
				tasks.add(t);
				adapter.add(c.getString(0));
				Log.d("MUB", "name:" + c.getString(0) + " url:" + c.getString(1) + " cookie:" + c.getString(2));
				isEof = c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}

		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ListView listView = (ListView) parent;
				// クリックされたアイテムを取得します
				//String item = (String) listView.getItemAtPosition(position);
				selected = position;
				showWebView(tasks.get(position).getURL());
			}

		});
	}

	private void showWebView(String url) {
		setContentView(R.layout.web_main1);
		if (url == null) {
			url = "http://google.co.jp";
		}
		this.webView = (WebView)findViewById(R.id.webView1);
		//webView.setWebViewClient(new WebViewClient());
		WebViewClient client = webClientList.get(0);
		webView.getSettings().setJavaScriptEnabled(true);
		if (android.os.Build.VERSION.SDK_INT > 10) {
			webView.getSettings().setPluginState(PluginState.ON);
		}

		//sessionクッキーをDBからロードしてくる
		DatabaseHelper helper = new DatabaseHelper(this);
		SQLiteDatabase db = null;
		Cursor c = null;
		String url1 = "";
		String sessionCookie = "";
		try {
			db = helper.getReadableDatabase();
			c = db.query("emp", new String[] { "url", "cookie" }, "key=" + (selected+1), null, null, null, null);
			boolean isEof = c.moveToFirst();
			while(isEof){
				url1 = c.getString(0);
				sessionCookie = c.getString(1);

				Log.d("MUB", "key=" + (selected+1) + " url=" + url1 + " session=" + sessionCookie);

				isEof = c.moveToNext();
			}
		} finally {
			c.close();
			db.close();
		}
		webView.clearHistory();
		webView.clearCache(true);
		CookieManager cookieManager = CookieManager.getInstance();
		String domain = null;
		Pattern p2 = Pattern.compile("https?:\\/\\/([-_.!~*'()a-zA-Z0-9;?:\\@&=+\\$,%#]+)\\/?");
		Matcher m2 = p2.matcher(url1);
		if (m2.find()) {
			domain = m2.group(1);
			Log.d("MUB", "ドメイン=" + domain);
		} else {
			Log.d("MUB", "ドメイン取得失敗");
		}
		if (domain != null) {
			if (sessionCookie != null && sessionCookie.length() > 0) {
				//cookieManager.setCookie(domain, "session=" + sessionCookie + "");

				//cookieManager.removeSessionCookie();
				//cookieManager.removeExpiredCookie();

				String [] cookies = sessionCookie.split("; ");
				for (String cookie1 : cookies) {
					Log.d("MUB", "cookie1=" + cookie1);
					cookieManager.setCookie(domain, cookie1);
					cookieManager.setCookie("gree.jp", cookie1);
					cookieManager.setCookie("id.gree.net", cookie1);
					cookieManager.setCookie("t.gree.jp", cookie1);
					cookieManager.setCookie("games.gree.net", cookie1);
					cookieManager.setCookie("gree.net", cookie1);
					//cookieManager.setCookie("pf.gree.net", cookie1); //必要か不明
					//cookieManager.setCookie("apps.gree.net", cookie1); //必要か不明
				}
				CookieSyncManager.getInstance().sync();
			} else {
/*				cookieManager.setCookie("gree.jp", "balloon_shown_101=");
				cookieManager.setCookie("gree.jp", "gcid=");
				cookieManager.setCookie("gree.jp", "gssid=");
				cookieManager.setCookie("gree.jp", "last_time=");

				cookieManager.setCookie("id.gree.net", "balloon_shown_101=");
				cookieManager.setCookie("id.gree.net", "gcid=");
				cookieManager.setCookie("id.gree.net", "gssid=");
				cookieManager.setCookie("id.gree.net", "last_time=");

				cookieManager.setCookie("t.gree.jp", "balloon_shown_101=");
				cookieManager.setCookie("t.gree.jp", "gcid=");
				cookieManager.setCookie("t.gree.jp", "gssid=");
				cookieManager.setCookie("t.gree.jp", "last_time=");

				cookieManager.setCookie("games.gree.net", "balloon_shown_101=");
				cookieManager.setCookie("games.gree.net", "gcid=");
				cookieManager.setCookie("games.gree.net", "gssid=");
				cookieManager.setCookie("games.gree.net", "last_time=");


				CookieSyncManager.getInstance().sync();*/

				cookieManager.removeAllCookie();
				//CookieSyncManager.getInstance().sync();

				cookieManager.setCookie("games.gree.net", "__utma=");
				cookieManager.setCookie("games.gree.net", "__utmb=");
				cookieManager.setCookie("games.gree.net", "__utmc=");
				cookieManager.setCookie("games.gree.net", "__utmz=");
				cookieManager.setCookie("games.gree.net", "balloon_shown_101=");
				//cookieManager.setCookie("games.gree.net", "gcid=");
				//cookieManager.setCookie("games.gree.net", "gcid=B=");
				//cookieManager.setCookie("games.gree.net", "gssid=");
				//cookieManager.setCookie("games.gree.net", "last_time=''");
				CookieSyncManager.getInstance().sync();
			}
		}

		webView.setWebViewClient(client);
		webView.loadUrl(url);
		webView.requestFocus(View.FOCUS_DOWN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.optionsmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menuitem1:
			this.webView.goBack();
			return true;
		case R.id.menuitem2:
			this.webView.goForward();
			return true;
		case R.id.menuitem3:
			showUsersList();
			return true;
		/*case R.id.debug:
			debug();
			return true;
		/*
		case R.id.showcookie1:
			showCookie();
			return true;
		*/
		case R.id.savecookie1:
			saveCookie();
			return true;
		case R.id.reload1:
			webView.reload();
			return true;
		}

		return false;
	}

	private void showCookie() {
		CookieManager cookieManager = CookieManager.getInstance();
		String nullSessionCookie = "session=''";
		String url = webView.getUrl();
		String domain = null;
		Pattern p2 = Pattern.compile("https?:\\/\\/([-_.!~*'()a-zA-Z0-9;?:\\@&=+\\$,%#]+)\\/?");
		Matcher m2 = p2.matcher(url);
		String cookie = "";
		String sessionCookie = "";
		if (m2.find()) {
			domain = m2.group(1);
			Log.d("MUB", "ドメイン=" + domain);
			cookie = cookieManager.getCookie(domain);
			Log.d("MUB", "cookie:" + cookie);

			Pattern p1 = Pattern.compile("session=([0-9a-zA-Z_]+)");
			Matcher m1 = p1.matcher(cookie);
			if (m1.find()) {
				sessionCookie = m1.group(1);
			}
			Log.d("MUB", "cookie:" + sessionCookie);
		} else {
			Log.d("MUB", "ドメイン取得失敗");
		}
	}

	private void saveCookie() {
		CookieManager cookieManager = CookieManager.getInstance();
		String nullSessionCookie = "session=''";
		String url = webView.getUrl();
		String domain = null;
		Pattern p2 = Pattern.compile("https?:\\/\\/([-_.!~*'()a-zA-Z0-9;?:\\@&=+\\$,%#]+)\\/?");
		Matcher m2 = p2.matcher(url);
		String cookie = "";
		String sessionCookie = "";
		if (m2.find()) {
			domain = m2.group(1);
			Log.d("MUB", "ドメイン=" + domain);
			cookie = cookieManager.getCookie(domain);
			Log.d("MUB", "cookie:" + cookie);

			Pattern p1 = Pattern.compile("session=([0-9a-zA-Z_]+)");
			Matcher m1 = p1.matcher(cookie);
			if (m1.find()) {
				sessionCookie = m1.group(1);
			}
			Log.d("MUB", "cookie:" + sessionCookie);
		} else {
			Log.d("MUB", "ドメイン取得失敗");
		}

		DatabaseHelper helper = new DatabaseHelper(this);

		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.beginTransaction();
			if (cookie.length() > 0) {
				//helper.update(db, selected+1,  sessionCookie);
				helper.update(db, selected+1,  cookie);
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	private void debug() {
		String url = webView.getUrl();
		String domain = null;
		Pattern p2 = Pattern.compile("https?:\\/\\/([-_.!~*'()a-zA-Z0-9;?:\\@&=+\\$,%#]+)\\/?");
		Matcher m2 = p2.matcher(url);
		Log.d("MUB", "URL=" + url);
		if (m2.find()) {
			domain = m2.group(1);
			Log.d("MUB", "ドメイン=" + domain);
		} else {
			Log.d("MUB", "ドメイン取得失敗");
		}
	}

	protected void showMessage(String msg){
		Toast.makeText(
				this,
				msg, Toast.LENGTH_SHORT).show();
	}
}


