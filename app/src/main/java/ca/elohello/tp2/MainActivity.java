package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Main activy class, Login page.
 * @author William D'Anjou
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity{

    Button boutonOk;
    Button boutonCreate;

    EditText user;
    EditText pass;

    GatherData gatherData = null;


    @Override
    protected void onActivityResult(int requestCode, int ResponseCode, Intent resultIntent)
    {
        super.onActivityResult(requestCode, ResponseCode, resultIntent);

        if(requestCode == 0)
        {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        boutonOk = (Button)findViewById(R.id.boutonOK);

        this.user = (EditText) findViewById(R.id.nomIdentification);
        this.pass = (EditText) findViewById(R.id.motDePasse);

        boutonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });

        boutonCreate = (Button) findViewById(R.id.boutonCreate);

        boutonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAccount.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    /**
     * Make a http request to server to login the user.
     */
    public void login()
    {
        if(gatherData != null)
            if(gatherData.getStatus() != AsyncTask.Status.FINISHED)
                return;

        if(this.user != null && this.pass != null)
        {
            HashMap<String, String> data = new HashMap<>();
            String user = this.user.getText().toString();
            String pass = this.pass.getText().toString();

            data.put("user", user);
            data.put("pass", pass);

            GatherData gatherData = new GatherData(data,TopImages.url + "program.php?login");
            gatherData.execute();

            this.gatherData = gatherData;

        }
    }



    public class GatherData extends AsyncTask<String, Void, String>
    {

        private HashMap<String, String> fields = new HashMap<>();
        private String urlString;

        public GatherData(HashMap<String, String> data, String urlString)
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

                //String data = URLEncoder.encode("name", "UTF-8")
                  //      + "=" + URLEncoder.encode("SLT", "UTF-8"); // To add more, u need & between fields
                // Defined URL  where to send data
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

            if(!result.isEmpty())
            {
                try {
                    JSONObject json = new JSONObject(result);

                    if(json.has("token"))
                    {
                        String t = json.getString("token"); // Store this somewhere.

                        Intent intent = new Intent(MainActivity.this, MainPage.class);
                        startActivity(intent);

                        finish();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, getString(R.string.wrongUser), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex)
                {
                    System.out.println(result);
                    Toast.makeText(MainActivity.this, getString(R.string.serverSideError), Toast.LENGTH_SHORT).show();
                    ex.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(MainActivity.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
