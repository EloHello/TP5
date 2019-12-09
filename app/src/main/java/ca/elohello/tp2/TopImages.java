package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class TopImages extends AppCompatActivity {

    ImageButton boutonFeu;
    ImageButton boutonCompte;
    ProgressDialog pd;

    private ArrayList<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        JsonTask jsonTask = new JsonTask();
        jsonTask.execute("http://ratethis.benliam12.net/program.php?test");

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
    }

    public void setup(ArrayList<Image> list)
    {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(TopImages.this, list));
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(TopImages.this);
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

            images = readJson(result);

            setup(images);
        }
    }

    public ArrayList<Image> readJson(String data) {
        try {
            JSONArray tester = new JSONArray(data);

            ArrayList<Image> images = new ArrayList<>();

            for (int i = 0; i < tester.length(); i++) {
                Image image = new Image("Bobinette");
                JSONObject object = tester.getJSONObject(i);
                DlImage dlImage = new DlImage(image);
                dlImage.execute("http://ratethis.benliam12.net/" + object.getString("path"));
                image = dlImage.getImage();
                new DlImage(image).execute("http://ratethis.benliam12.net/" + object.getString("path"));

                images.add(image);
            }

            return images;

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Toast.makeText(this, "Can't retreive data =(", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private class DlImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        Image image;

        public DlImage(Image image) {
            this.image = image;
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
            this.image.setBitmap(result);
        }

        public Image getImage()
        {
            return this.image;
        }
    }

    public void loadImage(String url)
    {

    }
}