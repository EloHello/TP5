package ca.elohello.tp2;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Have the method required to read files around the app.
 * @author William D'Anjou
 * @version 1.0
 */
public class PrefManager {

    private static PrefManager instance = new PrefManager();
    public static PrefManager getInstance(){return instance;}

    private PrefManager(){}

    private JSONObject settings;
    private String file = "config.json";

    public JSONObject buildDefault()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("notification", true);
            object.put("shareData", false);

        } catch (JSONException ex)
        {

        }

        return object;
    }

    /**
     * Setup method.
     * @param context
     */
    public void setup(Context context)
    {
        String results = this.readFromFile(context);
        if(results != null)
        {
            try
            {
              JSONObject jsonObject = new JSONObject(results);
              this.settings = jsonObject;
            } catch (JSONException ex)
            {
                this.settings = buildDefault();
            }
        }
        else
        {
            //Default config;
            this.settings = this.buildDefault();
        }
    }

    public JSONObject getSettings()
    {
        return this.settings;
    }

    public void setSettings(JSONObject jsonObject)
    {
        this.settings = jsonObject;
    }

    public void updateSettings(Context context)
    {
        this.writeToFile(this.settings.toString(), context);
    }

    public void updateSettings(JSONObject jsonObject, Context context)
    {
        this.writeToFile(jsonObject.toString(), context);
    }

    public void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(this.file, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(this.file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        try
        {
            JSONObject jsonObject = new JSONObject(ret);
            this.settings = jsonObject;
        } catch (JSONException ex)
        {
        }

        return ret;
    }
}
