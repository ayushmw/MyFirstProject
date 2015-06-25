//check if already signed in, first_time_check()
//load profile image
//check if want to add revokeAccess button or not
package com.example.ayush.customerapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

public class SignInActivity extends Activity implements OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 0;
    // Google client to communicate with Google
    private GoogleApiClient mGoogleApiClient;
    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private ImageView image;
    private RelativeLayout signinFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button signinButton = (Button) findViewById(R.id.signin);
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        TextView keopay = (TextView) findViewById(R.id.keopay);
        keopay.setTypeface(tf1);

        Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        TextView eko = (TextView) findViewById(R.id.eko);
        eko.setTypeface(tf2);

        Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        TextView powered = (TextView) findViewById(R.id.powered);
        powered.setTypeface(tf3);

        TextView doublecontent = (TextView) findViewById(R.id.doubleContent);
        doublecontent.setTypeface(tf2);

        signinButton.setTypeface(tf2);


        signinFrame = (RelativeLayout) findViewById(R.id.signinFrame);
        signinButton.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();

    }

    /* private void first_time_check() {
         //save value in database and if activity opened next time, check the value fro database that
         // whether user is already logged in or not.
         if (username != null) {
             Intent intent2 = new Intent(SignInActivity.this, SavedCards.class);
             startActivity(intent2);
         }
     }*/
    protected void onStart() {
        super.onStart();
        Log.v("123Start is executed", "1");
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            Log.v("123Stop is executed", "2");
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                Log.v("123NoException ", "3");
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                Log.v("123Exception is caught", "4");
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            Log.v("ResolutionFalse", "5");
            return;
        }
        if (!mIntentInProgress) {
            Log.v("IntentFalse", "6");
            // store mConnectionResult
            mConnectionResult = result;
            if (signedInUser) {
                resolveSignInError();
                Log.v("ResolveSignIn", "7");
            }
        }
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (responseCode != RESULT_OK) {
                    signedInUser = false;
                }
                if (responseCode == RESULT_OK) {
                    new MaterialDialog.Builder(this)
                            .content("Please Wait")
                            .progress(true, 0)
                            .widgetColorRes(R.color.md_widget_color)
                            .show();
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    public void onConnected(Bundle arg0) {
        signedInUser = false;
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Log.v("Person is not null", "4");
        }
        getProfileInformation();
    }


    private void updateProfile(boolean isSignedIn) {
        if (!isSignedIn) {
            signinFrame.setVisibility(View.VISIBLE);
        }
    }

    private void getProfileInformation() {
        try {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    String personName = currentPerson.getDisplayName();
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    Log.v("tag", personName);
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    //new LoadProfileImage(image).execute(personPhotoUrl);
                    Intent intent2 = new Intent(SignInActivity.this, MobileNumberVerification.class);
                    intent2.putExtra("personName", personName);
                    intent2.putExtra("email", email);
                    startActivity(intent2);
                    updateProfile(true);
                } else {
                    Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
        updateProfile(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                googlePlusLogin();
                break;
        }
    }

    public void logout(View v) {
        googlePlusLogout();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void googlePlusLogin() {
        if (isNetworkAvailable()) {
            if (!mGoogleApiClient.isConnecting()) {
                signedInUser = true;
                resolveSignInError();
            }
        } else {
            new MaterialDialog.Builder(this)
                    .title("No Internet Connection")
                    .content("Please Turn On Your internet Connection")
                    .positiveText("Ok")
                    .callback(new MaterialDialog.ButtonCallback() {
                                  @Override
                                  public void onPositive(MaterialDialog dialog) {
                                      dialog.dismiss();
                                  }
                              }
                    )
                    .show();
        }
    }

    private void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
            updateProfile(false);
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView downloadedImage;

        public LoadProfileImage(ImageView image) {
            this.downloadedImage = image;
        }

        protected Bitmap doInBackground(String... urls) {

            String url = urls[0];
            Bitmap icon = null;

            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(getResources(), result);
            img.setCornerRadius(10);
            downloadedImage.setImageDrawable(img);
        }
    }
}
