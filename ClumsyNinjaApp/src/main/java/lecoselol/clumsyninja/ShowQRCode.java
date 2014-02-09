package lecoselol.clumsyninja;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.loopj.android.image.SmartImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowQRCode extends Activity
{
    SmartImageView qrImage;

    final static String urlGoogleChart = "http://chart.apis.google.com/chart?chs=200x200&cht=qr&amp;chl=";
    String urlMySite;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qrcode);

        qrImage = (SmartImageView)findViewById(R.id.imgQr);

        Intent intent;
        intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        Log.e("TYPE SHARED", type);
        if (Intent.ACTION_SEND.equals(action) && type != null)
        {
            if ("text/plain".equals(type))
            {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null)
                {
                    handleTextShareAction(sharedText);
                }
            }
        }
    }

    public void handleTextShareAction(String s)
    {
        Log.e("QR", "handle");
        creaCodiceQr(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public boolean isOnline()
    {
        Log.e("QR", "online");
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            Log.e("QR", "connesso");
            return true;
        }
        else
        {
            Log.e("QR", "non connesso");
            Toast.makeText(getApplicationContext(), "Non sei connesso ad internet!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean controlloStringa(String url)
    {
        Log.e("QR", "controllo stringa");
        if (url.trim().length() > 0)
        {
            Log.e("QR", "ok");
            return true;
        }
        else
        {
            Log.e("QR", "no");
            Toast.makeText(getApplicationContext(), "Non hai digitato nulla", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void creaCodiceQr(String testo)
    {
        if (isOnline() && controlloStringa(testo))
        {
            //qrImage.setImageUrl(urlGoogleChart+urlMySite);
            final Handler handler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {
                    if (msg.what == 1)
                    {
                        qrImage.setImageBitmap((Bitmap)msg.obj);
                    }
                    super.handleMessage(msg);
                }
            };

            Thread thread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Bitmap img = getBitmapFromURL(urlGoogleChart + urlMySite);
                    img = replaceColor(img, -1, 15658734);
                    Message m = new Message();
                    m.what = 1;
                    m.obj = img;
                    handler.sendMessage(m);
                }
            });
            thread.start();
        }
    }

    public static Bitmap getBitmapFromURL(String src)
    {
        try
        {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap replaceColor(Bitmap src, int fromColor, int targetColor)
    {
        if (src == null)
        {
            return null;
        }
        // Source image sie 
        int width = src.getWidth();
        int height = src.getHeight();
        int[] pixels = new int[width * height];
        //get pixels
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int x = 0; x < pixels.length; ++x)
        {
            pixels[x] = (pixels[x] == fromColor) ? targetColor : pixels[x];
            Log.e("COLORE", pixels[x] + "");
        }
        // create result bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());
        //set pixels
        result.setPixels(pixels, 0, width, 0, 0, width, height);

        return result;
    }
}

