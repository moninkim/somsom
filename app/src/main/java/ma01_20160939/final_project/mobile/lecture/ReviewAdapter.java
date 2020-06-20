package ma01_20160939.final_project.mobile.lecture;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ReviewAdapter extends CursorAdapter {

    LayoutInflater inflater;
    Cursor cursor;
    int layout;

    public ReviewAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        cursor = c;
        layout = this.layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTitle = (TextView)view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView)view.findViewById(R.id.tvContent);

        tvTitle.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_TITLE)));
        tvContent.setText(cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_CONTENT)));

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View listItemLayout = inflater.inflate(R.layout.listview_review_layout, parent, false);
        return listItemLayout;
    }
}
