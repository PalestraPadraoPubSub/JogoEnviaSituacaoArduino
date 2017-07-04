package agora.humano.seja.com.bis;

import android.hardware.SensorManager;

import org.cocos2d.nodes.CCDirector;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGSize;

/**
 * Created by ricardoogliari on 6/5/17.
 */

public class DeviceSettings {

    public static CGPoint screenResolution(CGPoint gcPoint) {
        return gcPoint;
    }

    public static float screenWidth() {
        return winSize().width;
    }

    public static float screenHeight() {
        return winSize().height;
    }

    public static CGSize winSize() {
        return CCDirector.sharedDirector().winSize();
    }

    private static SensorManager sensormanager;

    public static void setSensorManager(SensorManager sm) {
        sensormanager = sm;
    }
    public static SensorManager getSensormanager() {
        return sensormanager;
    }

}
