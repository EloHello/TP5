package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the class that handle the CreateAccount page.
 * @author William D'Anjou
 * @version 1.0
 */
public class CreateAccount extends AppCompatActivity {

    int age;
    Button boutonOk;

    EditText username;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;

    /**
     * Create the page as it should be.
     * @param savedInstanceState Old instance if found.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerAge);
        ArrayList<Integer> items = new ArrayList<>();
        for(int i = 0; i < 100; i++)
        {
            items.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                age = (int)spinner.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.username = (EditText) findViewById(R.id.nouveauUserName);
        this.firstName = (EditText) findViewById(R.id.nouveauPrenomUser);
        this.lastName = (EditText) findViewById(R.id.nouveauNomUser);
        this.email = (EditText) findViewById(R.id.nouveauEmailUser);
        this.password = (EditText) findViewById(R.id.nouveauMDPUser);


        //si toutes informations remplies

        boutonOk = (Button) findViewById(R.id.boutonOkCreate);

        boutonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    /**
     * Make register request to server in order to make an account.
     */
    private void register()
    {
        String username = this.username.getText().toString();
        String firstName = this.firstName.getText().toString();
        String lastName = this.lastName.getText().toString();
        String email    = this.email.getText().toString();
        String password = this.password.getText().toString();

        if(
            !username.isEmpty() &&
            !firstName.isEmpty() &&
            !lastName.isEmpty() &&
            !email.isEmpty() &&
            !password.isEmpty()
        )
        {
            HashMap<String, String> data = new HashMap<>();
            data.put("user", username);
            data.put("firstname", firstName);
            data.put("lastname", lastName);
            data.put("email", email);
            data.put("password", password);
            data.put("age", String.valueOf(age));

            DataSender dataSender = new DataSender(data, TopImages.url + "program.php?register");
            dataSender.execute();
        }
        else
        {
            Toast.makeText(this, getString(R.string.requireFields), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Custom class to send data.
     */
    public class DataSender extends AsyncTask<String, Void, String> {

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

        /**
         * When data has been processed.
         * @param result Result that was send by the {@link CreateAccount.DataSender#doInBackground(String...)} class.
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(!result.isEmpty())
            {
                try {
                    System.out.println(result);
                    JSONObject json = new JSONObject(result);

                    if(json.has("success"))
                    {
                        boolean success = json.getBoolean("success");
                        if(success)
                        {
                            Intent intent = new Intent(CreateAccount.this, MainPage.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(CreateAccount.this, getString(R.string.alreadyUser), Toast.LENGTH_SHORT).show();
                        }
                        /*Intent intent = new Intent(CreateAccount.this, MainPage.class);
                        startActivity(intent);*/
                    }
                    else
                    {
                        Toast.makeText(CreateAccount.this, getString(R.string.serverSideError), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException ex)
                {
                    ex.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(CreateAccount.this, getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
            }
        }
    }
}