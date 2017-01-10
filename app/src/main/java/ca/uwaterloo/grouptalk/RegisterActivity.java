package ca.uwaterloo.grouptalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.thrift.TException;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {
    EditText registerUserId, registerPassword;
    Button registerButton;
    TextView registerSucceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        registerUserId = (EditText) findViewById(R.id.registerUserId);
        registerPassword = (EditText) findViewById(R.id.registerPassword);
        registerSucceed = (TextView) findViewById(R.id.registerSucceed);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String userId = registerUserId.getText().toString();
                        String password = registerPassword.getText().toString();
                        ClientX clientx = new ClientX();

                        MessageService.Client mClient = clientx.getThriftClient();
                        if (mClient == null) {
                            registerSucceed.post(new Runnable() {
                                @Override
                                public void run() {
                                    registerSucceed.setText(getString(R.string.networkError));
                                }
                            });
                        }
                        else {
                            try {
                                mClient.registerX(userId, password);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registerSucceed.setText(getString(R.string.registerSuccess));
                                        try {
                                            Thread.sleep(300);
                                        }catch (InterruptedException e){}
                                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(myIntent);
                                    }
                                });
                            } catch (TException e) {
                                Log.e(TAG, "Login_getThriftAccount: ", e);
                            }
                        }
                    }
                }).start();
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

}
