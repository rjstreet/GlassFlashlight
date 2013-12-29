package org.newmyths.glassflashlight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


// The "main" activity...
public class MainActivity extends Activity
{
    // For tap events
    private GestureDetector mGestureDetector;
    // Voice action.
    private String voiceAction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d("onCreate() called.");

        setContentView(R.layout.activity_main);

        // For gesture handling.
        mGestureDetector = createGestureDetector(this);

        //voiceAction = getVoiceAction(getIntent());
        //if(Log.I) Log.i("voiceAction = " + voiceAction);
        //processVoiceAction(voiceAction);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("onResume() called");

        // This makes it impossible to exit the app.
//        voiceAction = getVoiceAction(getIntent());
//        if(Log.I) Log.i("voiceAction = " + voiceAction);
//        processVoiceAction(voiceAction);
    }

    // Returns the "first" word from the phrase following the prompt.
    private String getVoiceAction(Intent intent)
    {
        if(intent == null) {
            return null;
        }
        String action = null;
        Bundle extras = intent.getExtras();
        ArrayList<String> voiceActions = null;
        if(extras != null) {
            voiceActions = extras.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
            if(voiceActions != null && !voiceActions.isEmpty()) {
                if(Log.D) {
                    for(String a : voiceActions) {
                        Log.d("action = " + a);
                    }
                }
                action = voiceActions.get(0);
            }
        }
        return action;
    }

    // Opens the WordDictation activity,
    // or, quits the program.
    private void processVoiceAction(String voiceAction)
    {
        if(voiceAction != null) {
            if(voiceAction.equals("game")) {

            } else if(voiceAction.equals("stop")) {
                Log.i("VoiceDemo activity has been terminated upon start.");
                this.finish();
            } else {
                Log.w("Unknown voice action: " + voiceAction);
            }
        } else {
            Log.w("No voice action provided.");
        }
    }

    ///////////////////////////////////////////////////////
    /// Gesture handling
    //

    @Override
    public boolean onGenericMotionEvent(MotionEvent event)
    {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return super.onGenericMotionEvent(event);
    }

    private GestureDetector createGestureDetector(Context context)
    {
        GestureDetector gestureDetector = new GestureDetector(context);
        //Create a base listener for generic gestures
        gestureDetector.setBaseListener( new GestureDetector.BaseListener() {
            @Override
            public boolean onGesture(Gesture gesture) {
                if(Log.I) Log.i("gesture = " + gesture);
                if (gesture == Gesture.TAP) {
                	 URL url;
                	    HttpURLConnection connection = null;  
                	    try {
                	      //Create connection
                	      url = new URL("192.168.1.177:8002");
                	      String urlParameters = "";
                	      connection = (HttpURLConnection)url.openConnection();
                	      connection.setRequestMethod("POST");
                	      connection.setRequestProperty("Content-Type", 
                	           "application/x-www-form-urlencoded");
                				
                	      connection.setRequestProperty("Content-Length", "" + 
                	               Integer.toString(urlParameters.getBytes().length));
                	      connection.setRequestProperty("Content-Language", "en-US");  
                				
                	      connection.setUseCaches (false);
                	      connection.setDoInput(true);
                	      connection.setDoOutput(true);

                	      //Send request
                	      DataOutputStream wr = new DataOutputStream (
                	                  connection.getOutputStream ());
                	      wr.writeBytes (urlParameters);
                	      wr.flush ();
                	      wr.close ();

                	      //Get Response	
                	      InputStream is = connection.getInputStream();
                	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                	      String line;
                	      StringBuffer response = new StringBuffer(); 
                	      while((line = rd.readLine()) != null) {
                	        response.append(line);
                	        response.append('\r');
                	      }
                	      rd.close();

                	    } catch (Exception e) {
                	      e.printStackTrace();
                	    } finally {
                	      if(connection != null) {
                	        connection.disconnect(); 
                	      }
                	    }                	
                    return true;
                } // etc...
                return false;
            }
        });
        return gestureDetector;
    }

}