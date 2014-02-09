package lecoselol.clumsyninja;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lecoselol.clumsyninja.figherie.CosiGirevoli;

/**
 * Where rotating things are.
 */
public class RotationFragment extends Fragment implements NinjaSensor.RotationListener {
    private static final float ANGLE = (float) Math.toRadians(30);

    private CosiGirevoli cosiGirevoli;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootto = inflater.inflate(R.layout.fragment_rotation, container, false);
        // ah ah ah such a funny name
        cosiGirevoli = (CosiGirevoli) rootto.findViewById(R.id.i_cosi_girano);
        return rootto;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cosiGirevoli = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        finished = false;
        NinjaSensor.initialize(getActivity());
        NinjaSensor.registerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!finished) unregisterSensor();
    }

    private void unregisterSensor() {
        NinjaSensor.unregisterListener(this);
        initialAngles = null;
    }

    private float[] initialAngles = null;
    private boolean finished;

    @Override
    public void onRotationChanged(float[] rotationVector) {
        if (finished) return;

        if (initialAngles == null) {
            initialAngles = rotationVector;
            return;
        }

        //final float angleZ = rotationVector[0] - initialAngles[0]; // YOU ARE SO SADLY USELESS
        //final float angleX = rotationVector[1] - initialAngles[1]; // YOU TOO, BUT I LIKE YOU
        final float angleY = rotationVector[2] - initialAngles[2];
        final float position = angleY / ANGLE;
        if (position < 0) {
            initialAngles = rotationVector;
        }
        else if (position > 1) {
            //initialAngles[0] = rotationVector[0] - ANGLE;
            //initialAngles[1] = rotationVector[1] - ANGLE;
            initialAngles[2] = rotationVector[2] - ANGLE;
        }
        cosiGirevoli.setPlaybackPosition(position);
        if (position >= 1) {
            finished = true;
            unregisterSensor();

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    NinjaVibrator.initalize(getActivity());
                    NinjaVibrator.vibrate(10);

                    NinjaApplication.playSoundAsync(R.raw.alarm);

                    startActivity(new Intent(getActivity(), NoteListActivity.class));

                    return null;
                }
            }.execute();
        }
    }
}
