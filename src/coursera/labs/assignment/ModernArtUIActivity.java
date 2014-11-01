package coursera.labs.assignment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.SeekBar;
import android.widget.TextView;


public class ModernArtUIActivity extends Activity {
    private static final String MORE_INFO_URL = "http://www.moma.org";
    private final String TAG = "ModernArtUIActivity";

    SeekBar seekBar;

    TextView []textViews = new TextView[5];
    int indexOfTextViewWithWhiteBg = -1;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        seekBar= (SeekBar) findViewById(R.id.seekBar);
        textViews[0] = (TextView) findViewById(R.id.textView1);
        textViews[1] = (TextView) findViewById(R.id.textView2);
        textViews[2] = (TextView) findViewById(R.id.textView3);
        textViews[3] = (TextView) findViewById(R.id.textView4);
        textViews[4] = (TextView) findViewById(R.id.textView5);
        // Find the id of the text view with white color

        for (int i = 0; i< textViews.length;i++){
            if (textViews[i].getBackground() instanceof ColorDrawable
                    && ((ColorDrawable) textViews[i].getBackground()).getColor() == Color.WHITE) {
                indexOfTextViewWithWhiteBg = i;
                Log.i(TAG,"Found Text view " + textViews[i].getId() + " with white bg");
                break;
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TAG, "Progress : " + progress);
                for (int i = 0; i < textViews.length; i++) {
                    final TextView currentTextView = textViews[i];
                    if (i != indexOfTextViewWithWhiteBg &&
                            currentTextView.getBackground() instanceof ColorDrawable) {
                        ColorDrawable bgColorDraw = (ColorDrawable) currentTextView.getBackground();
                        currentTextView.setBackgroundColor(bgColorDraw.getColor() + progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.i(TAG,"SeekBar:onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.i(TAG,"SeekBar:onStopTrackingTouch");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = ModernArtUIActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int progress = seekBar.getProgress();
        editor.putInt(getString(R.string.current_slider_level), progress);
        Log.i(TAG,"onPause-Saving current progress level : " + progress);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = ModernArtUIActivity.this.getPreferences(Context.MODE_PRIVATE);
        int progress = sharedPref.getInt(getString(R.string.current_slider_level),0);
        Log.i(TAG,"onResume - Retrive progress level " + progress);
        seekBar.setProgress(progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more_info) {
            Log.i(TAG,"Displaying the More info dialog");
            Context context = ModernArtUIActivity.this.getApplicationContext();
            LayoutInflater myLayout = LayoutInflater.from(context);
            final View dialogView = myLayout.inflate(R.layout.dialog_details, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ModernArtUIActivity.this)
                    .setView(dialogView);

            alertDialogBuilder.setNegativeButton(R.string.dialog_visit_moma, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(TAG,"Showing the the More info webpage");
                    openMoreInfoWebpage();
                }
            });
            alertDialogBuilder.setPositiveButton(R.string.dialog_not_now, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(TAG,"Closing the dialog");
                    dialog.dismiss();
                }
            });

            alertDialogBuilder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openMoreInfoWebpage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(MORE_INFO_URL));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
