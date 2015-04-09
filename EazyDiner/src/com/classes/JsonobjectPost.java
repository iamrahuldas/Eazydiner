package com.classes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JsonobjectPost {
	
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	
	public JSONObject getJSONObj(String url,
			List<NameValuePair> nameValuePairs, String jsonData)
	{
		try {
			DefaultHttpClient httpClient=new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("content-type", "application/json");
			/*if(nameValuePairs != null)
			{
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}*/
			StringEntity entity = new StringEntity(jsonData, HTTP.UTF_8);
			httpPost.setEntity(entity);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"),8);
			
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			Log.i("json result", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return jObj;
	}
	
	/*public PostObject getPostObject(String value, boolean isFile) {
		PostObject _postObject = new PostObject();
		_postObject.setFile(isFile);
		if (isFile) {
			_postObject.setFile_url(value);
		}
		_postObject.setString_value(value);
		return _postObject;
	}*/
}
