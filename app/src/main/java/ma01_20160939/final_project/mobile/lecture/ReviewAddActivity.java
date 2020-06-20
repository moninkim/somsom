package ma01_20160939.final_project.mobile.lecture;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviewAddActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 200;
    private String mCurrentPhotoPath;

    ImageView ivPhoto;
    EditText etTitle;
    EditText etContent;

    ReviewDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_add);

        helper = new ReviewDBHelper(this);

        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);
        etTitle = (EditText)findViewById(R.id.etTitle);
        etContent = (EditText)findViewById(R.id.etContent);

        ivPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)   {
                    dispatchTakePictureIntent();
                    return true;
                }
                return false;
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId())  {
            case R.id.btnSave:
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();

                if (title.equals("") || content.equals(""))    {
                    Toast.makeText(this,"다시 입력하세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues value = new ContentValues();
                value.put(helper.COL_TITLE, title);
                value.put(helper.COL_CONTENT, content);
                value.put(helper.COL_PATH, mCurrentPhotoPath);

                long count = db.insert(ReviewDBHelper.TABLE_NAME, null, value);

                if(count > 0)   {
                    setResult(RESULT_OK, null);
                    helper.close();
                    finish();
                }   else    {
                    Toast.makeText(this, "후기 추가 실패", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnCancel2:
                setResult(RESULT_CANCELED);
                finish();
                break;

        }
    }

    private void dispatchTakePictureIntent()    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }catch (IOException e)  {
                e.printStackTrace();
            }

            if (photoFile != null)  {
                Uri photoURI = FileProvider.getUriForFile(this, "ma01_20160939.final_project.mobile.lecture.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath(); //db에 저장해야 할 것
        return image;
    }
    private void setPic() {
        // Get the dimensions of the View
        int targetW = ivPhoto.getWidth();
        int targetH = ivPhoto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        ivPhoto.setImageBitmap(bitmap);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }
}
