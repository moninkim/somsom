package ma01_20160939.final_project.mobile.lecture;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ReviewShowActivity extends AppCompatActivity {
    final static String TAG = "ShowMemoActivity";

    ReviewDBHelper helper;
    ImageView ivPhoto;
    TextView tvTitle;
    TextView tvContent;
    Bitmap bitmap;
    int id;
    String path, title, content;
    private static final int REQUEST_PERMISSION_CODE = 2;
    private static final int GALLERY_CODE = 3;
    private static final int REQUEST_EXTERNAL_STORAGE_CODE=1;
    boolean permissionCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_show);

        Intent intent = getIntent();
        long _id = intent.getLongExtra("_id", 0);
        helper = new ReviewDBHelper(this);

        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
        ivPhoto = findViewById(R.id.ivPhoto);

        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = {"_id", ReviewDBHelper.COL_PATH, ReviewDBHelper.COL_TITLE, ReviewDBHelper.COL_CONTENT};
        String selection = "_id=?";
        String[] selectionArgs = new String[] {String.valueOf(_id)};

        Cursor cursor = db.query(ReviewDBHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null, null);

        while (cursor.moveToNext()) {
            ReviewDto dto = new ReviewDto();
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            path = cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_PATH));
            title = cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_TITLE));
            content = cursor.getString(cursor.getColumnIndex(ReviewDBHelper.COL_CONTENT));
        }
        tvTitle.setText(title);
        tvContent.setText(content);
        bitmap = BitmapFactory.decodeFile(path);
        ivPhoto.setImageBitmap(bitmap);

        cursor.close();
    }

    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.btnClose:
                finish();
                break;

        }
    }

    public boolean onCreateOptionsMenu(Menu menu)   {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    public void onMenuItemClick(MenuItem item)  {
        switch (item.getItemId())   {
            case R.id.item_tweet:
                sharedTwitter();
                break;
            case R.id.item_insta:
                sharedInstagram();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)    {
            case REQUEST_EXTERNAL_STORAGE_CODE:
                for (int i=0; i<permissions.length; i++)    {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE))   {
                        if (grantResult == PackageManager.PERMISSION_GRANTED)   {
                            Toast.makeText(this, "허용", Toast.LENGTH_SHORT).show();
                            permissionCheck=true;
                        } else  {
                            Toast.makeText(this, "허용되지않음", Toast.LENGTH_SHORT).show();
                            permissionCheck=false;
                        }
                    }
                }
                break;
        }
    }

    public void onRequestPermission()
    {
        int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionReadStorage == PackageManager.PERMISSION_DENIED
                || permissionWriteStorage == PackageManager.PERMISSION_DENIED)  {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_CODE);
        }   else    {
            permissionCheck = true;
        }
    }

    public void sharedInstagram()
    {
        onRequestPermission();

        if (permissionCheck) {
            String storage = Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "이미지명.png";

            String folderName = "/폴더명/";
            String fullPath = storage + folderName;
            File filePath;

            try {
                filePath = new File(fullPath);
                if (!filePath.isDirectory())
                    filePath.mkdirs();
                FileOutputStream fos = new FileOutputStream(fullPath + fileName);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            Uri uri = Uri.fromFile(new File(fullPath, fileName));
            try {
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_TEXT, "텍스트는 지원하지 않음");
                share.setPackage("com.instagram.android");
                startActivity(share);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "인스타그램이 설치되어있지 않습니다", Toast.LENGTH_SHORT).show();
            } catch (Exception e)   {
                e.printStackTrace();
            }
        }
    }



//    void sharedInstagram() {
//        selectGallery();
//    }
//
//    private void selectGallery() {
//
//        int permissionCheck = ContextCompat.checkSelfPermission(ReviewShowActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (PackageManager.PERMISSION_GRANTED == permissionCheck) {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
//            startActivityForResult(intent, GALLERY_CODE);
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    REQUEST_PERMISSION_CODE);
//        }
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case GALLERY_CODE:
//                    shareImage(data.getData());
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    private void shareImage(Uri imgUri) {
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_STREAM, imgUri);
//        intent.setPackage("com.instagram.android");
//        startActivity(intent);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        switch (requestCode) {
//            case REQUEST_PERMISSION_CODE:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //동의 했을 경우
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(intent, GALLERY_CODE);
//                } else {
//                    //거부했을 경우
//                    Toast toast = Toast.makeText(this, "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//
//                break;
//        }
//    }

    public void sharedTwitter() {
        String strLink = null;
        try {
            strLink = String.format("http://twitter.com/intent/tweet?text=%s",
                    URLEncoder.encode(tvContent.getText().toString(), "utf-8"));

        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strLink));
        startActivity(intent);
    }


}
