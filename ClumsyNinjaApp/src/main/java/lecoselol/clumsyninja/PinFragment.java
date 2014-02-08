package lecoselol.clumsyninja;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by rock3r on 08/02/2014.
 */
public class PinFragment extends Fragment {

    private TextView mPinText;

    private String mPin;

    public PinFragment() {
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
            if (v instanceof Button) {
                v.setOnClickListener(l);
            }
        }

        mPinText = (TextView) rootGroup.findViewById(R.id.txt_pin);
        mPinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNumber();
            }
        });

        return rootView;
    }

    public void addNumber(char num) {
        mPin = mPin + num;
        mPinText.setText(mPin);
    }

    public void removeNumber() {
        if (!TextUtils.isEmpty(mPin)) {
            mPin = mPin.substring(0, mPin.length() - 1);
            mPinText.setText(mPin);
        }
    }

    public String getPin() {
        return mPin;
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
