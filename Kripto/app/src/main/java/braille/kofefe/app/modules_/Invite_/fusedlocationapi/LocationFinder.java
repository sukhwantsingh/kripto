package braille.kofefe.app.modules_.Invite_.fusedlocationapi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import braille.kofefe.app.modules_.Invite_.CreateProfileScreen;
import braille.kofefe.app.supports_.SessionKofefeApp;
import braille.kofefe.app.supports_.UiHandleMethods;


/**
 * Created by Snow-Dell-07 on 4/20/2017.
 */

public class LocationFinder implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Activity mContext;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient = null;
    private SessionKofefeApp mSession;
    private UiHandleMethods uihandle;
    private CreateProfileScreen mActivityCreateProfile;

    public LocationFinder(Activity mContext) {

        this.mContext = mContext;
        uihandle = new UiHandleMethods(mContext);
        mSession = new SessionKofefeApp(mContext);

        if (mContext instanceof CreateProfileScreen) {
            mActivityCreateProfile = new CreateProfileScreen();
        }

        initClient();
    }

    public void initClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //    InitialValueSetUp.mConectivityCheck = true;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.e("LCheck", "" + mLastLocation);

        if (mLastLocation != null) {

            //  pdLocationDialog.hide();
            ReservedLocation.getSingletonInstance().setCurret_lat(Double.toString(mLastLocation.getLatitude()));
            ReservedLocation.getSingletonInstance().setCurrent_lng(Double.toString(mLastLocation.getLongitude()));

            Log.e("Lon", "" + ReservedLocation.getSingletonInstance().getCurrent_lng());
            Log.e("Lat", "" + ReservedLocation.getSingletonInstance().getCurret_lat());

            try {

             /*   //    Set Detect Location of Current Device
                if (ReservedLocation.getSingletonInstance().getCurret_lat() != null) {
                    mActivityCreateProfile.getGeocodeAddress(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }*/

            } catch (Exception e) {
                //    pdLocationDialog.hide();
                e.printStackTrace();
            }
        } else {
           /* if (PreLoginActivity.mLocationDialog.isShowing()) {
                PreLoginActivity.mLocationDialog.dismiss();
            }*/
            //     startApiClient();
        }
    }

    public void refreshScreen() {

        Intent intent = mContext.getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        mContext.finish();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(mContext, "Connection Failed: " + connectionResult, Toast.LENGTH_SHORT).show();
    }

    //  private ProgressDialog pdLocationDialog;
    public void startApiClient() {

        mGoogleApiClient.connect();
    /*    pdLocationDialog = ProgressDialog.show(mContext,"Please wait","Location is being fetched",false,false);*/
        Log.d("ajeeb", "onCall");
    }

    public void stopApiClient() {
        mGoogleApiClient.disconnect();

    }


    @Override
    public void onLocationChanged(Location location) {

        ReservedLocation.getSingletonInstance().setCurret_lat(Double.toString(location.getLatitude()));
        ReservedLocation.getSingletonInstance().setCurrent_lng(Double.toString(location.getLongitude()));
    }
}
