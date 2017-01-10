package ca.uwaterloo.grouptalk;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {
    TextView hello;
    ImageButton game, music, color, run;
    Button logout;
    String userId;
    ImageView smileFace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        smileFace = (ImageView) findViewById(R.id.smile_face);
        smileFace.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.smile,35, 35));
        hello = (TextView) findViewById(R.id.hello);
        game = (ImageButton) findViewById(R.id.game);
        game.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.game_fan_1,100, 80));
        music = (ImageButton) findViewById(R.id.music);
        music.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.music1,100, 80));
        color = (ImageButton) findViewById(R.id.color);
        color.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.color,100, 80));
        run = (ImageButton) findViewById(R.id.run);
        run.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.running_man,100, 80));
        logout = (Button) findViewById(R.id.buttonLogout);

        Intent myIntent = getIntent();
        userId = myIntent.getStringExtra("userId");
        hello.setText(getString(R.string.hello1,userId));

        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(v.getContext(),TalkActivity.class);
                myIntent1.putExtra("userId",userId);
                myIntent1.putExtra("groupName","game");
                startActivity(myIntent1);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(v.getContext(),TalkActivity.class);
                myIntent1.putExtra("userId",userId);
                myIntent1.putExtra("groupName","music");
                startActivity(myIntent1);
            }
        });

        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(v.getContext(),TalkActivity.class);
                myIntent1.putExtra("userId",userId);
                myIntent1.putExtra("groupName","color");
                startActivity(myIntent1);
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent1 = new Intent(v.getContext(),TalkActivity.class);
                myIntent1.putExtra("userId",userId);
                myIntent1.putExtra("groupName","run");
                startActivity(myIntent1);
            }
        });

       logout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent myIntent1 = new Intent(v.getContext(),MainActivity.class);
               startActivity(myIntent1);
           }
       });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
