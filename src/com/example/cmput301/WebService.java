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
import java.util.ArrayList;
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
	public static Task put(Task task)
	{
		try
		{	
			//get data string
			String dataString = getDataString(toJson(task), "post");

			//setup connection
			HttpURLConnection conn = setupConnections();
		
			// Send data and get response
			String httpResponse = getHttpResponse(conn, dataString);
			
			// convert string response to json object
			JSONObject jsonObject = toJsonTask(httpResponse);
			
			// convert json object to task and return
			return toTask(jsonObject);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return null;
	}

	/**
	 * Deletes a task from the web server.
	 * @param task to be deleted.
	 * @return true if deleted, false if not
	 */
	public static boolean delete(String id)
	{
		try
		{
			//get data string
			String dataString = getDataString(id, "remove");

			//setup connection
			HttpURLConnection conn = setupConnections();

			//send data and get response
			String httpResponse = getHttpResponse(conn, dataString);
			
			//convert response to json object
			JSONObject jsonObject = new JSONObject(httpResponse);
			
			//return true if task was deleted, false if otherwise
			return jsonObject.getString("message").equals("removes") ? true : false;
		}
		catch (IOException e)
		{
			Log.d("IOException", e.getMessage());
		}
		catch (JSONException e)
		{
			Log.d("JSONException", e.getMessage());
		}
		return false;
	}

	/**
	 * Gets a task (if exists) from the web server.
	 * @param id to search for
	 * @return Task found, if nothing found returns null.
	 */
	public static Task get(String id)
	{
		try
		{
			//Construct data string
			String data =  getDataString(id, "get");

			// Setup Connection
			HttpURLConnection conn = setupConnections(); 

			// Send data and get response
			String httpResponse = getHttpResponse(conn,data);

			// convert string response to json object
			JSONObject jsonObject = toJsonTask(httpResponse);
			
			// convert json object to task and return
			return toTask(jsonObject);
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
	 * Posts a task on the web server, used when response needs to be added.  
	 * @param task to be updated
	 * @param response response to be added
	 * @return updated task.
	 */
	public static Task post(Task task, Response response)
	{
		try
		{	
			//get current version of task from webservice
			Task webTask = get(task.getId());
			
			// add new response
			webTask.addResponse(response);
			
			// get data string
			String dataString = getDataString(toJson(webTask), "post");
			
			// setup connection
			HttpURLConnection conn = setupConnections();
			
			// send data and get response
			String httpResponse = getHttpResponse(conn, dataString);
			
			// convert string response to json object
			JSONObject jsonObject = toJsonTask(httpResponse);
			
			// convert json object to task and return
			return toTask(jsonObject);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		return null;
	}


	/**
	 * Gets all tasks from web server.
	 * @return List<Task> of all tasks on web server.
	 */
	public static List<Task> list()
	{
		try
		{
			String dataString = getDataString((JSONObject)null, "list");

			//setup connection
			HttpURLConnection conn = setupConnections();

			//send data and get response
			String httpResponse = getHttpResponse(conn, dataString);
			
			//convert response to json array
			JSONArray jsonArray = new JSONArray(httpResponse);
			
			return fromJsonArray(jsonArray);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedOperationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	//private methods

	/* returns a list of tasks from a jsonArray */
	private static List<Task> fromJsonArray(JSONArray jsonArray) throws JSONException
	{

		List<Task> tasks = new ArrayList<Task>();
		for(int i = 0; i < jsonArray.length(); i++)
		{
			String id = jsonArray.getJSONObject(i).getString("id");
			tasks.add(get(id));
		}
		return tasks;
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
	private static String getHttpResponse(URLConnection conn, String data) throws IOException
	{
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush(); 
		wr.close(); 

		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line, httpResponse = "";
		while ((line = rd.readLine()) != null) {
			// Process line...
			httpResponse += line;
		}
		rd.close();
		return httpResponse;
	}
	
	/* Converts json string into a task object and returns. */
	private static Task toTask(JSONObject obj) throws JSONException
	{
		if(obj==null)
		{
			return null;
		}
		else
		{
			return new Task(obj.getString("name"), obj.getString("description"), obj.getString("id")
					,obj.getInt("status"), toResponses(obj));
		}
	}

	/* Gets list of responses from jsonObject and returns */
	private static List<Response> toResponses(JSONObject jsonObject) throws JSONException
	{
		try
		{
			JSONArray jsonArray = jsonObject.getJSONArray("responses");
			List<Response> responses = new ArrayList<Response>();
			String type = jsonObject.getString("type");
			if(type.equals(TextResponse.class.toString()))
			{
				for(int i = 0; i < jsonArray.length(); i++)
				{
					responses.add(new TextResponse(jsonArray.getJSONObject(i).getString("content"),
							new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(jsonArray.getJSONObject(i).getString("timestamp"))));
				}
			}
			else if(type.equals(PictureResponse.class.toString()))
			{
				throw new UnsupportedOperationException("Not implemented");
			}
			else if(type.equals(AudioResponse.class.toString()))
			{
				throw new UnsupportedOperationException("Not implemented");
			}
			else
			{
				throw new IllegalStateException();
			}

			return responses;
		}
		catch(ParseException e)
		{
			System.err.println("Could not parse date");
			e.printStackTrace();
		}
		return null;
	}

	/* Constructs a data string to send to the web server.  */
	private static String getDataString(JSONObject jsonObject, String action) throws UnsupportedEncodingException, JSONException, UnsupportedOperationException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		if(action.equals("post"))
		{
			if(!jsonObject.isNull("id"))
			{
				data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(jsonObject.getString("id"),"UTF8");
			}
			data += "&" + URLEncoder.encode("content","UTF8")  + "=" + URLEncoder.encode(jsonObject.toString(),"UTF8");
			return data;
		}
		else if(action.equals("list"))
		{
			return data;
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}

	/* Overload.  For remove and get */
	private static String getDataString(String id, String action) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(id,"UTF8");
		return data;
	}
	
	/* Parse task into json object */
	private static JSONObject toJson(Task task) throws JSONException
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", task.getName());
		jsonObject.put("description", task.getDescription());
		jsonObject.put("id", task.getId());
		jsonObject.put("type", task.getType());
		jsonObject.put("status", task.getStatus());

		List<Response> responses = task.getResponses();
		JSONArray arr = new JSONArray();
		for(Response response : responses)
		{
			JSONObject jo = new JSONObject();
			jo.put("content", response.getContent());
			jo.put("timestamp", response.getTimestamp());
			arr.put(jo);
		}
		jsonObject.put("responses", arr);
		return jsonObject;
	}

	/* converts httpresponse from crowdsourcer into a json task object */
	private static JSONObject toJsonTask(String httpResponse) throws JSONException
	{
		// TODO Auto-generated method stub
		JSONObject jsonResponse = new JSONObject(httpResponse);
		JSONObject jsonTask = jsonResponse.getJSONObject("content");
		jsonTask.put("id", jsonResponse.get("id"));
		return jsonTask;
	}
	
}
