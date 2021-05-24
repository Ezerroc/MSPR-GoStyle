package com.example.codescannergostyle.util;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.codescannergostyle.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponsAdapter extends ArrayAdapter<Coupon> {

    private ArrayList<Coupon> listeCoupons;
    private Context mContext;
    //Used for filter
    private List<Coupon> tempItems, suggestions;

    public CouponsAdapter(@NonNull Context context, ArrayList<Coupon> coupons) {
        super(context, 0, coupons);
        this.listeCoupons = coupons;
        mContext = context;
        tempItems = new ArrayList<Coupon>(listeCoupons); // this makes the difference.
        suggestions = new ArrayList<Coupon>();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Coupon coupon = listeCoupons.get(position);

        // Check if an existing view is being reused, otherwise inflate the view
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.coupon_item, parent, false);
        }

        // Lookup view for data population
        TextView codeT = listItem.findViewById(R.id.code);
        TextView reducT = listItem.findViewById(R.id.reducT);
        TextView from = listItem.findViewById(R.id.fromT);
        TextView to = listItem.findViewById(R.id.toT);
        TextView desc = listItem.findViewById(R.id.descT);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date dateTo = sdf.parse(coupon.getDate_to());
            Date dateFrom = sdf.parse(coupon.getDate_from());



            from.setText(sdf2.format(dateFrom));
            to.setText(sdf2.format(dateTo));

            String reducS = coupon.getReduction_percent() != 0 ? "<b>-"+coupon.getReduction_percent()+"%</b>" : "<b>-"+coupon.getReduction_amount()+"€</b>";
            reducS += " de réduction, dès <b>"+ coupon.getMinimum_amount()+"€</b> d'achats";

            reducT.setText(Html.fromHtml(reducS));
        } catch (ParseException e) {
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        codeT.setText(coupon.getCode());
        desc.setText(coupon.getDescription());


        // Return the completed view to render on screen
        return listItem;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Coupon) resultValue).getCode();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Coupon coupon : tempItems) {
                    if (coupon.getCode().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(coupon);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Coupon> filterList = (ArrayList<Coupon>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Coupon coupon : filterList) {
                    add(coupon);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
