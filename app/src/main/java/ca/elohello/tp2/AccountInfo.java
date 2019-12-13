package ca.elohello.tp2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AccountInfo extends AppCompatActivity {

    TextView info;
    ImageButton boutonMessages;
    ImageButton boutonFeu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_screen);

        boutonMessages = (ImageButton) findViewById(R.id.messagerie);

        boutonFeu = (ImageButton) findViewById(R.id.feu);

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

    }


}
