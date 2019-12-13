package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TopImages extends AppCompatActivity {

    private ImageButton boutonFeu;
    private ImageButton boutonCompte;
    private ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private MyAdapter adapter;

    //Base URL to gather the data.
    //TODO : Make this customizable into the Pref, in case server change location.

    //public static final String url = "http://192.168.0.163/projects/TinderTesting/";
    public static final String url = "http://ratethis.benliam12.net/";

    private ArrayList<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        JsonTask jsonTask = new JsonTask();
        //jsonTask.execute("http://ratethis.benliam12.net/program.php?test");
        jsonTask.execute(url + "program.php?topImage");

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(TopImages.this, images));

        boutonFeu = (ImageButton)findViewById(R.id.feu);

        boutonFeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopImages.this, MainPage.class);
                startActivity(intent);
            }
        });

        boutonCompte = (ImageButton) findViewById(R.id.compte);

        boutonCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopImages.this, AccountInfo.class);
                startActivity(intent);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                new JsonTask().execute(url + "program.php?topImage");
            }
        });

        // Lookup the swipe container view
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * Adding the list of images to the program.
     * @param list
     */
    public void setup(ArrayList<Image> list)
    {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(new MyAdapter(TopImages.this, list));

        if(swipeContainer != null)
        {
            swipeContainer.setRefreshing(false);
        }
    }

    /**
     * Private class to load up JSON Data from web site URL
     */
    private class JsonTask extends AsyncTask<String, String, String> {

        private boolean refresh = false;

        protected void onPreExecute() {
            super.onPreExecute();
            if(swipeContainer != null) // Little check to be sure the swipeContainer actually exits. To prevent stupid crashes
            {
                swipeContainer.setRefreshing(true);
            }
        }

        // Loading up the juicy data.
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            images = readJson(result);
            setup(images);
        }
    }

    /**
     * Method that read the JSON data and put them in the recycle view.
     * @param data JSON data in String form.
     * @return Array Of Image.
     */
    public ArrayList<Image> readJson(String data) {
        try {
            JSONArray tester = new JSONArray(data);

            ArrayList<Image> images = new ArrayList<>();

            for (int i = 0; i < tester.length(); i++) {
                JSONObject object = tester.getJSONObject(i);
                String path = url + object.getString("path");
                Image image = new Image("Bobinette", path, i+1, object.getInt("rating"));
                images.add(image);
            }

            return images;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(this, "No Internet!", Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}