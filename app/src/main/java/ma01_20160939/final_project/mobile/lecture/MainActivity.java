package ma01_20160939.final_project.mobile.lecture;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view)  {
        Intent intent = null;
        switch (view.getId())   {
            case R.id.btnRestaurant:
                intent = new Intent(MainActivity.this, RestaurantActivity.class);
                break;
            case R.id.btnReview:
                intent = new Intent(MainActivity.this, ReviewActivity.class);
                break;
        }

        if (intent != null)
            startActivity(intent);
    }
}
