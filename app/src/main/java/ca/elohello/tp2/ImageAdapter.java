package ca.elohello.tp2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ImageAdapter extends PagerAdapter {
    Context context;


    private int[] GalImages = new int[] {


            R.drawable.bonhomme,    //Here first,second,third... are the name of the jpeg files placed in drawable folder

    };

    ImageAdapter(Context context){

        this.context=context;

    }

    @Override

    public int getCount() {

        return GalImages.length;

    }


    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == ((ImageView) object);

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);


        imageView.setImageResource(GalImages[position]);

        ((ViewPager) container).addView(imageView, 0);

        return imageView;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ((ViewPager) container).removeView((ImageView) object);

    }

}
