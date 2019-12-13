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
import android.widget.Button;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Main page where the magic happens.
 */
public class MainPage extends AppCompatActivity {

    ImageButton boutonCompte;
    ImageButton boutonMessages;
    ImageView imageSwipe;
    ProgressDialog pd;
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    private int TopID = 1;

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

        /**
         * All image class Loader.
         */
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
                class DataSender extends AsyncTask<String, Void, String> {

                    String result = "";

                    String urlString;
                    HashMap<String, String> fields;

                    public DataSender(HashMap<String, String> data, String urlString)
                    {
                        this.urlString = urlString;
                        this.fields = data;
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        BufferedReader reader=null;
                        String result = "";
                        try
                        {
                            String data = "";

                            for(Map.Entry<String, String> entry : this.fields.entrySet())
                            {
                                data += URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8") + "&";
                            }

                            URL url = new URL(urlString);

                            // Send POST data request
                            URLConnection conn = url.openConnection();
                            conn.setDoOutput(true);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            wr.write( data );
                            wr.flush();

                            // Get the server response
                            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line = null;

                            // Read Server Response
                            while((line = reader.readLine()) != null)
                            {
                                // Append server response in string
                                sb.append(line + "\n");
                            }
                            result = sb.toString();
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                    }
                }

                final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.photoSwipe);


                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    int lastpos = 0;
                    boolean b = true;
                    ArrayList<Integer> sids = new ArrayList<Integer>();
                    ArrayList<String> surls = new ArrayList<String>();
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        if(b){
                            for (int i=0; i<ids.size(); i++) {
                                list.add(new Integer(i));
                            }
                            sids = new ArrayList<Integer>();
                            surls = new ArrayList<String>();
                            Collections.shuffle(list);
                            for (int i=0; i<4; i++) {
                                sids.add(ids.get(list.get(i)));
                                surls.add(urls.get(list.get(i)));
                            }
                            b=false;
                        }
                        if (position == 0)
                        {
                            viewPager.setCurrentItem(2);
                        }
                        else if (position == 2){

                        }
                        else{
                            HashMap<String, String> datas = new HashMap<>();
                            int loser=1;
                            int winner=1;
                            if(1 == position){ // DROITE

                                ImageView im = (ImageView) findViewById(R.id.photoCompare);

                                new DownloadImageTask(im)
                                        .execute(TopImages.url + surls.get(2));

                                loser = TopID;
                                TopID = sids.get(2); // Nouveau champion
                                winner = TopID;
                            }
                            else if(3 == position) // GAUCHE
                            {
                                winner = TopID;
                                loser = sids.get(2);

                            }
                            datas.put("user", "1");
                            datas.put("upVote", String.valueOf(winner));
                            datas.put("downVote", String.valueOf(loser));

                            System.out.println("WINNER : " + String.valueOf(winner) + " -- LOSER : " + String.valueOf(loser));

                            new DataSender(datas, TopImages.url + "program.php").execute();
                            lastpos = position;


                            sids = new ArrayList<Integer>();
                            surls = new ArrayList<String>();
                            Collections.shuffle(list);
                            for (int i=0; i<4; i++) {
                                sids.add(ids.get(list.get(i)));
                                surls.add(urls.get(list.get(i)));
                            }


                            // Pass results to ViewPagerAdapter Class
                            PagerAdapter adapter = new ViewPagerAdapter(MainPage.this, sids, surls);
                            // Binds the Adapter to the ViewPager
                            viewPager.setAdapter(adapter);
                            viewPager.setCurrentItem(2);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                // Pass results to ViewPagerAdapter Class
                PagerAdapter adapter = new ViewPagerAdapter(MainPage.this, ids, urls);
                // Binds the Adapter to the ViewPager
                viewPager.setAdapter(adapter);
                viewPager.setCurrentItem(2);
                viewPager.setCurrentItem(1);

                ImageButton xBtn = (ImageButton) findViewById(R.id.dislike);
                xBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(3);
                    }
                });

                ImageButton yBtn = (ImageButton) findViewById(R.id.like);
                yBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(1);
                    }
                });
            }
        }
        new JsonTask().execute(TopImages.url + "program.php?test");
    }
}