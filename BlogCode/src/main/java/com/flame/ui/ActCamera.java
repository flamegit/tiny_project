package com.flame.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.test.R;

/**
 * Camera与Matrix的比较：<br/>
 * Camera的rotate()相关方法是指定某一维度上旋转指定的角度。<br/>
 * Matrix的rotate()相关方法实现的效果是顺时针旋转指定的角度；与Camera指定Z轴旋转效果相同，但方向相反。<br/>
 * <p/>
 * Camera的translate()方法根据某一维度上视点的位移实现图像的缩放，与Matrix的scale()相关方法作用效果相似，
 * 只是Matrix的scale()相关方法是直接指定缩放比例。<br/>
 * <p/>
 * Camera不支持倾斜操作，Matrix可以直接实现倾斜操作。<br/>
 * <p/>
 * <a href="http://my.oschina.net/arthor" class="referer" target="_blank">@author</a> Sodino E-mail:sodinoopen@hotmail.com
 *
 * @version Time：2011-9-26 下午04:17:49
 */
public class ActCamera extends Activity implements OnSeekBarChangeListener {
    private Camera camera;
    // views
    private SeekBar seekbarXRotate;
    private SeekBar seekbarYRotate;
    private SeekBar seekbarZRotate;
    private TextView txtXRotate;
    private TextView txtYRotate;
    private TextView txtZRotate;
    private SeekBar seekbarXTranslate;;
    private SeekBar seekbarYTranslate;;
    private SeekBar seekbarZTranslate;
    private TextView txtXTranslate;
    private TextView txtYTranslate;
    private TextView txtZTranslate;
    private ImageView imgResult;
    // integer params
    private int rotateX, rotateY, rotateZ;
    private float skewX, skewY;
    private int translateX,translateY, translateZ;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        // camera
        camera = new Camera();
        // initViews
        // rotate
        seekbarXRotate = (SeekBar) findViewById(R.id.seekbarXRotate);
        seekbarXRotate.setOnSeekBarChangeListener(this);
        seekbarYRotate = (SeekBar) findViewById(R.id.seekbarYRotate);
        seekbarYRotate.setOnSeekBarChangeListener(this);
        seekbarZRotate = (SeekBar) findViewById(R.id.seekbarZRotate);
        seekbarZRotate.setOnSeekBarChangeListener(this);
//        txtXRotate = (TextView) findViewById(R.id.txtXRotate);
//        txtYRotate = (TextView) findViewById(R.id.txtYRotate);
//        txtZRotate = (TextView) findViewById(R.id.txtZRotate);
        // translate
//        seekbarXSkew = (SeekBar) findViewById(R.id.seekbarXSkew);
//        seekbarXSkew.setOnSeekBarChangeListener(this);
//        seekbarYSkew = (SeekBar) findViewById(R.id.seekbarYSkew);
//        seekbarYSkew.setOnSeekBarChangeListener(this);
        seekbarZTranslate = (SeekBar) findViewById(R.id.seekbarZTranslate);
        seekbarZTranslate.setOnSeekBarChangeListener(this);
        seekbarXTranslate = (SeekBar) findViewById(R.id.seekbarXTranslate);
        seekbarXTranslate.setOnSeekBarChangeListener(this);
        seekbarYTranslate = (SeekBar) findViewById(R.id.seekbarYTranslate);
        seekbarYTranslate.setOnSeekBarChangeListener(this);
//        txtXTranslate = (TextView) findViewById(R.id.txtXSkew);
//        txtYTranslate = (TextView) findViewById(R.id.txtYSkew);
//        txtZTranslate = (TextView) findViewById(R.id.txtZTranslate);
        imgResult = (ImageView) findViewById(R.id.imgResult);
        // refresh
        refreshImage();
    }

    @TargetApi(16)
    private void refreshImage() {
        // 获取待处理的图像
        BitmapDrawable tmpBitDra = (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_launcher);
        Bitmap tmpBit = tmpBitDra.getBitmap();
        // 开始处理图像
        // 1.获取处理矩阵
        // 记录一下初始状态。save()和restore()可以将图像过渡得柔和一些。
        // Each save should be balanced with a call to restore().
        camera.save();
        Matrix matrix = new Matrix();
        // rotate
        //camera.rotateX(rotateX);
        //camera.rotateY(rotateY);

        camera.translate(translateX, translateY, translateZ);

        camera.rotate(rotateX,rotateY,rotateZ);


        // translate

        camera.getMatrix(matrix);
        Log.d("ANDROID_LAB",matrix+"");



        // 恢复到之前的初始状态。
        camera.restore();
        matrix.preTranslate(-tmpBit.getWidth()>>1,-tmpBit.getHeight()>>1);
        matrix.postTranslate(tmpBit.getWidth()>>1,tmpBit.getHeight()>>1);
        // 设置图像处理的中心点
       // matrix.preTranslate(tmpBit.getWidth() >> 1, tmpBit.getHeight() >> 1);
       // matrix.preSkew(skewX, skewY);
        // matrix.postSkew(skewX, skewY);
        // 直接setSkew()，则前面处理的rotate()、translate()等等都将无效。
        // matrix.setSkew(skewX, skewY);
        // 2.通过矩阵生成新图像(或直接作用于Canvas)

        Bitmap newBit = null;
        try {
            // 经过矩阵转换后的图像宽高有可能不大于0，此时会抛出IllegalArgumentException
            //newBit = Bitmap.createBitmap(tmpBit, 0, 0, tmpBit.getWidth(), tmpBit.getHeight(), matrix, true);
           // Log.d("ANDROID_LAB", "width =" + newBit.getWidth() +"height ="+newBit.getHeight());
            Log.d("ANDROID_LAB", "x=" + camera.getLocationX() +"y=" + camera.getLocationY()+"z=" + camera.getLocationZ());
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }
            imgResult.setScaleType(ImageView.ScaleType.MATRIX);
            imgResult.setImageMatrix(matrix);
            imgResult.setImageBitmap(tmpBit);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == seekbarXRotate) {
          //  txtXRotate.setText(progress +"。");
            rotateX = progress;
        } else if (seekBar == seekbarYRotate) {
           // txtYRotate.setText(progress +"。");
            rotateY = progress;
        } else if (seekBar == seekbarZRotate) {
           // txtZRotate.setText(progress +"。" );
            rotateZ = progress;
        } else if (seekBar ==seekbarXTranslate ) {
            translateX = progress;
           // txtXTranslate.setText(String.valueOf(skewX));
        } else if (seekBar == seekbarYTranslate) {
            translateY = progress;
            //txtYTranslate.setText(String.valueOf(skewY));
        } else if (seekBar == seekbarZTranslate) {
            translateZ = progress;
          //  txtZTranslate.setText(String.valueOf(translateZ));
        }

        refreshImage();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
