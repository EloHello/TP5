package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageHelper;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainPage extends AppCompatActivity {

    ImageButton boutonCompte;
    ImageButton boutonMessages;
    ImageView imageSwipe;
    ProgressDialog pd;
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boutonCompte = (ImageButton) findViewById(R.id.compte);

        boutonCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, AccountInfo.class);
                startActivity(intent);
            }
        });

        boutonMessages = (ImageButton) findViewById(R.id.messagerie);

        boutonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPage.this, TopImages.class);
                startActivity(intent);
            }
        });

        class JsonTask extends AsyncTask<String, String, String> {

            public void readJson(String data)
            {
                try {
                    JSONObject jsonObject = new JSONObject(data);

                    int version = jsonObject.getInt("version");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch(NullPointerException e)
                {

                }
            }

            protected void onPreExecute() {
                super.onPreExecute();

                pd = new ProgressDialog(MainPage.this);
                pd.setMessage("Please wait");
                pd.setCancelable(false);
                pd.show();
            }

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
                if (pd.isShowing()){
                    pd.dismiss();
                }
                JSONArray jsonArray;
                try {
                    System.out.print(result);
                    jsonArray = new JSONArray(result);

                    for(int i = 0; i< jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        int id = object.getInt("id");
                        String url = object.getString("path");
                        ids.add(id);
                        urls.add(url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
                    ImageView bmImage;

                    public DownloadImageTask(ImageView bmImage) {
                        this.bmImage = bmImage;
                    }

                    protected Bitmap doInBackground(String... urls) {
                        String urldisplay = urls[0];
                        Bitmap mIcon11 = null;
                        try {
                            InputStream in = new java.net.URL(urldisplay).openStream();
                            mIcon11 = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            e.printStackTrace();
                        }
                        return mIcon11;
                    }

                    protected void onPostExecute(Bitmap result) {
                        bmImage.setImageBitmap(result);

                        if(result == null)
                        {
                            bmImage.setImageResource(R.drawable.bonhomme);
                        }
                    }
                }

                ViewPager viewPager = (ViewPager) findViewById(R.id.photoSwipe);

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    int lastpos = 0;
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(lastpos < position){
                            ImageView im = (ImageView) findViewById(R.id.photoCompare);

                            new DownloadImageTask(im)
                                    .execute("http://ratethis.benliam12.net/" + urls.get(lastpos));
                        }

                        lastpos = position;
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // Pass results to ViewPagerAdapter Class
                PagerAdapter adapter = new ViewPagerAdapter(MainPage.this, ids, urls);
                // Binds the Adapter to the ViewPager
                viewPager.setAdapter(adapter);
            }
        }

        new JsonTask().execute("http://ratethis.benliam12.net/program.php?test");

    }
}