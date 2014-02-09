package lecoselol.clumsyninja;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity implements View.OnClickListener
{
    ImageView elSenorCardoCuandoSeHaceClicar;
    TextView  txtLeCoseLOL, txtSebastiano, txtEugenio, txtAntonio, txtRoberto;

    final static String[] messagesElCardo = { "¡Ahiahiahi! ¡Que dolor! ¡No me cliques! ¡Clica a los autores!",
                                              "¡Ay caramba! ¡Qué dolor!",
                                              "¡Te he dicho que no me cliques!",
                                              "¡BASTA! ¡Te lo digo para la ultima vez!",
                                              "¡Muahahahahah! El Cardo quiere todos los PokeMones... ¡Hazte con Todos!"
    };

    static int timesElCardoHasBeenClicked = 0;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        elSenorCardoCuandoSeHaceClicar = (ImageView)findViewById(R.id.elSenorCardoCuandoSeHaceClicarTodo);
        elSenorCardoCuandoSeHaceClicar.setOnClickListener(this);

        txtSebastiano = (TextView)findViewById(R.id.txtSebastiano);
        txtSebastiano.setOnClickListener(this);

        txtEugenio = (TextView)findViewById(R.id.txtEugenio);
        txtEugenio.setOnClickListener(this);

        txtAntonio = (TextView)findViewById(R.id.txtAntonio);
        txtAntonio.setOnClickListener(this);

        txtRoberto = (TextView)findViewById(R.id.txtRoberto);
        txtRoberto.setOnClickListener(this);

        txtLeCoseLOL = (TextView)findViewById(R.id.txtLeCoseLOL);
        txtLeCoseLOL.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        final int clickedViewId = view.getId();

        switch (clickedViewId)
        {
            case R.id.elSenorCardoCuandoSeHaceClicarTodo:
                elSenorCardoHaSidoClicado();
                break;
            case R.id.txtSebastiano:
                startTwitter("seebrock3r");
                break;
            case R.id.txtEugenio:
                startTwitter("workingkills");
                break;
            case R.id.txtAntonio:
                startTwitter("croccioAntonio");
                break;
            case R.id.txtRoberto:
                startTwitter("_tiwiz");
                break;
            case R.id.txtLeCoseLOL:
                startTwitter("LECOSELOL");
                break;
        }

    }

    private void elSenorCardoHaSidoClicado()
    {
        if (timesElCardoHasBeenClicked >= messagesElCardo.length) timesElCardoHasBeenClicked = 0;

        SplendidToast.showLonger(NinjaApplication.getInstance(), messagesElCardo[timesElCardoHasBeenClicked]);

        if (timesElCardoHasBeenClicked == messagesElCardo.length - 1)
        {
            NinjaApplication.playSoundAsync(R.raw.senhoramusica);

            final Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_pikachu);
            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setContentTitle("You can't stop the music!")
                    .setContentText("until you catch'em all!")
                    .setSmallIcon(R.drawable.ic_stat_pikachu)
                    .setLargeIcon(largeIcon);

            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }

        timesElCardoHasBeenClicked++;
    }

    private void startTwitter(String user)
    {
        final String url = "https://twitter.com/" + user;
        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
        urlIntent.setData(Uri.parse(url));
        startActivity(urlIntent);
    }
}