package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Setting class that handles the settings options.
 */
public class AccountInfo extends AppCompatActivity {

    TextView info;
    ImageButton boutonMessages;
    ImageButton boutonFeu;
    Button button;
    SwitchCompat notif;
    SwitchCompat datas;
    SwitchCompat beta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

        Configuration config = new Configuration(getResources().getConfiguration());

        boutonMessages = (ImageButton) findViewById(R.id.messagerie);

        boutonFeu = (ImageButton) findViewById(R.id.feu);

        button = (Button) findViewById(R.id.button);

        boutonFeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfo.this, MainPage.class);
                startActivity(intent);
            }
        });

        boutonMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountInfo.this, TopImages.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
            }
        });




        JSONObject settings = PrefManager.getInstance().getSettings();

        notif = (SwitchCompat) findViewById(R.id.switch2);
        datas = (SwitchCompat) findViewById(R.id.switch3);
        beta = (SwitchCompat) findViewById(R.id.switch4);
        try
        {
            notif.setChecked(settings.getBoolean("notification"));
            datas.setChecked(settings.getBoolean("shareData"));
            beta.setChecked(settings.getBoolean("shareData"));
        } catch (JSONException ex)
        {

        }


        /*final Spinner spinner = (Spinner) findViewById(R.id.spinnerLangue);
        ArrayList<String> items = new ArrayList<>();

        items.add("Francais");
        items.add("English");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    /**
     * Update the stats
     */
    public void updateSettings()
    {
        try {
            JSONObject jsonObject = PrefManager.getInstance().getSettings();
            jsonObject.put("notification", notif.isChecked());
            jsonObject.put("shareData", datas.isChecked());
            jsonObject.put("betafunctions", beta.isChecked());
            PrefManager.getInstance().updateSettings(jsonObject, this);
            PrefManager.getInstance().setSettings(jsonObject);

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
            
            Log.i("INFO", "Setting updated!");
        } catch (JSONException ex)
        {
            Log.e("Exception", "ERROR ON SAVING SETTINGS");
        }
    }

}
