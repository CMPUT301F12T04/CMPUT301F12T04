package com.example.crowdsource;

import java.util.Date;

import android.provider.MediaStore.Audio;

public class AudioResponse extends Response
{
	private Audio audio;
	
	public AudioResponse(Audio audio, Date timestamp)
	{
		super.setTimestamp(timestamp);
		this.audio = audio;
	}
	public Audio getAudio()
	{
		return this.audio;
	}
}
