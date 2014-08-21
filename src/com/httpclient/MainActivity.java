package com.httpclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText in;
	TextView out;
	Button ok;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		in = (EditText) findViewById(R.id.editText1);
		out = (TextView) findViewById(R.id.textView1);
		ok = (Button) findViewById(R.id.button1);
		
		if(isConnected()){
			Toast.makeText(this, "Connected!", Toast.LENGTH_LONG).show();
        }
		else{
			Toast.makeText(this, "NOT conncted!", Toast.LENGTH_LONG).show();
		}
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new HttpTask().execute(in.getText().toString());				
			}
		});
	}

	public static String fetchData(String address){
		InputStream inputStream = null;
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(address));
			inputStream = httpResponse.getEntity().getContent();
			if(inputStream != null){
				
				BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		        String line = "";
		        while((line = bufferedReader.readLine()) != null)
		            result += line;		        
		        inputStream.close();
			}
			else{
				result = "Did not work!";
			}
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		return result;
	}

    public boolean isConnected(){
    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) 
    	    	return true;
    	    else
    	    	return false;	
    }
    
    private class HttpTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... arg) {
              
            return fetchData(arg[0]);
        }
        @Override
        protected void onPostExecute(String result) {
        	out.setText(result);
       }
    }


}