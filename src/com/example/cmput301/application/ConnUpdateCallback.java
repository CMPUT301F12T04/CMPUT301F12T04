package com.example.cmput301.application;

/**
 *  CallBack interface that allows different controllers to message
 *  our main activity after/before an Internet connection is made.
 */
public interface ConnUpdateCallback
{
	/**
	 * Connection finished successfully, stop loading screen
	 */
	void finished();
	
	/**
	 * Uploading started, launch loading screen
	 */
	void startUploadingScreen();
	
	/**
	 * Web service sync started, launch loading screen.
	 */
	void startSyncLoadingScreen();
	
	/**
	 * Connection failed, display error message.
	 */
	void failed();
}
