package fpuoc.puzzleandroide;



import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<PiezaPuzzle> piezas;

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
                for (PiezaPuzzle piece : piezas) {
                    layout.addView(piece);
                    ImageView iv = new ImageView(getApplicationContext());
                    //iv.setImageBitmap(piece);
                    //layout.addView(iv);
                }
            }
        });
    }


    private ArrayList<PiezaPuzzle> seccionarImagen() {
        int filas = 4;
        int columnas = 3;
        int totalPiezas = filas * columnas;


        ImageView imageView = findViewById(R.id.imageView2);
        ArrayList<PiezaPuzzle> piezas = new ArrayList<>(totalPiezas);

        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensiones = posicionRelativa(imageView);
        int redimensionIzquierda = dimensiones[0];
        int redimensionSuperior = dimensiones[1];
        int redimensionAncho = dimensiones[2];
        int redimensionAlto = dimensiones[3];

        int recortarAncho = redimensionAncho - (2 * abs(redimensionIzquierda));
        int recortarAlto = redimensionAlto - (2 * abs(redimensionSuperior));

        Bitmap redimensionBitmap = Bitmap.createScaledBitmap(bitmap,redimensionAncho,redimensionAlto,true);
        Bitmap recortarBitmap = Bitmap.createBitmap(redimensionBitmap,abs(redimensionIzquierda),abs(redimensionSuperior),recortarAncho,recortarAlto);

        int anchoPieza = recortarAncho / columnas;
        int altoPieza = recortarAlto / filas;

        /*int yCoord = 0;
        for (int fila = 0; fila < filas; fila++){
            int xCoord = 0;
            for (int columna = 0; columna < columnas; columna++){
                piezas.add(Bitmap.createBitmap(recortarBitmap,xCoord,yCoord,anchoPieza,altoPieza));
                xCoord = xCoord + anchoPieza;
            }
            yCoord = yCoord + altoPieza;
        }*/

        int yCoord = 0;
        for (int row = 0; row < filas; row++) {
            int xCoord = 0;
            for (int col = 0; col < columnas; col++) {
                // calculate offset for each piece
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = anchoPieza / 3;
                }
                if (row > 0) {
                    offsetY = altoPieza / 3;
                }

                Bitmap pieceBitmap = Bitmap.createBitmap(recortarBitmap, xCoord - offsetX, yCoord - offsetY, anchoPieza + offsetX, altoPieza + offsetY);
                PiezaPuzzle piece = new PiezaPuzzle(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = anchoPieza + offsetX;
                piece.pieceHeight = altoPieza + offsetY;

                Bitmap puzzlePiece = Bitmap.createBitmap(anchoPieza + offsetX, altoPieza + offsetY, Bitmap.Config.ARGB_8888);

                // draw path
                int bumpSize = altoPieza / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (col == columnas - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize,offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (row == filas - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5,pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (col == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                // mask the piece
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);

                // draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                // draw a black border
                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);

                piezas.add(piece);
                xCoord += anchoPieza;
            }
            yCoord += altoPieza;
        }
        return piezas;
    }

    private int[] posicionRelativa(ImageView imageView){
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;


        float[] dimensionImagen = new float[9];
        imageView.getImageMatrix().getValues(dimensionImagen);

        final float redimensionX = dimensionImagen[Matrix.MSCALE_X];
        final float redimensionY = dimensionImagen[Matrix.MSCALE_Y];

        final Drawable draw = imageView.getDrawable();
        final int origenAncho = draw.getIntrinsicWidth();
        final int origenAlto = draw.getIntrinsicHeight();

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