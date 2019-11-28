package ca.elohello.tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Messages extends AppCompatActivity {

    ImageButton boutonFeu;
    ImageButton boutonCompte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        ArrayList<Image> image = new ArrayList<>();
        image.add(new Image("Boris"));
        image.add(new Image("Bob"));

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(Messages.this, image));

        boutonFeu = (ImageButton)findViewById(R.id.feu);

        boutonFeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Messages.this, MainPage.class);
                startActivity(intent);
            }
        });

        boutonCompte = (ImageButton) findViewById(R.id.compte);

        boutonCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Messages.this, AccountInfo.class);
                startActivity(intent);
            }
        });

    }
}