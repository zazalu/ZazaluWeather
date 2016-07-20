package util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.zazaluweather.com.zazaluweather.R;
import model.Province;

/**
 * Created by zazalu on 7/20/16.
 */
public class ProvinceAdapter extends ArrayAdapter<String> {
    private int resouceId;
    private int[] imageArray = {R.mipmap.a01,R.mipmap.b02,R.mipmap.c03,R.mipmap.d04,R.mipmap.e05};
    private static int i = 0;
    public ProvinceAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resouceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder = new ViewHolder();
        String s=getItem(position);
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resouceId,null);
            viewHolder.provinceImage = (ImageView)view.findViewById(R.id.list_item_image_view);
            viewHolder.provinceName = (TextView)view.findViewById(R.id.List_item_text_view);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.provinceImage.setImageResource(imageArray[(++i)%5]);
        viewHolder.provinceName.setText(s);
        return view;
    }

    class ViewHolder{
        ImageView provinceImage;
        TextView provinceName;
    }

}
