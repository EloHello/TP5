package ca.elohello.tp2;

import android.graphics.Bitmap;

public class Image {
    protected String nomImage;
    protected Bitmap bitmap;

    public Image(String nomImage) {
        this.nomImage = nomImage;
        this.bitmap = null;
    }

    public Image(String nomImage, Bitmap bitmap)
    {
        this.nomImage = nomImage;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public String getNomImage() {
        return nomImage;
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }
}
