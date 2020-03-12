package edu.illinois.cs.cs125.spring2020.mp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Represents the launch screen of the app, where the user can login to games.
 */
public class LaunchActivity extends AppCompatActivity {

    /** Discretional constant that functions as sign-in request code. */
    private static final int RC_SIGN_IN = 123;

    /**
     * Called by the Android system when the activity is to be set up.
     * @param savedInstanceState info from the previously terminated instance (unused)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            createSignInIntent();
        }
    }

    /**
     * Starts an intent to log-in.
     */
    public void createSignInIntent() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .build(),
                RC_SIGN_IN);
    }
    /**
     * Checks log-in input with request and result code.
     * @param requestCode request code constant that is used to compare against sign-in constant
     * @param resultCode integer output that results from sign-in intent method
     * @param data Intent to be passed to activity
     */
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                TextView loginVariable = findViewById(R.id.goLogin);
                loginVariable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        loginVariable.setText("Retry Login");
                        createSignInIntent();
                    }
                });
            }
        }
    }
}
