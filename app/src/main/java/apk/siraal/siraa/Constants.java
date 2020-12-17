package apk.siraal.siraa;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {



    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.new_disc, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }




}
