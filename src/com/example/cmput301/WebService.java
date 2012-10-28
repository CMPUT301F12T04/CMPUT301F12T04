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
	// public methods
	/**
	 * Adds a task to the web server. If it's a new task then it will 
         * generate an id and return the task with that id.
         * 
	 * @param task Task to be added
	 * @return The task received from web server with id included
	 */
	public static Task post(Task task)
	{
		try 
		{
			//Construct data string
			String data =  constructData(task);

			//setup connection
			HttpURLConnection conn = setupConnections();

			//send data and get response
			JSONObject nTask = getHttpResponse(conn, data);
			
			return new Task(nTask.getString("summary"), nTask.getString("description"), nTask.getString("id"));

		} 
		catch (Exception e) 
		{
			System.err.println("Exception " + e.toString());
		}
		
		return null;
	}
	
	/**
	 * Deletes a task from the web server.
	 * @param id ID of task to be deleted.
	 */
	public static void deleteTask(String id)
	{
		try
		{
			//Construct data string 
			String data =  constructData("remove", id);

			//setup connection
			HttpURLConnection conn = setupConnections(); 

			//send data and get response
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
	
	/**
	 * Gets a task (if exists) from the web server.
	 * @param id ID of task to search for
	 * @return Task found, if nothing found returns null.
	 */
	public static Task getTask(String id)
	{
		try
		{
			//Construct data string
			String data =  constructData("get", id);

			// Setup Connection
			HttpURLConnection conn = setupConnections(); 

			// Send data and get response
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

	public void postResponse(String id, Response response) throws Exception
	{
		throw new Exception("Not implemented");
	}
	
	//private methods
	
	/*
	 * Constructs a data string to send to the web server.  
	 */
	private static String constructData(Task task) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode("post","UTF8");
		data += "&" + URLEncoder.encode("summary","UTF8")  + "=" + URLEncoder.encode(task.getName(),"UTF8");
		data += "&" + URLEncoder.encode("description","UTF8") + "=" + URLEncoder.encode(task.getDescription(),"UTF8");
		return data;
	}

	/*
	 * Constructs a data string to send to the web server and returns the data string.
	 */
	private static String constructData(String action, String id) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(id,"UTF8");
		return data;
	}

	/*
	 * Sets up http connection to the web server and returns the connection.
	 */
	private static HttpURLConnection setupConnections() throws IOException
	{
		// Send data
		URL url = new URL("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T04/?");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setDoOutput(true);
		return conn;
	}

	/*
	 * Sends data to the web server and returns the response.
	 */
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
}
