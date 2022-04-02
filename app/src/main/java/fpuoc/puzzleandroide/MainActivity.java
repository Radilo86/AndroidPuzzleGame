package fpuoc.puzzleandroide;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

        int[] dimensiones = posicionRelativa(imageView);
        int redimensionIzquierda = dimensiones[0];
        int redimensionSuperior = dimensiones[1];
        int redimensionAncho = dimensiones[2];
        int redimensionAlto = dimensiones[3];

        int recortarAncho = redimensionAncho - 2 * abs(redimensionIzquierda);
        int recortarAlto = redimensionAlto - 2 * abs(redimensionSuperior);

        Bitmap redimensionBitmap = Bitmap.createScaledBitmap(bitmap,redimensionAncho,redimensionAlto,true);
        Bitmap recortarBitmap = Bitmap.createBitmap(redimensionBitmap,abs(redimensionIzquierda),abs(redimensionSuperior),recortarAncho,recortarAlto);

        int anchoPieza = recortarAncho / columnas;
        int altoPieza = recortarAlto / filas;

        int yCoord = 0;
        for (int fila = 0; fila < filas; fila++){
            int xCoord = 0;
            for (int columna = 0; columna < columnas; columna++){
                piezas.add(Bitmap.createBitmap(recortarBitmap,xCoord,yCoord,anchoPieza,altoPieza));
                xCoord = xCoord + anchoPieza;
            }
            yCoord = yCoord + altoPieza;
        }

        return piezas;
    }

    private int[] posicionRelativa(ImageView imageView){
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null){
            return ret;
        }

        float[] dimensionImagen = new float[9];
        imageView.getImageMatrix().getValues(dimensionImagen);

        final float redimensionX = dimensionImagen[Matrix.MSCALE_X];
        final float redimensionY = dimensionImagen[Matrix.MSCALE_Y];

        final Drawable drawable = imageView.getDrawable();
        final int origenAncho = drawable.getIntrinsicWidth();
        final int origenAlto = drawable.getIntrinsicHeight();

        final int actualAncho = Math.round(origenAncho * redimensionX);
        final int actualAlto = Math.round(origenAlto * redimensionY);

        ret[2] = actualAncho;
        ret[3] = actualAlto;

        int imgViewAncho = imageView.getWidth();
        int imgViewAlto = imageView.getHeight();

        int superior = (int)(imgViewAlto - actualAlto) / 2;
        int izquierdo = (int)(imgViewAncho - actualAncho) / 2;

        ret[0] = izquierdo;
        ret[1] = superior;

        return ret;
    }

}