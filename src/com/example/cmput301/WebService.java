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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class WebService
{
	/* public methods */

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
	 * Erases everything form web service.
	 * @param key Password
	 * @return String response
	 */
	public static String nuke(String key)
	{	
		try
		{
			//Construct data string
			String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode("nuke","UTF8");
			data += "&" + URLEncoder.encode("key","UTF8")  + "=" + URLEncoder.encode(key,"UTF8");
			
			// Setup Connection
			HttpURLConnection conn = setupConnections(); 

			// Send data and get response
			return getHttpResponse(conn,data);
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

	/* private methods */

	/**
	 * returns a list of tasks from a jsonArray
	 * @param jsonArray of task objects
	 * @return List<Task>
	 * @throws JSONException
	 */
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

	/**
	 * Sets up http connection to the web server and returns the connection.
	 * @return HttpURLConnection
	 * @throws IOException
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

	/**
	 * Sets up http connection to the web server and returns the connection.
	 * @param conn URLConnection
	 * @param data string to send to web service.
	 * @return String response from web service.
	 * @throws IOException
	 */
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
	
	/**
	 * Converts json string into a task object and returns.
	 * @param jsonTask , task object in json format.
	 * @return Task
	 * @throws JSONException
	 */
	private static Task toTask(JSONObject jsonTask) throws JSONException
	{
		if(jsonTask==null)
		{
			return null;
		}
		else
		{
			return new Task(jsonTask.getString("name"), jsonTask.getString("description"), jsonTask.getString("id")
					,jsonTask.getInt("status"), toResponses(jsonTask));
		}
	}

	/**
	 * Gets list of responses from jsonObject and returns
	 * @param jsonTask , task object in json format.
	 * @return List<Response>
	 * @throws JSONException
	 */
	private static List<Response> toResponses(JSONObject jsonTask) throws JSONException
	{
		try
		{
			JSONArray jsonArray = jsonTask.getJSONArray("responses");
			List<Response> responses = new ArrayList<Response>();
			String type = jsonTask.getString("type");
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

	/** 
	 * Constructs a data string to send to the web server.
	 * @param jsonTask , task object in json format.
	 * @param action , http request method
	 * @return URLEncoded request string
	 * @throws UnsupportedEncodingException
	 * @throws JSONException
	 * @throws UnsupportedOperationException
	 */
	private static String getDataString(JSONObject jsonTask, String action) throws UnsupportedEncodingException, JSONException, UnsupportedOperationException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		if(action.equals("post"))
		{
			if(!jsonTask.isNull("id"))
			{
				data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(jsonTask.getString("id"),"UTF8");
			}
			data += "&" + URLEncoder.encode("content","UTF8")  + "=" + URLEncoder.encode(jsonTask.toString(),"UTF8");
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

	/**
	 * Overload, for remove and get
	 * @param id of task
	 * @param action http request method
	 * @return String http request string
	 * @throws UnsupportedEncodingException
	 */
	private static String getDataString(String id, String action) throws UnsupportedEncodingException
	{
		String data =  URLEncoder.encode("action","UTF8")  + "=" + URLEncoder.encode(action,"UTF8");
		data += "&" + URLEncoder.encode("id","UTF8")  + "=" + URLEncoder.encode(id,"UTF8");
		return data;
	}

	/**
	 * Parse task into json object
	 * @param task to be converted to json object
	 * @return JSONObject
	 * @throws JSONException
	 */
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

	/**
	 * converts Http response from crowdsourcer into a json task object 
	 * @param httpResponse
	 * @return JSONObject
	 * @throws JSONException
	 */
	private static JSONObject toJsonTask(String httpResponse) throws JSONException
	{
		// TODO Auto-generated method stub
		JSONObject jsonResponse = new JSONObject(httpResponse);
		JSONObject jsonTask = jsonResponse.getJSONObject("content");
		jsonTask.put("id", jsonResponse.get("id"));
		return jsonTask;
	}
	
}
