package ca.elohello.tp2;

import android.graphics.Bitmap;

public class Image {
    protected String nomImage;
    protected String imagePath;
    protected int position;

    public Image(String nomImage) {
        this.nomImage = nomImage;
        this.imagePath = null;
    }

    public Image(String nomImage, String imagePath, int position)
    {
        this.nomImage = nomImage;
        this.imagePath = imagePath;
        this.position = position;
    }

    public String getImagePath()
    {
        return this.imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public int getPosition()
    {
        return this.position;
    }

    public String getNomImage() {
        return nomImage;
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }
}
