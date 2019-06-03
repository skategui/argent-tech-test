package agis.guillaume.argent.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.Glide;

public class GlideImageView extends AppCompatImageView {


    public GlideImageView(Context context) {
        super(context);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * load an image from an external resource using an URL
     *
     * @param url url of the resource to load
     */
    public void loadImageURL(@NonNull String url) {
        Glide.with(getContext())
                .load(url)
                .into(this);
    }
}
