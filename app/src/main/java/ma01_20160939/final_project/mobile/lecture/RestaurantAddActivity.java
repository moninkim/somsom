package ma01_20160939.final_project.mobile.lecture;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RestaurantAddActivity extends AppCompatActivity {

    EditText etName;
    EditText etHour;
    EditText etPhone;
    DBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = findViewById(R.id.etName);
        etHour = findViewById(R.id.etHour);
        etPhone = findViewById(R.id.etPhone);

        helper = new DBHelper(this);
    }

    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.btnAdd:

                String name = etName.getText().toString();
                String hour = etHour.getText().toString();
                String phone = etPhone.getText().toString();

                if (name.equals("") || hour.equals(""))    {
                    Toast.makeText(this,"다시 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues value = new ContentValues();
                value.put(helper.COL_NAME, name);
                value.put(helper.COL_HOUR, hour);
                value.put(helper.COL_PHONE, phone);

                long count = db.insert(DBHelper.TABLE_NAME, null, value);

                if(count > 0)   {
                    setResult(RESULT_OK, null);
                    helper.close();
                    finish();
                }   else    {
                    Toast.makeText(this, "새로운 식당 추가 실패", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel:
                setResult(RESULT_CANCELED);
                finish();
                break;

        }
    }
}
