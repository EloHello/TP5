package ca.elohello.tp2;

/**
 * Image objects to handle received images.
 */
public class Image {
    private String nomImage;
    private String imagePath;
    private int position;
    private int rating;

    /**
     * Constructor
     * @param nomImage Image name (Old system)
     */
    public Image(String nomImage) {
        this.nomImage = nomImage;
        this.imagePath = null;
    }

    /**
     * Full constructor
     * @param nomImage Image name
     * @param imagePath Path to load the image
     * @param position Reference for system to properly work.
     * @param rating Rank of the image at request.
     */
    public Image(String nomImage, String imagePath, int position, int rating)
    {
        this.nomImage = nomImage;
        this.imagePath = imagePath;
        this.position = position;
        this.rating = rating;
    }

    /**
     * @return Image path
     */
    public String getImagePath()
    {
        return this.imagePath;
    }

    /**
     * Overwrite image path
     * @param imagePath New image path. Shouldn't be used because handled on server side.
     * @deprecated
     */
    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    /**
     * Overwrite image position.
     * @param position new Position.  Handled by the program. Shouldn't be changed.
     * @deprecated
     */
    public void setPosition(int position)
    {
        this.position = position;
    }

    /**
     * @return Image rating
     */
    public int getRating()
    {
        return this.rating;
    }

    /**
     * @return Image Position
     */
    public int getPosition()
    {
        return this.position;
    }

    /**
     * @return Image name
     * @deprecated
     */
    public String getNomImage() {
        return nomImage;
    }

    /**
     * Overwrite image name
     * @param nomImage Image name. Handled server side.
     * @deprecated
     */
    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }
}
