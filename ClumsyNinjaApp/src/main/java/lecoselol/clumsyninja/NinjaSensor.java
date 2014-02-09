package lecoselol.clumsyninja;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.HashMap;
import java.util.Map;

import static android.hardware.Sensor.TYPE_ROTATION_VECTOR;
import static android.hardware.SensorManager.SENSOR_DELAY_GAME;

public final class NinjaSensor {
    public static final int SENSOR_TYPE = TYPE_ROTATION_VECTOR;
    public static final int SENSOR_DELAY = SENSOR_DELAY_GAME;

    private static SensorManager sensorManager;
    private static Sensor sensor;

    private static Map<RotationListener, SensorEventListener> listeners;

    private NinjaSensor() {} // NEVAH FUCKIN' INSTANCIATE ME

    /**
     * Initialize the sensor.
     * <p>
     * Must be called before {@link #registerListener(lecoselol.clumsyninja.NinjaSensor.RotationListener)}!
     * </p>
     *
     * @param context any context. Really. We need it. Pleeeeeease. GIMME DA FUKIN' CONTEXT.
     *
     * @return {@code true} if the sensor is supported and successfully enabled.
     */
    public static boolean initialize(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(SENSOR_TYPE);
        if (sensor == null) {
            destroy();
            return false;
        }
        listeners = new HashMap<>(1);
        return true;
    }

    /**
     * Release <strike>The Kraken</strike> resources.
     */
    public static void destroy() {
        // unregister all listeners
        if (listeners != null) {
            for (RotationListener listener : listeners.keySet()) {
                unregisterListener(listener);
            }
        }
        // free all references
        sensorManager = null;
        sensor = null;
        listeners = null;
    }

    /**
     * Registers a {@link SensorEventListener} for the sensor.
     * <p>
     * Must be called after {@link #initialize(android.content.Context)}!
     * </p>
     *
     * @return {@code true} if the sensor is supported and successfully enabled.
     */
    public static boolean registerListener(RotationListener listener) {
        if (sensorManager != null && sensor != null && listeners != null) {
            final SensorEventListenerWrapper wrapper = new SensorEventListenerWrapper(listener);
            listeners.put(listener, wrapper);
            return sensorManager.registerListener(wrapper, sensor, SENSOR_DELAY);
        }
        else {
            return false;
        }
    }

    /**
     * Unregisters a {@link SensorEventListener} for the sensor.
     */
    public static void unregisterListener(RotationListener listener) {
        if (sensorManager != null && sensor != null && listeners != null) {
            final SensorEventListenerWrapper wrapper = (SensorEventListenerWrapper) listeners.get(listener);
            if (wrapper != null) sensorManager.unregisterListener(wrapper, sensor);
        }
    }

    public interface RotationListener {
        public void onRotationChanged(float[] rotationVector);
    }

    private static final class SensorEventListenerWrapper implements SensorEventListener {
        private final RotationListener listener;

        public SensorEventListenerWrapper(RotationListener listener) {
            this.listener = listener;
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            final float[] R = new float[9];
            SensorManager.getRotationMatrixFromVector(R, sensorEvent.values);
            final float[] angles = new float[3];
            SensorManager.getOrientation(R, angles);
            listener.onRotationChanged(filter(angles));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            // do nothing
        }

        // Why didn't I use a Queue? Because I dig efficency... ** SO COOL **
        private final float[][] values = new float[5][];
        private int valuesIndex = 0;

        public float[] filter(float[] angles) {
            values[valuesIndex++] = angles;
            if (valuesIndex >= values.length) valuesIndex = 0;
            final float[] averaged = new float[3];
            int vivident = 0; // should have been DIVIDEND, but, like, whatever
            for (int i = values.length - 1; i >= 0; i--) // backwards, like Michael Jackson!
            {
                final float[] value = values[i];
                if (value != null) {
                    averaged[0] += value[0];
                    averaged[1] += value[1];
                    averaged[2] += value[2];
                    vivident++;
                }
            }
            averaged[0] /= vivident;
            averaged[1] /= vivident;
            averaged[2] /= vivident;
            return averaged;
        }
    }
}
