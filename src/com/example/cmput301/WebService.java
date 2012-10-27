package com.example.cmput301;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;

public class WebService
{

	private static JSONObject getHttpResponse(URLConnection conn, String data) throws IOException
	{
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush(); 
		wr.close(); 

		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line, HTTPresponse = "";
		while ((line = rd.readLine()) != null) {
			// Process line...
			HTTPresponse += line;
		}
		rd.close();
		try
		{
			return new JSONObject(HTTPresponse);
		}
		catch (JSONException e)
		{
			return null;
		}
	}
	public static Task post(Task task)
	{
		try {
			String data =  constructData(task);

			// Send data
			HttpURLConnection conn = setupConnections();

			// Get the response
			JSONObject nTask = getHttpResponse(conn, data);

			return new Task(nTask.getString("summary"), nTask.getString("description"), nTask.getString("id"));

		} catch (Exception e) {
			System.err.println("Exception " + e.toString());
		}
		return null;
	}
	public static void deleteTask(String id)
	{
		try
		{
			String data =  constructData("remove", id);

			HttpURLConnection conn = setupConnections(); 

			// Get the response
			JSONObject nTask = getHttpResponse(conn, data);

			if(nTask.getString("message").equals("removed"))
			{
				System.out.println("Task removed");
			}
			else
			{
				throw new NoSuchElementException("Task not found");
			}
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Task getTask(String id)
	{
		try
		{
			//Construct data string
			String data =  constructData("get", id);

			// Setup Connection
			HttpURLConnection conn = setupConnections(); 

			// Get the response
			JSONObject nTask = getHttpResponse(conn, data);

			if(nTask.isNull("summary")||nTask.isNull("description"))
			{
				return null;
			}
			else
			{
				return new Task(nTask.getString("summary"), nTask.getString("description"), nTask.getString("id"));
			}
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void post(Response response)
	{
		try {
			// Construct data
			//			j.put("test", "something");
			//			j.put("test2", "something2");
			String data =  URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("update","UTF-8");
			data += "&" + URLEncoder.encode("summary", "UTF-8") + "=" + URLEncoder.encode("someReasonse", "UTF-8");
			data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode("this is a response", "UTF-8");
			data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode("39a2569b6b8298bf572a8675c7d9196366fcbeb1","UTF-8");
			//		    data += "&" + URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(j.toString(), "UTF-8");
			System.out.println(data);

			// Send data
			URL url = new URL("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T04/?");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			if (conn instanceof HttpURLConnection) {
				((HttpURLConnection)conn).setRequestMethod("POST");
			}
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				// Process line...
				System.out.println(line);
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
		}
	}
//private methods	
	private static String constructData(Task task) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode("post","UTF8");
		data += "&" + URLEncoder.encode("summary","UTF8")  + "=" + URLEncoder.encode(task.getName(),"UTF8");
		data += "&" + URLEncoder.encode("description","UTF8") + "=" + URLEncoder.encode(task.getDescription(),"UTF8");
		return data;
	}

	private static String constructData(String action, String id) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(id,"UTF8");
		return data;
	}

	private static HttpURLConnection setupConnections() throws IOException
	{
		// Send data
		URL url = new URL("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T04/?");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setDoOutput(true);
		return conn;
	}


}
