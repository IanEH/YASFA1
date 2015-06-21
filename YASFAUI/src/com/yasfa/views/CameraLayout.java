/*
 * TouchImageView.java
 * By: Edward Ian Hickman
 * Copyright (C) 2015 Edward Ian Hickman
 */

package com.yasfa.views;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.app.Dialog;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.Window;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import android.app.Activity;

import android.hardware.Camera  ;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

/**
 * Created by Ian on 08/02/15.
 */
public class CameraLayout extends YASFAControl{
    boolean sTaken;
    public String Name;
    byte [] ItemBytes=null;
    final TouchImageView vImage;
    AbsoluteLayout panel;
    CameraDialog cdlg;
    TextView grabClick;

    public void SetSize(int width,int height) {
        //vImage.setImageBitmap(null);

        RelativeLayout rl = (RelativeLayout)getChildAt(0);
        android.view.ViewGroup.LayoutParams params1 = rl.getLayoutParams();;
        params1.height = height;
        params1.width = width;
        //rl.setLayoutParams(params1);

        android.view.ViewGroup.LayoutParams params = vImage.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = height;
        params.width = width;
        //android.view.ViewGroup.LayoutParams params = rl.getLayoutParams();
    }

    public void NewName() {
        Name="S"+ UUID.randomUUID().toString().replace("-","");
    }

    final int IMAGE_MAX_SIZE = 630;
    private Bitmap prescaledBitmap(byte[] bmpin)  {
        Bitmap bmp=null;
        try {
            File file = null;
            InputStream fis;
            BitmapFactory.Options opts;
            int resizeScale;
            opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            fis = new ByteArrayInputStream(bmpin);
            BitmapFactory.decodeStream(fis, null, opts);
            fis.close();
            resizeScale = 1;
            if (opts.outHeight > IMAGE_MAX_SIZE || opts.outWidth > IMAGE_MAX_SIZE) {
                resizeScale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(opts.outHeight, opts.outWidth)) / Math.log(0.5)));
            }
            opts = new BitmapFactory.Options();
            opts.inSampleSize = resizeScale;
            fis = new ByteArrayInputStream(bmpin);
            bmp = BitmapFactory.decodeStream(fis, null, opts);
            fis.close();
        } catch (Exception ex) {}
        return bmp;
    }

    static int MaxImageSize = 1000000;
    public void Compress() {
        if (ItemBytes.length > MaxImageSize) {
            Bitmap bmp = prescaledBitmap(ItemBytes);
            if (bmp != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                ItemBytes = stream.toByteArray();
                bmp.recycle();
            }
        }
        if (ItemBytes.length > MaxImageSize) {
            int comress = 90;
            Bitmap bitmap = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
            if (bitmap.getWidth() > IMAGE_MAX_SIZE || bitmap.getHeight() > IMAGE_MAX_SIZE) {
                float swidth = (float) (IMAGE_MAX_SIZE) / (float) (bitmap.getWidth());
                Bitmap bm2 = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * swidth), (int) (bitmap.getHeight() * swidth), true);
                bitmap.recycle();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                ItemBytes = stream.toByteArray();
                bm2.recycle();
            } else {
                bitmap.recycle();

            }

            while (ItemBytes.length > MaxImageSize && comress > 10) {
                Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, comress, bos);
                comress = comress - 10;
                bitmapOrg.recycle();
                ItemBytes = bos.toByteArray();
            }
        }
    }

    public byte [] GetByteValue()
     {
        try {
            Drawable img = vImage.getDrawable();
            if (img != null) {
                try {
                    Compress();
                    return ItemBytes;
                } catch (Exception ex) {
                }
            }
        } catch (Exception ex) {

        }
        return new byte [1];
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public void SetValue(byte [] itemBytes) {
        if  (itemBytes !=null && itemBytes.length > 1) {
            ItemBytes=itemBytes;
           // vImage.setText("");
            ShowImage(ItemBytes);
        } else {
            DefaultValue();
        }
    }

    public void DefaultValue() {
        ItemBytes=null;
        try {
            vImage.setBackgroundColor(Color.WHITE);
            ((BitmapDrawable)vImage.getDrawable()).getBitmap().recycle();
        } catch (Exception ex) {
        }
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(5, 5, conf); // this creates a MUTABLE bitmap
        vImage.setImageBitmap(bmp);
    }

    public CameraLayout(final InflateView context) {
        super(context);
        final RelativeLayout rl = new RelativeLayout(context);
        vImage = new TouchImageView(context);
        vImage.setVisibility(View.VISIBLE);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(5, 5, conf); // this creates a MUTABLE bitmap
        vImage.setImageBitmap(bmp);
        rl.addView(vImage);


        grabClick = new TextView(context);
        grabClick.setText("Grab");
        grabClick.setFocusable(false);
        grabClick.setFocusableInTouchMode(false);
        grabClick.setBackgroundColor(Color.TRANSPARENT);
        grabClick.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        grabClick.setTextColor(Color.WHITE);
        grabClick.setTextSize(18);
        grabClick.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 5, 5));
        SetLayout(grabClick, RelativeLayout.ALIGN_PARENT_TOP, 0, 0, 0, 0);


        final RelativeLayout menu =   new RelativeLayout(context);
        TextView md = new TextView(context);
        md.setText("M");
        md.setFocusable(false);
        md.setFocusableInTouchMode(false);
        md.setBackgroundColor(Color.TRANSPARENT);
        md.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        md.setTextColor(Color.WHITE);
        md.setTextSize(17);
        md.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean removed=false;
                for (int i=0;i<rl.getChildCount();++i) {
                    if (rl.getChildAt(i)==menu) {
                        rl.removeView(menu);
                        removed=true;
                        break;
                    }
                }
                if (!removed) rl.addView(menu);
            }
        });
        md.setLayoutParams(new AbsoluteLayout.LayoutParams(25, 25, 5, 5));
        SetLayout(md, RelativeLayout.ALIGN_PARENT_TOP, 0, 30, 0, 0);
        rl.addView(md);

        TextView saveClick = new TextView(context);
        saveClick.setText("SDSave");
        saveClick.setFocusable(false);
        saveClick.setFocusableInTouchMode(false);
        saveClick.setBackgroundColor(Color.BLACK);
        saveClick.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        saveClick.setTextColor(Color.WHITE);
        saveClick.setTextSize(14);
        saveClick.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 5, 5));
        SetLayout(saveClick, RelativeLayout.ALIGN_PARENT_TOP,25, 30, 0, 0);

        TextView loadClick = new TextView(context);
        loadClick.setText("SDLoad");
        loadClick.setFocusable(false);
        loadClick.setFocusableInTouchMode(false);
        loadClick.setBackgroundColor(Color.BLACK);
        loadClick.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        loadClick.setTextColor(Color.WHITE);
        loadClick.setTextSize(14);
        loadClick.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 5, 5));
        SetLayout(loadClick, RelativeLayout.ALIGN_PARENT_TOP, 25, 50, 0, 0);

        loadClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rl.removeView(menu);
                loadit ld = new loadit();
                Dialog dg = (context.CreateDialog(context.DIALOG_LOAD_FILE, ".jpg", ld));
            }
        });


        saveClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rl.removeView(menu);
                final Dialog mDialog;
                mDialog = new Dialog(context);
                mDialog.setTitle(" Save Picture ");
                LinearLayout ll = new LinearLayout(context);
                ll.setOrientation(LinearLayout.VERTICAL);
                LinearLayout ll1 = new LinearLayout(context);
                final LinearLayout.LayoutParams lp = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mDialog.addContentView(ll, lp);


                Button ok, cancel;
                final EditText filename;
                final TextView lfilename;

                lfilename = (TextView) new TextView(context);
                lfilename.setText("Name");
                filename = (EditText) new EditText(context);
                ll.addView(lfilename);
                ll.addView(filename);
                ok = (Button) new Button(context);
                ll1.addView(ok);
                ok.setText("OK");
                cancel = (Button) new Button(context);
                ll1.addView(cancel);
                ll.addView(ll1);
                cancel.setText("Cancel");
                ok.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        Utils.saveToSD(context,filename.getText() + ".jpg",ItemBytes);
                        mDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mDialog.cancel();
                    }
                });
                mDialog.show();

            }
        });

        rl.setBackgroundColor(Color.WHITE);
        rl.addView(grabClick);
        menu.addView(saveClick);
        menu.addView(loadClick);
        addView(rl);

        grabClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) { // Take a picture!
                grabClick.setEnabled(false);


                // tv2.setText("Image#");
                // tv2.setEnabled(false);
                /** Handles data for jpeg picture */
                PictureCallback jpegCallback = new PictureCallback() {
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            // Save the Data!
                            ItemBytes = data.clone();
                            Display display = ((WindowManager) mcontext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                            int angle = 0;
                            if (display.getRotation() == Surface.ROTATION_0) {
                                angle = 90;
                            } else if (display.getRotation() == Surface.ROTATION_90) {
                                angle = 0;
                            } else if (display.getRotation() == Surface.ROTATION_180) {
                                angle = 270;
                            } else if (display.getRotation() == Surface.ROTATION_270) {
                                angle = 180;
                            }

                            if (angle != 0) {
                                Matrix matrix = new Matrix();
                                matrix.postRotate(angle);
                                Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes, 0, ItemBytes.length);
                                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
                                bitmapOrg.recycle();

                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                                ItemBytes = stream.toByteArray();
                                rotatedBitmap.recycle();
                            }
                            ShowImage(ItemBytes);
                            cdlg.preview.closeCamera();
                            try {
                                ((InflateView) mcontext).mainRelativeLayout.removeView(panel);
                            } catch (Exception ex) {
                            }
                            ;
                            grabClick.setEnabled(true);
                        } catch (Throwable e) {
                            cdlg.preview.closeCamera();
                            try {
                                ((InflateView) mcontext).mainRelativeLayout.removeView(panel);
                            } catch (Exception ex) {
                            }
                            ;
                            String ee = e.getMessage();
                            grabClick.setEnabled(true);
                        } finally {
                        }
                    }
                };
                /** Handles data for raw picture */
                PictureCallback rawCallback = new PictureCallback() {
                    public void onPictureTaken(byte[] data, Camera camera) {
                        grabClick.setEnabled(true);
                    }
                };
                cdlg = new CameraDialog(mcontext, jpegCallback, rawCallback);
            }
        });
    }

    class loadit extends LoadAFile
    {
        @Override
        public void LoadFile (String file){
            getBitmapFromFile(file);
        }
    }

    public void getBitmapFromFile(String src) {
        try {
            File file = new File(src);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
                SetValue(bytes);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } catch (Exception e) {
            // Log exception
        }
    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
        try {
            ((BitmapDrawable)vImage.getDrawable()).getBitmap().recycle();
        } catch (Exception ex) {
        }
    }
    public void Destroy() {
        try {
           ((BitmapDrawable)vImage.getDrawable()).getBitmap().recycle();

            vImage.setImageDrawable(null);
        } catch (Exception ex) {
        }
    }
    private void SetLayout(View item, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        // Defining the layout parameters of the Button
        //DBSerializer qq = new DBSerializer();
        //qq.storeWidget(button);

        RelativeLayout.LayoutParams LayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Add Margin to the LayoutParameters
        LayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);

        // Add Rule to Layout
        LayoutParameters.addRule(centerInParent);

        // Setting the parameters on the Button
        item.setLayoutParams(LayoutParameters);
    }
    public boolean ShowImage(byte [] ItemBytes) {
        try {
            if (ItemBytes!=null ) {
                vImage.setVisibility(View.VISIBLE);

                Bitmap bitmapOrg = BitmapFactory.decodeByteArray(ItemBytes,0,ItemBytes.length);

                try {
                    ((BitmapDrawable)vImage.getDrawable()).getBitmap().recycle();
                } catch (Exception ex) {
                }
                ViewGroup.LayoutParams params1 = vImage.getLayoutParams();
                float swidth = (float)(params1.width)/(float)(bitmapOrg.getWidth());
                vImage.setBackgroundColor(Color.LTGRAY);
                vImage.setImageBitmap(Bitmap.createScaledBitmap(bitmapOrg, (int)(bitmapOrg.getWidth()*swidth),(int)(bitmapOrg.getHeight()*swidth), false));
                bitmapOrg.recycle();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            String ee=e.getMessage();
            return false;
        }
    }


 public class CameraDialog  {
     public String Name;
     Context mcontext;
     Dialog me;

     PictureCallback mjpeg;
     PictureCallback mraw;
     boolean sTaken = false;
     Preview preview;
     @SuppressWarnings("deprecation")
     public CameraDialog(Context context, PictureCallback jpeg, PictureCallback raw) {
      //   super(context);
         mcontext = context;
     //    me=this;
         //setMinimumWidth(200);
        // setMinimumHeight(200);
         mjpeg = jpeg;
         mraw = raw;

         //setTitle("Camera");

       //  requestWindowFeature(Window.FEATURE_NO_TITLE);
         panel = new AbsoluteLayout(context);

         preview = new Preview(context);
         Display display = ((WindowManager)mcontext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
         int dwidth = display.getWidth();
         int dheight = display.getHeight();
         preview.setLayoutParams(new AbsoluteLayout.LayoutParams(dwidth, dheight-40, 0, 0));

         final TextView buttonClick = new TextView(context);
         buttonClick.setText("Click");
         buttonClick.setBackgroundColor(Color.TRANSPARENT);
         buttonClick.setShadowLayer(1.5f, -1, 1, Color.DKGRAY);
         buttonClick.setTextSize(20);
         buttonClick.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 5, 1));

         final TextView buttonClick1 = new TextView(context);
         buttonClick1.setText("Close");
         buttonClick1.setShadowLayer(1.5f, -1, 1, Color.DKGRAY);
         buttonClick1.setBackgroundColor(Color.TRANSPARENT);
         buttonClick1.setTextSize(20);
         buttonClick1.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 160, 1));

         panel.setBackgroundColor(Color.TRANSPARENT);

         buttonClick1.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 try {
                     buttonClick1.setEnabled(false);
                     preview.closeCamera();
                     ((InflateView) mcontext).mainRelativeLayout.removeView(panel);
                     grabClick.setEnabled(true);

                 } catch (Exception ex) {
                 }
                 ;
             }
         });

         buttonClick.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 sTaken = false;
                 try {
                     buttonClick.setEnabled(false);
                     preview.camera.takePicture(shutterCallback, mraw, mjpeg);
                 } catch (Exception ex) {
                     String tt = ex.getMessage();
                 }
             }
         });

         panel.addView(preview);
         panel.addView(buttonClick);
         panel.addView(buttonClick1);
         ((InflateView)mcontext).mainRelativeLayout.addView(panel);
       //  setContentView(panel);
         buttonClick.invalidate();
     }

     ShutterCallback shutterCallback = new ShutterCallback() {
         public void onShutter() {
             //Log.d(TAG, "onShutter'd");
         }
     };
 }
 class Preview extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mHolder;
    public Camera camera;


    Preview(Context context) {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);

   }

     private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
         final double ASPECT_TOLERANCE = 0.1;
         double targetRatio=(double)h / w;

         if (sizes == null) return null;

         Camera.Size optimalSize = null;
         double minDiff = Double.MAX_VALUE;

         int targetHeight = h;

         for (Camera.Size size : sizes) {
             double ratio = (double) size.width / size.height;
             if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
             if (Math.abs(size.height - targetHeight) < minDiff) {
                 optimalSize = size;
                 minDiff = Math.abs(size.height - targetHeight);
             }
         }

         if (optimalSize == null) {
             minDiff = Double.MAX_VALUE;
             for (Camera.Size size : sizes) {
                 if (Math.abs(size.height - targetHeight) < minDiff) {
                     optimalSize = size;
                     minDiff = Math.abs(size.height - targetHeight);
                 }
             }
         }
         return optimalSize;
     }
     Camera.Size   mPreviewSize;
     public void surfaceCreated(SurfaceHolder holder) {
         // The Surface has been created, acquire the camera and tell it where
         // to draw.
         try {

             camera = Camera.open();
             Parameters parameters = camera.getParameters();
             Display display = ((WindowManager) mcontext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
             int dwidth = display.getWidth();
             int dheight = display.getHeight()-40;

             if(display.getRotation() == Surface.ROTATION_0)
             {
                 mPreviewSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(),dwidth, dheight);
             } else if(display.getRotation() == Surface.ROTATION_90)
             {
                 mPreviewSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(),dheight,dwidth);
             } else if(display.getRotation() == Surface.ROTATION_180)
             {
                 mPreviewSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(),dwidth, dheight);
             } else if(display.getRotation() == Surface.ROTATION_270)
             {
                 mPreviewSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(),dheight,dwidth);
             }


             parameters.setPictureFormat(PixelFormat.JPEG);
             parameters.set("jpeg-quality", 100);

             camera.setParameters(parameters);
             camera.setPreviewDisplay(holder);

         } catch (Exception e) {
             closeCamera();
         }
     }

     public void closeCamera() {
         try {
             if (camera!=null) {
                 camera.stopPreview();
                 camera.release();
                 camera = null;
             }
         } catch (Exception e) {
         }

     }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        closeCamera();
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        try {

            Parameters parameters = camera.getParameters();
            Display display = ((WindowManager)mcontext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            parameters.setPreviewSize(mPreviewSize.width,mPreviewSize.height);

            if(display.getRotation() == Surface.ROTATION_0)
            {
                camera.setDisplayOrientation(90);
            } else if(display.getRotation() == Surface.ROTATION_180)
            {
                camera.setDisplayOrientation(270);
            } else if(display.getRotation() == Surface.ROTATION_270)
            {
                camera.setDisplayOrientation(180);
            }

            camera.setParameters(parameters);
            camera.startPreview();
        } catch (Exception e) {
            try {

            } catch (Exception ee) {
            }

        }
    }

  }}