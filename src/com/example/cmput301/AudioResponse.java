package com.example.cmput301;

import java.util.Date;

import android.provider.MediaStore.Audio;

public class AudioResponse extends Response
{
	private Audio audio;
	
	public AudioResponse(Audio audio, String annotation, Date timestamp)
	{
		super(annotation, timestamp);
		this.audio = audio;
	}
	public Audio getAudio()
	{
		return this.audio;
	}
}
