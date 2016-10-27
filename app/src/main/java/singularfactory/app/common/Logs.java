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
}
