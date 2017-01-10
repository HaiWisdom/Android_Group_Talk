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

public class MainActivity extends AppCompatActivity {
    EditText loginUserId, loginPassword;
    Button buttonLogin, buttonRegister;
    TextView warning;

    private class MyRunnable implements Runnable{
        private String passwordFromServer;
        private String password;
        private String userId;

        MyRunnable(String passwordFromServer, String password, String userId){
            this.passwordFromServer = passwordFromServer;
            this.password = password;
            this.userId = userId;
        }

        @Override
        public void run() {
            if (passwordFromServer.equals(password)) {
                Intent myIntent = new Intent(getApplicationContext(), GroupActivity.class);
                myIntent.putExtra("userId", userId);
                startActivity(myIntent);
            } else {
                warning.setText(getString(R.string.incorrectUserID));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        loginUserId = (EditText) findViewById(R.id.loginUserId);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        warning = (TextView) findViewById(R.id.warning);
        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String userId = loginUserId.getText().toString();
                        String password = loginPassword.getText().toString();
                        ClientX clientx = new ClientX();
                        MessageService.Client mClient = clientx.getThriftClient();
                        if (mClient == null) {
                            warning.post(new Runnable() {
                                @Override
                                public void run() {
                                    warning.setText(getString(R.string.networkError));
                                }
                            });
                        }
                        else {
                            try {
                                String passwordFromServer = mClient.loginX(userId);
                                runOnUiThread(new MyRunnable(passwordFromServer, password, userId));
                            } catch (TException e) {
                                Log.e(TAG, "Login_getThriftAccount: ", e);
                            }
                        }
                    }
                }).start();
            }
        });
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),RegisterActivity.class);
                startActivity(myIntent);
            }
        });


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
