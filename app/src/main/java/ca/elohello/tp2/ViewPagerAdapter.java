package ca.elohello.tp2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.io.InputStream;
import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> image;
    ArrayList<Integer> id;
    LayoutInflater inflater;


    public ViewPagerAdapter(Context context, ArrayList<Integer> id, ArrayList<String> image) {
        this.context = context;
        this.image = image;
        this.id = id;
    }

    @Override
    public int getCount() {
        return id.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        // Declare Variables
        ImageView imgflag;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container, false);


        // Locate the ImageView in viewpager_item.xml

        final int pos = position;
        imgflag = (ImageView) itemView.findViewById(R.id.image);
        // Capture position and set to the ImageView
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

        new DownloadImageTask((ImageView) itemView.findViewById(R.id.image))
                .execute("http://ratethis.benliam12.net/" + image.get(position));


        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);

    }
}
