package ca.elohello.tp2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Image> people;
    Messages activityMessages;
    String nom;

    public MyAdapter(Messages activityMessages, ArrayList<Image> people) {
        this.activityMessages = activityMessages;
        this.people = people;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        private Image currentPerson;

        public MyViewHolder(final View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nomImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    nom = currentPerson.getNomImage();

                    System.out.println(nom);

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    //intent.putExtra("unite", unite);
                    view.getContext().startActivity(intent);
                }
            });
        }

        public void display(Image list) {
            currentPerson = list;
            name.setText(list.getNomImage());
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

    @Override
    public int getItemCount() {
        return people.size();
    }
}
