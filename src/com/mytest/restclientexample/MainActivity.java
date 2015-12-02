package com.mytest.restclientexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button getData = (Button) findViewById(R.id.getservicedata);
		getData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String restURL = "http://api.openweathermap.org/data/2.5/weather?appid=79bcb0aa252eda1770734cf9660f15ce";
				new RestOperation().execute(restURL);

			}
		});
	}

	private class RestOperation extends AsyncTask<String, Void, Void> {
		private String TAG = "RestOperation";
		private final String API_KEY = "79bcb0aa252eda1770734cf9660f15ce";
		String content;
		String error;
		ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
		String data = "";
		TextView serverDataReceived = (TextView) findViewById(R.id.serverDataReceived);
		TextView showParsedJSON = (TextView) findViewById(R.id.showParsedJSON);
		EditText userinput = (EditText) findViewById(R.id.userinput);

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog.setTitle("Please wait ...");
			progressDialog.show();

			try {
//				data += "&" + URLEncoder.encode("appid", "UTF-8") + "=" + API_KEY;
				data += "&" + URLEncoder.encode("q", "UTF-8") + "=" + userinput.getText();
				data += "&units=metric";
				Log.i(TAG, "data... data:::"+data);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground(String... params) {
			BufferedReader br = null;

			URL url;
			try {
				String param = params[0]+data;
				url = new URL(param);
				Log.i(TAG, "1... reached");
				URLConnection connection = (URLConnection)url.openConnection();
				connection.setDoOutput(true);
				Log.i(TAG, "2... reached ");
//				OutputStreamWriter outputStreamWr = new OutputStreamWriter(connection.getOutputStream());
//				connection.getOutputStream();
				try {
					OutputStream output = connection.getOutputStream();
					Log.i(TAG, "3... reached");
//				    output.write(data.getBytes("UTF-8"));
//				    Log.i(TAG, "4... reached ");
				    output.flush();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				Log.i(TAG, "5... reached "+connection.getInputStream());
				br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				Log.i(TAG, "br... reached "+connection.getURL());
				while ((line = br.readLine()) != null) {
					sb.append(line);
					sb.append(System.getProperty("line.separator"));
				}

				content = sb.toString();

			} catch (MalformedURLException e) {
				error = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				error = e.getMessage();
				e.printStackTrace();
			} 
			catch (Exception e) {
				error = e.getMessage();
				e.printStackTrace();
			}finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			progressDialog.dismiss();

			if (error != null) {
				serverDataReceived.setText("Error " + error);
			} else {
				serverDataReceived.setText(content);

				String output = "";
				JSONObject jsonResponse;

				try {
					jsonResponse = new JSONObject(content);
					Log.d(TAG, "Content:::"+jsonResponse.getString("name"));
					Log.d(TAG, "Temp:::"+jsonResponse.getJSONObject("main").getString("temp"));
					showParsedJSON.setText(output);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
