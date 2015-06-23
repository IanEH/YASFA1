/*
    Copyright (C) {2015}  (Edward Ian Hickman}

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.yasfa.views;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import com.yasfa.views.DBInterface.Direction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class RecordPlay extends YASFAControl {
	public String Name;
    private MediaPlayer mPlayer = null;
    private Visualizer mVisualizer;
    private VisualizerView mVisualizerView;
    private MediaRecorder mRecorder = null;
    private static final float VISUALIZER_HEIGHT_DIP = 50f;
    String mFileName="";
    private RelativeLayout listBG;

    final FButton record;
    final FButton play;

    public void NewName() {
        Name="S"+ UUID.randomUUID().toString().replace("-","");
    }
    static int MaxSoundSize = 1000000;

    public String SayText() {
        return "Play Record Control";
    }

    public byte [] GetByteValue() {
        if (!mFileName.equals("")) {
            File file = new File(mFileName);
            int size = (int) file.length();
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                int length = size;
                if (length>MaxSoundSize) length = MaxSoundSize;
                byte[] bytes = new byte[length];
                buf.read(bytes, 0, length);
                buf.close();
                return bytes;
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }

        return new byte [1];
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)

    public void SetValue(byte [] itemBytes)  {
        if  (itemBytes !=null && itemBytes.length > 1) {
            try {
                mVisualizerView.setBackgroundColor(Color.BLACK);
                killit();
                AudioRecordTest();
                File yourFile = new File(mFileName);
                yourFile.createNewFile();
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFileName));
                bos.write(itemBytes);
                bos.flush();
                bos.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        } else {
            DefaultValue();
        }
    }
    public void DefaultValue() {
        try {
            mVisualizerView.setBackgroundColor(Color.DKGRAY);
        File yourFile = new File(mFileName);
        yourFile.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFileName));
        bos.flush();
        bos.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public void Edit (boolean edit) {
                if (edit) {
                    this.setBackgroundColor(InflateView.edite);
                    LinearLayout lbg = (LinearLayout)getChildAt(0);
                    lbg.setEnabled(false);
                    LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
                    lbg2.setEnabled(false);
                    ListView list = (ListView)lbg2.getChildAt(0);
                    list.setEnabled(false);
                    list.setBackgroundColor(InflateView.edite);
                } else {
                    this.setBackgroundColor(Color.BLACK);
                    LinearLayout lbg = (LinearLayout)getChildAt(0);
                    lbg.setEnabled(true);
                    LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(0);
                    lbg2.setEnabled(true);
                    ListView list = (ListView)lbg2.getChildAt(0);
                    list.setEnabled(true);
                    list.setBackgroundColor(Color.BLACK);

                }
    }

    boolean mStartRecording = true;
    boolean mStartPlaying = true;

    public void killit() {
        try {
            stopRecording();
            stopPlaying();
            mStartRecording = true;
            mStartPlaying = true;
            play.setBackgroundResource(R.drawable.mnrec);
            record.setBackgroundResource(R.drawable.mnbrec);
            play.setEnabled(true);
            record.setEnabled(true);
        } catch (Exception ex) {}
    }

	public RecordPlay(final InflateView MainView) {
        super(MainView);

        this.setBackgroundColor(Color.BLACK);

        setOrientation(LinearLayout.VERTICAL);
        // Create a VisualizerView (defined below), which will render the simplified audio
        // wave form to a Canvas.
        mVisualizerView = new VisualizerView(MainView);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)));



        LinearLayout SelBG = new LinearLayout(MainView);
        SelBG.setBackgroundColor(Color.WHITE);
        SelBG.setPadding(1, 1, 1, 1);
        SelBG.setOrientation(LinearLayout.VERTICAL);



        play = new FButton(MainView,"Play");
        play.setBackgroundResource(R.drawable.mnrec);
        play.setTextSize(12);
        play.setShadowLayer(1.5f, -1, 1, Color.GRAY);
        play.setPadding(7, 3, 1, 1);
        play.setVisibility(View.VISIBLE);



        play.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    play.setBackgroundResource(R.drawable.mnc);
                    record.setEnabled(false);
                } else {
                    play.setBackgroundResource(R.drawable.mnrec);
                    record.setEnabled(true);
                }
                mStartPlaying = !mStartPlaying;

            }
        });
        AudioRecordTest();
        record = new FButton(MainView,"Record");
        record.setBackgroundResource(R.drawable.mnbrec);
        record.setTextSize(12);
        record.setShadowLayer(1.5f, -1, 1, Color.GRAY);
        record.setPadding(7, 3, 1, 1);
        record.setVisibility(View.VISIBLE);

        record.setOnClickListener(new OnClickListener()   {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    record.setBackgroundResource(R.drawable.mnc);
                    play.setEnabled(false);
                } else {
                    record.setBackgroundResource(R.drawable.mnbrec);
                    play.setEnabled(true);
                }
                mStartRecording = !mStartRecording;
            }
        });




       // params.setMargins(5, 1, 5, 5);

        LinearLayout SelBG1 = new LinearLayout(MainView);
        SelBG1.setBackgroundColor(Color.BLACK);
        SelBG1.setPadding(2, 2, 1, 1);
        SelBG.addView(SelBG1);
        SelBG.setBackgroundColor(Color.GRAY);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(95,28);
        params1.setMargins(1, 1, 1, 1);
        SelBG1.setLayoutParams(params1);
        SelBG1.setBaselineAligned(false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(1, 25);
        TextView pad1 = new TextView(MainView);
        pad1.setEnabled(false);
        SelBG1.addView(pad1,params);

        params = new FrameLayout.LayoutParams(44, 25);
        params.gravity = Gravity.CENTER_VERTICAL;
        play.setGravity(Gravity.TOP);
        SelBG1.addView(play, params);

        params = new FrameLayout.LayoutParams(2, 25);
        TextView pad = new TextView(MainView);
        pad.setEnabled(false);
        SelBG1.addView(pad,params);

        params = new FrameLayout.LayoutParams(44, 25);
        params.gravity = Gravity.CENTER_VERTICAL;

        record.setGravity(Gravity.TOP);
        SelBG1.addView(record, params);

        SelBG.setOrientation(LinearLayout.HORIZONTAL);

        listBG = new RelativeLayout(MainView);
        listBG.setBackgroundColor(Color.GRAY);
        SelBG.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(100,28);
        params2.setMargins(1, 1, 1, 1);
        mVisualizerView.setLayoutParams(params2);

        mVisualizerView.setBackgroundColor(Color.BLACK);
        mVisualizerView.setPadding(2, 2, 2, 2);
        SetLayout(mVisualizerView, RelativeLayout.ALIGN_PARENT_TOP, 0, 0, 0, 0);
        listBG.addView(mVisualizerView);

        listBG.setPadding(1, 1, 1, 1);
       // LinearLayout listBG2 = new LinearLayout(MainView);
       // listBG2.setBackgroundColor(Color.BLACK);
      //  listBG2.setPadding(2, 2, 2, 2);

      //  listBG.addView(listBG2);
        addView(listBG);
        addView(SelBG);


        final RelativeLayout menu =   new RelativeLayout(mcontext);

        TextView md = new TextView(mcontext);
        md.setText("M");
        md.setFocusable(false);
        md.setFocusableInTouchMode(false);
        md.setBackgroundColor(Color.TRANSPARENT);
        md.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        md.setTextColor(Color.WHITE);
        md.setTextSize(17);
        md.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                boolean removed = false;
                for (int i = 0; i < listBG.getChildCount(); ++i) {
                    if (listBG.getChildAt(i) == menu) {
                        listBG.removeView(menu);
                        removed = true;
                        break;
                    }
                }
                if (!removed) listBG.addView(menu);
            }
        });
        md.setLayoutParams(new AbsoluteLayout.LayoutParams(25, 25, 5, 5));
        SetLayout(md, RelativeLayout.ALIGN_PARENT_TOP, 5, 0, 0, 0);

        params = new FrameLayout.LayoutParams(44, 25);
        params.gravity = Gravity.CENTER_VERTICAL;
        record.setGravity(Gravity.TOP);
        listBG.addView(md);

        TextView saveClick = new TextView(mcontext);
        saveClick.setText("SDSave");
        saveClick.setFocusable(false);
        saveClick.setFocusableInTouchMode(false);
        saveClick.setBackgroundColor(Color.BLACK);
        saveClick.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        saveClick.setTextColor(Color.WHITE);
        saveClick.setTextSize(14);
        saveClick.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 5, 5));
        SetLayout(saveClick, RelativeLayout.ALIGN_PARENT_TOP, 30, 0, 0, 0);
        saveClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listBG.removeView(menu);
                final Dialog mDialog;
                mDialog = new Dialog(mcontext);
                mDialog.setTitle(" Save Sound ");
                LinearLayout ll = new LinearLayout(mcontext);
                ll.setOrientation(LinearLayout.VERTICAL);
                LinearLayout ll1 = new LinearLayout(mcontext);
                final LinearLayout.LayoutParams lp = new
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                mDialog.addContentView(ll, lp);


                Button ok, cancel;
                final EditText filename;
                final TextView lfilename;

                lfilename = (TextView) new TextView(mcontext);
                lfilename.setText("Name");
                filename = (EditText) new EditText(mcontext);
                ll.addView(lfilename);
                ll.addView(filename);
                ok = (Button) new Button(mcontext);
                ll1.addView(ok);
                ok.setText("OK");
                cancel = (Button) new Button(mcontext);
                ll1.addView(cancel);
                ll.addView(ll1);
                cancel.setText("Cancel");
                ok.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        Utils.saveToSD(mcontext,filename.getText() + ".mp3",GetByteValue());
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
        menu.addView(saveClick);


        TextView loadClick = new TextView(mcontext);
        loadClick.setText("SDLoad");
        loadClick.setFocusable(false);
        loadClick.setFocusableInTouchMode(false);
        loadClick.setBackgroundColor(Color.BLACK);
        loadClick.setShadowLayer(1.5f, -1, 1, Color.BLACK);
        loadClick.setTextColor(Color.WHITE);
        loadClick.setTextSize(14);
        loadClick.setLayoutParams(new AbsoluteLayout.LayoutParams(70, 35, 5, 5));
        SetLayout(loadClick, RelativeLayout.ALIGN_PARENT_TOP, 30, 18, 0, 0);

        loadClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listBG.removeView(menu);
                loadit ld = new loadit();
                Dialog dg = (MainView.CreateDialog(MainView.DIALOG_LOAD_FILE, ".mp3", ld));
            }
        });
        menu.addView(loadClick);


        SetSize(150,100);
		//SetLayout(list, RelativeLayout.ALIGN_PARENT_LEFT, 0, 80, 200, 200);
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

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setBackgroundResource(R.drawable.mnrec);
                    mStartPlaying = true;
                }
            });

            mPlayer.setDataSource(mFileName);
            // Create the Visualizer object and attach it to our media player.
             mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
            mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                                  int samplingRate) {
                    mVisualizerView.updateVisualizer(bytes);
                }

                public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, false);
            //mVisualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
            mVisualizer.setEnabled(true);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        }
    }
    public void stopPlaying() {
        try {
            mPlayer.release();
            play.setBackgroundResource(R.drawable.mnrec);
            mVisualizer.setEnabled(false);
            mPlayer = null;
        } catch (Exception ex) {}
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
        }

        mRecorder.start();
    }

    private void stopRecording() {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (Exception ex) {}
    }



    public String AudioRecordTest() {
      //  mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName =  "/data/data/" + mcontext.getPackageName() + "/databases/"+"YASFAtemp.3gp";
        return mFileName;
    }




    public void onPause() {
       // super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }




    public void SetSize(int width,int height) {
        try {
            if (height < 34) height = 34;
            if (width < 105) width = 105;
            LinearLayout lbg = (LinearLayout) getChildAt(1);
            android.view.ViewGroup.LayoutParams params = lbg.getLayoutParams();
            params.height = 34;
            params.width = width;

            params =listBG.getLayoutParams();
            params.height = height-34;
            params.width = width;

            RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(width - 2, height-34-2);
            params1.setMargins(1, 1, 1, 1);
            mVisualizerView.setLayoutParams(params1);
        } catch (Exception ex) {
            String sex = ex.getMessage();
        }
        //LinearLayout lbg2 = (LinearLayout)lbg.getChildAt(1);
    }
    private void SetLayout(View button, int centerInParent, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        // Defining the layout parameters of the Button
        //DBSerializer qq = new DBSerializer();
        //qq.storeWidget(button);

    	RelativeLayout.LayoutParams buttonLayoutParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // Add Margin to the LayoutParameters
        buttonLayoutParameters.setMargins(marginLeft, marginTop, marginRight, marginBottom);

        // Add Rule to Layout
        //buttonLayoutParameters.addRule(centerInParent);

        // Setting the parameters on the Button
        button.setLayoutParams(buttonLayoutParameters);     
    }

    class VisualizerView extends View {
        private byte[] mBytes;
        private float[] mPoints;
        private Rect mRect = new Rect();

        private Paint mForePaint = new Paint();

        public VisualizerView(Context context) {
            super(context);
            init();
        }

        private void init() {
            mBytes = null;

            mForePaint.setStrokeWidth(1f);
            mForePaint.setAntiAlias(true);
            mForePaint.setColor(Color.rgb(150, 150, 255));
        }

        public void updateVisualizer(byte[] bytes) {
            mBytes = bytes;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (mBytes == null) {
                return;
            }

            if (mPoints == null || mPoints.length < mBytes.length * 4) {
                mPoints = new float[mBytes.length * 4];
            }

            mRect.set(0, 0, getWidth(), getHeight());

            for (int i = 0; i < mBytes.length - 1; i++) {
                mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
                mPoints[i * 4 + 1] = mRect.height() / 2
                        + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
                mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
                mPoints[i * 4 + 3] = mRect.height() / 2
                        + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
            }

            canvas.drawLines(mPoints, mForePaint);
        }
    }
}
