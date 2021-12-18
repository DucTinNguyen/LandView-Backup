package com.example.landview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.landview.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter<WeatherModels> {
    private int layout;
    private Context context;
    private List<WeatherModels>list;
    public WeatherAdapter(@NonNull Context context, int resource, @NonNull List<WeatherModels> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layout = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layout,null);
        //ánh xạ view
        TextView txtDay = view.findViewById(R.id.txtday);
        TextView txtTemp = view.findViewById(R.id.txttempWeather);
        TextView txtDes = view.findViewById(R.id.txtWeatherDes);
        ImageView imgIcon = view.findViewById(R.id.Iconweather);

        WeatherModels weatherModel = list.get(position);

        txtDay.setText(weatherModel.getDay());
        txtDes.setText(weatherModel.getDescription());
        txtTemp.setText(weatherModel.getTemp()+"°C");
        Picasso.get().load(weatherModel.getUrlIcon()).into(imgIcon);
        return view;
    }
}
