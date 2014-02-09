package lecoselol.clumsyninja;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity implements View.OnClickListener
{
    ImageView elSenorCardoCuandoSeHaceClicar;
    TextView  txtLeCoseLOL, txtSebastiano, txtEugenio, txtAntonio, txtRoberto;

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

    private final void elSenorCardoHaSidoClicado()
    {
        SplendidToast.showLonger(NinjaApplication.getInstance(),
                                 "¡Ahiahiahi! ¡Que dolor! ¡No me cliques! ¡Clica a los autores!");
    }

    private final void startTwitter(String user)
    {
        final String url = "https://twitter.com/" + user;
        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
        urlIntent.setData(Uri.parse(url));
        startActivity(urlIntent);
    }
}