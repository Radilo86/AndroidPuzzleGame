package fpuoc.puzzleandroide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Bitmap> piezas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout layout = findViewById(R.id.layout2);
        ImageView imageView = findViewById(R.id.imageView2);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                piezas = seccionarImagen();
                for (Bitmap pieza : piezas) {
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageBitmap(pieza);
                    layout.addView(iv);
                }
            }
        });
    }


    private ArrayList<Bitmap> seccionarImagen() {
        int filas = 4;
        int columnas = 3;
        int totalPiezas = filas * columnas;


        ImageView imageView = findViewById(R.id.imageView2);
        ArrayList<Bitmap> piezas = new ArrayList<>(totalPiezas);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int anchoPieza = bitmap.getWidth() / columnas;
        int altoPieza = bitmap.getHeight() / filas;

        int yCoord = 0;
        for (int fila = 0; fila < filas; fila++){
            int xCoord = 0;
            for (int columna = 0; columna < columnas; columna++){
                piezas.add(Bitmap.createBitmap(bitmap,xCoord,yCoord,anchoPieza,altoPieza));
                xCoord = xCoord + anchoPieza;
            }
            yCoord = yCoord + altoPieza;
        }

        return piezas;
    }
}