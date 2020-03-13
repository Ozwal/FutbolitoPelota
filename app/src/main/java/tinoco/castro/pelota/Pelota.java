package tinoco.castro.pelota;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Pelota extends View implements SensorEventListener {
    //Para pintar o dibujar dentro de un lienzo:
    //Marcador
    Paint marcador = new Paint();
    //Cancha:
    Paint cancha = new Paint();
    //Pelota:
    Paint pelota = new Paint();
    //Porterias:
    Paint porteria1 = new Paint();
    Paint porteria2 = new Paint();
    //Variables de alto y ancho de la pantalla
    int alto, ancho;
    //Tamaño a dibujar del circulo
    int tam = 25;
    //Evitar que el circulo salga de la pantalla
    int borde = 12;
    //Valores del acelerometro
    float ejeX = 0, ejeY = 0, ejeZ = 0;
    String x, y, z;
    //Porterias
    int izq, der, alt;
    //Marcador
    int contador1 = 0, contador2 = 0;

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    Context prueba;

    public Pelota(Context interfaz){
        //Dar de alta el sensor de acelerometro
        super(interfaz);
        SensorManager snAdmin = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor snsRotacion = snAdmin.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        snAdmin.registerListener(this, snsRotacion, SensorManager.SENSOR_DELAY_FASTEST);
        Display pantalla = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        ancho = pantalla.getWidth();
        alto = pantalla.getHeight();
        izq = ancho / 3;
        der = izq * 2;
        alt = alto - 20;
        prueba = interfaz;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        ejeX -= sensorEvent.values[0];
        x = Float.toString(ejeX);
        if (ejeX < (tam + borde)) {
            ejeX = (tam + borde);
        } else if (ejeX > (ancho - (tam + borde))) {
            ejeX = ancho - (tam + borde);
        }
        ejeY += sensorEvent.values[1];
        y = Float.toString(ejeY);
        if (ejeY < (tam + borde)) {
            ejeY = (tam + borde);
            if(ejeX > izq && ejeX < der){
                contador1++;
                Toast.makeText(prueba, "Gol local: "+ contador1, Toast.LENGTH_SHORT).show();
                ejeY = alto/2;
            }
        } else if (ejeY > (alto - tam - 200)) {
            ejeY = (alto - tam - 200);
            if(ejeX > izq && ejeX < der){
                contador2++;
                Toast.makeText(prueba, "Gol visitante: "+ contador2, Toast.LENGTH_SHORT).show();
                ejeY = alto/2;
            }
        }
        ejeZ = sensorEvent.values[2];
        z = String.format("", ejeZ);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cancha.setColor(Color.parseColor("#17EE2D"));
        canvas.drawRect(0,0, ancho,alto, cancha);
        //marcador.setColor(Color.WHITE);
        //canvas.drawText(contador1+" - "+contador2,ancho/2,alto/2, marcador);
        porteria1.setColor(Color.BLACK);
        canvas.drawRect(izq,0, der,20, porteria1);
        porteria2.setColor(Color.BLACK);
        //canvas.drawRect(izq, alt, der, alto, porteria2);
        canvas.drawRect(izq, alt, der, alto, porteria2);
        pelota.setColor(Color.BLUE);
        canvas.drawCircle(ejeX, ejeY, ejeZ + tam, pelota);
    }
}
