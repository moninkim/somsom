package ma01_20160939.final_project.mobile.lecture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ReviewActivity extends AppCompatActivity {
    final int REQ_CODE = 100;
    ListView listview;
    ReviewDBHelper helper;
    Cursor cursor;
    ReviewAdapter reviewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        listview = (ListView)findViewById(R.id.lvReview);

        helper = new ReviewDBHelper(this);

        reviewAdapter = new ReviewAdapter(this, R.layout.listview_review_layout, null);
        listview.setAdapter(reviewAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final long ID = id;
                Intent intent = new Intent(ReviewActivity.this, ReviewShowActivity.class);
                intent.putExtra("_id", ID);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final long ID = id;

                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                builder.setTitle("삭제 확인")
                        .setMessage("삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = helper.getWritableDatabase();
                                String whereClause = "_id=?";
                                String[] whereArgs = new String[] {String.valueOf(ID)};
                                db.delete(ReviewDBHelper.TABLE_NAME, whereClause, whereArgs);
                                finish();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();
                return true;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu)   {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onMenuItemClick(MenuItem item)  {
        switch (item.getItemId())   {
            case R.id.item1:
                Intent intent = new Intent(ReviewActivity.this, ReviewAddActivity.class);
                startActivityForResult(intent, REQ_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE) {
            switch(resultCode)  {
                case RESULT_OK:
                    Toast.makeText(this, "추가 완료", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "추가 취소", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db =helper.getReadableDatabase();
        cursor = db.rawQuery("select * from " + ReviewDBHelper.TABLE_NAME, null);

        reviewAdapter.changeCursor(cursor);
        helper.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
    }
}
