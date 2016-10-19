package singularfactory.app.common;

import android.util.Log;

import singularfactory.app.BuildConfig;

public class Logs
{
	//Show system logs on the screen or hide it.
	public static void SystemLog(String text){
		if (BuildConfig.SHOW_LOGS){	//Show log
			System.out.println(text);
		}
	}
	
	//Show the different error type logs on the screen or hide it.
	public static void MessageLogs(String tag, String text, String type){

		if (BuildConfig.SHOW_LOGS){
			switch (type){
				case "i":
					Log.i(tag, text);		//Info
					break;
				case "v":
					Log.v(tag, text);		//Verbose
					break;
				case "w":
					Log.w(tag, text);		//Warning
					break;
				case "e":
					Log.e(tag, text);		//Error
					break;
				case "wtf":
					Log.wtf(tag, text);		//What Terrible Failure
					break;
			}
		}
	}
}
