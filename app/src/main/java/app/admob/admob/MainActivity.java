package screen.com.multiplesizes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import screen.com.multiplesizes.R;

public class MainActivity extends AppCompatActivity {
    // Remove the below line after defining your own ad unit ID.
    private static final String TOAST_TEXT = "Publicidades van a ser mostradas";

    private static final int START_LEVEL = 1;
    private int mLevel;
    private Button mNextLevelButton;
    private InterstitialAd mInterstitialAd;
    private TextView mLevelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the next level button, which tries to show an interstitial when clicked.
        mNextLevelButton = ((Button) findViewById(R.id.next_level_button));
        mNextLevelButton.setEnabled(false);
        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
            }
        });

        // Create the text view to show the level number.
        mLevelTextView = (TextView) findViewById(R.id.level);
        mLevel = START_LEVEL;

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();

        // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show();
    }


    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mNextLevelButton.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Toast.makeText(this, "NO pudo ser leida", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the next level button and load the ad.
        mNextLevelButton.setEnabled(true);
        AdRequest request = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                /*.addTestDevice("D9B26731969B87BBCF8213A11376DCBF")  // S4
                .addTestDevice("B679BA94D3C1FBCB5A6C3E5092356841")  // Moto X
                .addTestDevice("6391363B6002FC9201B17FCE94A0D1A4")  // Nexus 7*/
                .build();
        mInterstitialAd.loadAd(request);

        /*AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);*/

        /*AdRequest.Builder.addTestDevice("D9B26731969B87BBCF8213A11376DCBF");*/

    }

    private void goToNextLevel() {
        // Show the next level and reload the ad to prepare for the level after.
        mLevelTextView.setText("$0.0 " + (++mLevel) + "Generados ");
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }
}
