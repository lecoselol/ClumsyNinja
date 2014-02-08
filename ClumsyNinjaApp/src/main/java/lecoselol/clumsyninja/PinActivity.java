package lecoselol.clumsyninja;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;

public class PinActivity extends Activity implements PinFragment.OnPinChangedListener, FragmentManager
    .OnBackStackChangedListener {

    public static final String TEST_PIN = "0000";
    public static final String FRAG_ROTATION = "rotation";
    public static final String FRAG_PINPAD = "pinpad";
    private PinFragment mPinFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                                .add(R.id.container, new PinFragment(), "pinpad")
                                .commit();
        }

        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPinFrag = (PinFragment) getFragmentManager().findFragmentByTag(FRAG_PINPAD);
        assert mPinFrag != null;
        mPinFrag.setListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        forgetAboutThatPinFragmentDude();
    }

    private void forgetAboutThatPinFragmentDude() {
        if (mPinFrag != null) {
            mPinFrag.setListener(null);
            mPinFrag = null;
        }
    }

    @Override
    public void onPinChanged(String currentPin) {
        if (TEST_PIN.equals(currentPin)) {
            getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.fade_rotate_in,
                                                     R.animator.fade_rotate_out,
                                                     R.animator.fade_rotate_back_in,
                                                     R.animator.fade_rotate_back_out)
                                .replace(R.id.container, new RotationFragment(), FRAG_ROTATION)
                                .addToBackStack(null)
                                .commit();

            forgetAboutThatPinFragmentDude();

            boomBaby();
        }
    }

    private void boomBaby() {
        MediaPlayer mp = new MediaPlayer();
        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.boom);
        try {
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
            afd.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackStackChanged() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            mPinFrag = (PinFragment) getFragmentManager().findFragmentByTag(FRAG_PINPAD);
            assert mPinFrag != null;
            mPinFrag.setListener(this);
        }
    }
}
