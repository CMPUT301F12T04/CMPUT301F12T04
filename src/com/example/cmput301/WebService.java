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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class WebService
{
	// public methods
	/**
	 * Adds a task to the web server.  
	 * @param task to be added
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
		catch (IOException e)
		{
			Log.d("IOException", e.getMessage());
		}
		catch (JSONException e)
		{
			Log.d("JSONException", e.getMessage());
		}

		return null;
	}

	/**
	 * Deletes a task from the web server.
	 * @param id of task to be deleted.
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
			Log.d("IOException", e.getMessage());
		}
		catch (JSONException e)
		{
			Log.d("JSONException", e.getMessage());
		}
	}

	/**
	 * Gets a task (if exists) from the web server.
	 * @param id of task to search for
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
		catch (IOException e)
		{
			Log.d("IOException", e.getMessage());
		}
		catch (JSONException e)
		{
			Log.d("JSONException", e.getMessage());
		}
		return null;
	}
	/**
	 * Updates a task on the web server, used when response needs to be added.
	 * @param task to be updated
	 * @return updated task.
	 */
	public static Task updateResponse(Task task)
	{
		try
		{
			//convert responses to JSON
			JSONArray responsesJSON = convertResponsesToJSON(task.getResponses());
			
			//construct data 
			String data = constructData("update",task.getId());
			data += "&" + URLEncoder.encode("summary","UTF8")  + "=" + URLEncoder.encode(task.getName(),"UTF8");
			data += "&" + URLEncoder.encode("description","UTF8")  + "=" + URLEncoder.encode(task.getDescription(),"UTF8");
			data += "&" + URLEncoder.encode("content","UTF8")  + "=" + URLEncoder.encode(responsesJSON.toString(),"UTF8");

			// Setup Connection
			HttpURLConnection conn = setupConnections(); 

			// Send data and get response
			JSONObject taskJSON = getHttpResponse(conn, data);
			
			Task nTask = parseJSONIntoTask(taskJSON);
			return nTask;
			
		}
		catch (IOException e)
		{
			Log.d("IOException", e.getMessage());
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static Task parseJSONIntoTask(JSONObject taskJSON) throws JSONException, ParseException
	{
		//get list of responses
		JSONArray nResponses = (JSONArray)taskJSON.get("content");
		
		
		//parse json --> task
		

		//create new task to eventually return 
		Task someTask = new Task(taskJSON.getString("summary"), taskJSON.getString("description"), taskJSON.getString("id"));
		
		//add all responses to task
		for(int i = 0; i < nResponses.length(); i++)
		{
			JSONObject obj = (JSONObject) nResponses.get(i);
			@SuppressWarnings("unchecked")
			String type = obj.getString("type");
			Response response;
			if(type.equals(TextResponse.class.toString()))
			{
				response = new TextResponse(obj.getString("content"),
						new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(obj.getString("timestamp")));
			}
			else if(type.equals(PictureResponse.class.toString()))
			{
				//wrong
				response = new TextResponse(obj.getString("content"), (Date)obj.get("timestamp"));
			}
			else if(type.equals(AudioResponse.class.toString()))
			{
				//wrong
				response = new TextResponse(obj.getString("content"), (Date)obj.get("timestamp"));
			}
			else
			{
				throw new IllegalStateException();
			}
			someTask.addResponse(response);	
		}
		return someTask;
	}

	private static JSONArray convertResponsesToJSON(List<Response> responses) throws JSONException
	{
		JSONArray responsesJSON = new JSONArray();
		for(Response response: responses)
		{
			JSONObject responseJSON = new JSONObject();
			responseJSON.accumulate("timestamp", response.getTimestamp().toString());
			responseJSON.accumulate("annotation", response.getAnnotation());
			responseJSON.accumulate("content", response.getContent());
			responseJSON.accumulate("type", response.getClass());
			responsesJSON.put(responseJSON);
		}
		return responsesJSON; 
	}

	public static Task postResponse(Task task, Response response) throws Exception
	{
		Task serverTask = getTask(task.getId());
		serverTask.addResponse(response);
		return null;
	}

	//private methods	
	/* Constructs a data string to send to the web server.  */
	private static String constructData(Task task) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode("post","UTF8");
		data += "&" + URLEncoder.encode("summary","UTF8")  + "=" + URLEncoder.encode(task.getName(),"UTF8");
		data += "&" + URLEncoder.encode("description","UTF8") + "=" + URLEncoder.encode(task.getDescription(),"UTF8");
		return data;
	}

	/*Constructs a data string to send to the web server and returns the data string. */
	private static String constructData(String action, String id) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(id,"UTF8");
		return data;
	}

	/* Sets up http connection to the web server and returns the connection.*/
	private static HttpURLConnection setupConnections() throws IOException
	{
		// Send data
		URL url = new URL("http://crowdsourcer.softwareprocess.es/F12/CMPUT301F12T04/?");
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setDoOutput(true);
		return conn;
	}

	/* Sends data to the web server and returns the response.*/
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
