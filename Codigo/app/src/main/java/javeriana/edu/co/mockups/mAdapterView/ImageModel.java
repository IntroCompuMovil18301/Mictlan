package javeriana.edu.co.mockups.mAdapterView;

import android.graphics.Bitmap;

public class ImageModel {
    private int image_drawable;
    private Bitmap image_bitmap;

    public int getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(int image_drawable) {
        this.image_drawable = image_drawable;
    }

    public Bitmap getImage_bitmap() {
        return image_bitmap;
    }

    public void setImage_bitmap(Bitmap image_bitmap) {
        this.image_bitmap = image_bitmap;
    }
}
