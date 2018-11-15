package javeriana.edu.co.mockups.mAdapterView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import javeriana.edu.co.mockups.R;

public class ImageAddAdapter extends BaseAdapter {
    private List<String> images;
    private Context context;

    public ImageAddAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if( convertView == null ) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_add_model, parent,
                    false);
        }
        TextView titulo = convertView.findViewById(R.id.image_text);

        final String aux = (String) this.getItem(position);
        titulo.setText(aux);

        return convertView;
    }
}
