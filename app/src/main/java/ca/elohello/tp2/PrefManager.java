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

    /**
     * Make default configuration.
     * @return default JSON config
     */
    public JSONObject buildDefault()
    {
        JSONObject object = new JSONObject();
        try
        {
            object.put("notification", true);
            object.put("shareData", false);
            object.put("betafunctions", false);

        } catch (JSONException ex)
        {

        }
        return object;
    }

    /**
     *
     * @param jsonObject JSon object of Settings
     * @param field Name Of the setting
     * @param data Default value
     * @return Current object
     * @throws JSONException If something went wrong.
     */
    public JSONObject checkValues(JSONObject jsonObject, String field, Object data) throws JSONException {
        if(!jsonObject.has(field))
            jsonObject.put(field, data);

        return jsonObject;
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

                this.checkValues(jsonObject, "notification", true);
                this.checkValues(jsonObject, "shareData", false);
                this.checkValues(jsonObject, "betafunctions", false);

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

    /**
     * @return JSONObject Settings
     */
    public JSONObject getSettings()
    {
        return this.settings;
    }

    /**
     * Set a new JsonObject as the settings
     * @param jsonObject
     */
    public void setSettings(JSONObject jsonObject)
    {
        this.settings = jsonObject;
    }

    /**
     * Save current settings.
     * @param context Where it has been done.
     */
    public void updateSettings(Context context)
    {
        this.writeToFile(this.settings.toString(), context);
    }

    /**
     * Save setting using custom JSONObject
     * @param jsonObject JSONObject settings
     * @param context Where it has been done.
     */
    public void updateSettings(JSONObject jsonObject, Context context)
    {
        this.writeToFile(jsonObject.toString(), context);
    }

    /**
     * Write to config.json file. Basically saving the config into a local file on the Android device.
     * @param data Data to write into the file
     * @param context Where you did this operation.
     */
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

    /**
     * Retreive the data from the config.json file. Will return null if not found or format is not json + Stackprint into the console log.
     * @param context Where you did it.
     * @return The data from the file as a String.
     */
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
            ex.printStackTrace();
        }

        return ret;
    }
}
