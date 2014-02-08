package lecoselol.clumsyninja;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PinActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pin, container, false);
            PinPadListener l = new PinPadListener();

            // I don't remember the fucking view IDs LOL
            ViewGroup rootGroup = (ViewGroup) rootView;
            int count = rootGroup.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = rootGroup.getChildAt(i);
                if (v instanceof TextView) {
                    v.setOnClickListener(l);
                }
            }

            return rootView;
        }

        public void addNumber(char num) {

        }

        public class PinPadListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // WTF now I'm hating my smartassness
                    case R.id.txt_one:
                        addNumber('1');
                    case R.id.txt_due:
                        addNumber('2');
                    case R.id.txt_troix:
                        addNumber('3');
                    case R.id.txt_vier:
                        addNumber('4');
                    case R.id.txt_cinco:
                        addNumber('5');
                    case R.id.txt_kuusi:
                        addNumber('6');
                    case R.id.txt_zeven:
                        addNumber('7');
                    case R.id.txt_oito:
                        addNumber('8');
                    case R.id.txt_dziewięć:
                        addNumber('9');
                    case R.id.txt_null:
                        addNumber('0');
                }
            }
        }

    }
}
