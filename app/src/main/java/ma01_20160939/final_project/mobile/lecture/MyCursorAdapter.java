package ma01_20160939.final_project.mobile.lecture;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MyCursorAdapter extends android.widget.CursorAdapter {

    LayoutInflater inflater;
    Cursor cursor;
    int layout;

    public MyCursorAdapter(Context context, int layout, Cursor c) {
        super(context, c, android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = c;
        layout = this.layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView)view.findViewById(R.id.tvName);
//        TextView tvHour = (TextView)view.findViewById(R.id.tvHour);
//        TextView tvPhone = (TextView)view.findViewById(R.id.tvPhone);

        tvName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COL_NAME)));
//        tvHour.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COL_HOUR)));
//        tvPhone.setText(cursor.getString(cursor.getColumnIndex(DBHelper.COL_PHONE)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItemLayout = inflater.inflate(R.layout.listview_layout, parent, false);
        return listItemLayout;
    }
}
