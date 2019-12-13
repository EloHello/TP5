package ca.elohello.tp2;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Image> people;
    TopImages activityTopImages;
    String nom;

    public MyAdapter(TopImages activityTopImages, ArrayList<Image> people) {
        this.activityTopImages = activityTopImages;
        this.people = people;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        private Image currentPerson;
        private ImageView imageViewer;
        private TextView imgPosition;
        private TextView imgRating;

        // Class to send HTTP POST DATA;


        public MyViewHolder(final View itemView) {
            super(itemView);
            imageViewer = itemView.findViewById(R.id.photoImage);
            imgPosition = itemView.findViewById(R.id.positionImage);
            imgRating = itemView.findViewById(R.id.rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void display(Image list) {
            currentPerson = list;
            Picasso.get().load(currentPerson.getImagePath()).into(imageViewer);
            //name.setText(list.getNomImage());
            imgPosition.setText(String.valueOf(currentPerson.getPosition()) + ".");
            imgRating.setText("Score :" + String.valueOf(currentPerson.getRating()));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        Image t = people.get(position);
        holder.display(t);
    }

    public void clear()
    {
        people.clear();
        notifyDataSetChanged();
    }

    public void setNewList(ArrayList<Image> people)
    {
        this.people = people;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (people != null) ? people.size() : 0;
    }
}
