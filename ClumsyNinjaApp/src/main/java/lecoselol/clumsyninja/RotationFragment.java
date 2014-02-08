package lecoselol.clumsyninja;

import android.app.Fragment;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import lecoselol.clumsyninja.figherie.CosiGirevoli;

/**
 * Where rotating things are.
 */
public class RotationFragment extends Fragment implements NinjaSensor.RotationListener
{
    private static final float ANGLE = (float)Math.toRadians(30);

    private CosiGirevoli cosiGirevoli;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootto = inflater.inflate(R.layout.fragment_rotation, container, false);
        // ah ah ah such a funny name
        cosiGirevoli = (CosiGirevoli)rootto.findViewById(R.id.i_cosi_girano);
        return rootto;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        cosiGirevoli = null;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        NinjaSensor.initialize(getActivity());
        NinjaSensor.registerListener(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        NinjaSensor.unregisterListener(this);
        initialAngles = null;
    }

    private float[] initialAngles = null;

    @Override
    public void onRotationChanged(float[] rotationVector)
    {
        if (initialAngles == null)
        {
            initialAngles = rotationVector;
            return;
        }

        //final float angleZ = rotationVector[0] - initialAngles[0]; // YOU ARE SO SADLY USELESS
        //final float angleX = rotationVector[1] - initialAngles[1]; // YOU TOO, BUT I LIKE YOU
        final float angleY = rotationVector[2] - initialAngles[2];
        final float position = angleY/ANGLE;
        if (position < 0) initialAngles = rotationVector;
        else if (position > 1)
        {
            initialAngles[0] = rotationVector[0] - ANGLE;
            initialAngles[1] = rotationVector[1] - ANGLE;
            initialAngles[2] = rotationVector[2] - ANGLE;
        }
        cosiGirevoli.setPlaybackPosition(position);
        if (position >= 1)
        {
            NinjaVibrator.initalize(getActivity());
            NinjaVibrator.vibrate(10);

            Thread stealthyPlayer = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("LOL I DONT KNOW WHAT IM DOING HERE");
                    MediaPlayer mp = new MediaPlayer();
                    AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.alarm);
                    try {
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mp.prepare();
                        mp.start();
                        afd.close();
                    }
                    catch (Exception e) {
                        // FANCULO
                    }

                }
            });
            stealthyPlayer.setName("FUCK YOU DORPHIIIIINSSSS");
            stealthyPlayer.start();    // This is a badass thread we have here guys

            getFragmentManager().popBackStack();  // TODO: Eugenio fai le cose che io qui non coso le cose
        }
    }
}
