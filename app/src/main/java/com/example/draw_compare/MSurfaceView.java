package com.example.draw_compare;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class MSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    /**
     * 是否处于绘制状态
     */
    private boolean mIsDrawing;
    /**
     * 帮助类
     */
    private SurfaceHolder mHolder;
    /**
     * 画布
     */
    private Canvas mCanvas;
    /**
     * 路径
     */
    private Path mPath;
    /**
     * 画笔
     */
    private Paint mPaint;

    //初始化背景
    private int mWidth;
    private int mHeight;
    private RectF mDrawPictureRect = new RectF();
    //背景
    private Bitmap mBg;
    private Bitmap mBg_new;
    private ArrayList<Integer> drawable_list=new ArrayList<>();
    private static final int DRAW1=R.drawable.draw1;

    private int draw=DRAW1;
    private Bitmap loadImageByResId(int resId) {
        return BitmapFactory.decodeResource(getResources(), resId);
    }
    private void initBitmaps() {
        //初始化BitMap
        mBg = loadImageByResId(draw);
        mBg_new= loadImageByResId(draw);
    }

    /**
     * 获取drawable中所有的draw文件
     */
    private void getallDrawPic(){
        Field[] fields=R.drawable.class.getDeclaredFields();
        for(Field field:fields){
            //获取文件名对应的系统生成的id
            if(field.getName().contains("draw")) {
                int resID = getResources().getIdentifier(field.getName(),
                        "drawable",getClass().getPackage().getName());
                drawable_list.add(resID);
                Log.e("ADD","add all pic to List");
            }else {
                Log.e("ADD Wrong",field.getName());
            }
        }
    }
    public MSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        return true;
    }

    private void initView() {
        //初始化变量
        //获取SurfaceView LockHolder
        mHolder = getHolder();
        mHolder.addCallback(this);

        // 设置画布 背景透明
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

        //设置可选中、可触摸、屏幕常亮
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);

        //初始化路径、画笔
        if (mPath == null) {
            mPath = new Path();
        }
        if (mPaint == null) {
            mPaint = new Paint();
        }
        //设置画笔抗锯齿
        mPaint.setAntiAlias(true);
        //设置画笔颜色
        mPaint.setColor(Color.RED);
        //设置画笔样式，宽度20
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        initBitmaps();
        getallDrawPic();
//
//        final int DRAW2=R.drawable.draw2;
//        final int DRAW3=R.drawable.draw3;
//        final int DRAW4=R.drawable.draw4;
//        drawable_list.add(DRAW1);
//        drawable_list.add(DRAW2);
//        drawable_list.add(DRAW3);
//        drawable_list.add(DRAW4);
    }

    @Override
    public void run() {
        //获取画布

        long start = System.currentTimeMillis();
        while (mIsDrawing) {
            //画
            draw();
        }
        long end = System.currentTimeMillis();
        //解锁画布

        if (end - start < 100) {
            try {
                Thread.sleep(100 - (end - start));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doDraw(Canvas canvas) {
        try {
//            mCanvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawPath(mPath, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            if (canvas != null) {
//                mHolder.unlockCanvasAndPost(mCanvas);
//            }
        }
    }

    private void draw() {
        try {
            //获得锁
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //画背景
                mCanvas.drawBitmap(mBg,null,mDrawPictureRect,null);
//               mCanvas.drawColor(Color.WHITE);
                //画线
                mCanvas.drawPath(mPath, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                //释放锁
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        mIsDrawing = false;

    }

    //初始化尺寸相关
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mDrawPictureRect.set(0, 0, w, h);
    }



    /**
     * 清除内容
     */
    public void reset() {
        Log.e("TAG","CLEAR");
        mPath.reset();
    }


    /**
     * 检查相似度
     */
    public String check() {
//        mCanvas=mHolder.lockCanvas();
//        mCanvas=mHolder.lockCanvas(null);
        mCanvas.drawColor(Color.WHITE);
//        doDraw(mCanvas);

        saveToFile(getBitmap());
        //发现保存的是黑色的
//        mCanvas.drawColor(Color.WHITE);
        Bitmap bitmap=loadFromFile();
        Bitmap newbg=AdjustBitmap.sizeBitmap(mBg_new,getWidth(),getHeight());
        initBitmaps();
        BitmapCompareUtil.DiffInfo diffInfo = BitmapCompareUtil.getBitmapDiffInfo(newbg,bitmap,Color.BLACK,Color.RED);
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Log.e("SIMI", diffInfo.toString());
        return diffInfo.toString();

    }

    public void save(){
        saveToFile(getBitmap());
    }
    public Bitmap getBitmap() {
        Log.e("mWidth", "" + mWidth);
        Log.e("mHeight", "" + mHeight);
        Bitmap bitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        doDraw(canvas);
//        canvas.save();

        return bitmap;
    }
    /**
     * 改变背景图
     */
    public void changePic(){
        reset();
        Random random=new Random();
        int index=random.nextInt(drawable_list.size());
        draw=drawable_list.get(index);
        initBitmaps();
    }

    /**
     * 设置字号颜色
     *
     * @param size
     * @param color
     */
    public void setLineSizeandColor(int size, int color) {
        mPaint.setStrokeWidth(size);
        mPaint.setColor(color);
    }


    /**
     * 保存指纹图片
     *
     * @param bitmap
     */

    //保存文件到sd卡
    public void saveToFile(Bitmap bitmap) {
        //获取SD卡状态
        String state = Environment.getExternalStorageState();
        //判断SD卡是否就绪
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.e("TAG","SD card is not mounted");
            return;
        }
        //取得SD卡根目录
        File file = Environment.getExternalStorageDirectory();
        try {
            Log.e("T", "======SD卡根目录：" + file.getCanonicalPath());
            if(file.exists()){
                Log.e("T", "file.getCanonicalPath() == " + file.getCanonicalPath());
            }
            /*
            输出流的构造参数1：可以是File对象  也可以是文件路径
            输出流的构造参数2：默认为False=>覆盖内容； true=>追加内容
             */
            File myfile=new File(file.getCanonicalPath()+"/dbq.jpg");
            FileOutputStream fos = new FileOutputStream(myfile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
//            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            Log.e("TAG","save successfully");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    //从sd卡中读取
    public Bitmap loadFromFile(){
        //取得SD卡根目录
        Bitmap bitmap=null;
        try {
            File file =new File(Environment.getExternalStorageDirectory().getCanonicalPath());
            Log.e("T", "======SD卡根目录：" + file.getCanonicalPath());
            if(file.exists()){
                Log.e("T", "file.getCanonicalPath() == " + file.getCanonicalPath());
                bitmap=BitmapFactory.decodeFile(file.getCanonicalPath()+"/dbq.jpg");
            }
//            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            Log.e("TAG","save successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
