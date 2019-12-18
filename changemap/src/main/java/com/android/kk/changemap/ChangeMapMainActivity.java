package com.android.kk.changemap;

import android.Manifest;
import android.app.DialogFragment;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.kk.carapplication.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeMapMainActivity extends AppCompatActivity implements ChangeMapDialogFragment.NoticeDialogListener{

    public static final String SEP = "_";

    public static class Vendors {
        public static final List<String> vendors = Arrays.asList("HR", "TT", "HU");
        public static String next(String old) {
            int indOld = vendors.indexOf(old);
            int indNew = (indOld + 1) % vendors.size();
            return vendors.get(indNew);
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
//    public static final String STORAGE = "/storage";
    public static final String STORAGE = "/sdcard";
    private static final String FIND_IGO_SYSTXT = "find -L " + STORAGE + " -name sys.txt";
    private static final Pattern pattern1 = Pattern.compile(".*", Pattern.DOTALL);
    public static final String REGEX = "(.*content=\")(.+?)(\".*)";
    private static final Pattern pattern = Pattern.compile(REGEX, Pattern.DOTALL);

    private String sysTxtOut = null;
    private String oldVendor = null;
    private String newVendor = null;
    private String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_map_main);

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

        try {
            Process process = Runtime.getRuntime().exec(FIND_IGO_SYSTXT);
            String inpStream = MainActivity.readFullyAsString(process.getInputStream(), Charset.defaultCharset().name());
            String errStream = MainActivity.readFullyAsString(process.getErrorStream(), Charset.defaultCharset().name());
            Log.d(MainActivity.TAG, inpStream);
            Log.d(MainActivity.TAG, errStream);
            String[] split = inpStream.split("\n");
            for (String line: split) {
                if (line.startsWith(STORAGE)) {
                    changeSysTxt(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//    FileChooserDialog dialog = new FileChooserDialog(this);
//    dialog.loadFolder(Environment.getExternalStorageDirectory() + "/Download/");
//    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
//		public void onFileSelected(Dialog source, File file) {
//			source.hide();
//			Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
//			toast.show();
//		}
//		public void onFileSelected(Dialog source, File folder, String name) {
//			source.hide();
//			Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
//			toast.show();
//		}
//    });
//	dialog.show();

//    Intent intent = new Intent(this, FileChooserActivity.class);
//    intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory());
//        this.startActivityForResult(intent, 0);
//    }
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    if (resultCode == Activity.RESULT_OK) {
//	    	boolean fileCreated = false;
//	    	String filePath = "";
//
//	    	Bundle bundle = data.getExtras();
//	        if(bundle != null)
//	        {
//	        	if(bundle.containsKey(FileChooserActivity.OUTPUT_NEW_FILE_NAME)) {
//	        		fileCreated = true;
//	        		File folder = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
//	        		String name = bundle.getString(FileChooserActivity.OUTPUT_NEW_FILE_NAME);
//	        		filePath = folder.getAbsolutePath() + "/" + name;
//	        	} else {
//	        		fileCreated = false;
//	        		File file = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
//	        		filePath = file.getAbsolutePath();
//	        	}
//	        }
//
//	        String message = fileCreated? "File created" : "File opened";
//	        message += ": " + filePath;
//	    	Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
//			toast.show();
//	    }
    }

    private void checkPermission(String permission, int callBack) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, ask it.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    callBack);
        }
    }

    private void changeSysTxt(String fileName) {
        this.fileName = fileName;
        String sysTxt = null;
        File file = new File(fileName);
        try (FileInputStream inputStream = new FileInputStream(file)){
            sysTxt = MainActivity.readFullyAsString(inputStream, Charset.defaultCharset().name());
            Matcher matcher = pattern.matcher(sysTxt);
            if (matcher.matches()) {
                String content = matcher.group(2);
                Log.d(MainActivity.TAG, "old content: " + content);
                String[] split = content.split(SEP);
                if (split.length == 2)  {
                    oldVendor= split[1];
                    newVendor = Vendors.next(oldVendor);
                    sysTxtOut = changeSysTxt(matcher, split, newVendor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sysTxtOut != null) {
            ChangeMapDialogFragment dialog = new ChangeMapDialogFragment();

            Bundle args = new Bundle();
            args.putString("fileName", fileName);
            args.putString("oldVendor", oldVendor);
            args.putString("newVendor", newVendor);
            dialog.setArguments(args);
            dialog.show(this.getFragmentManager(), "ChangeMapDialogFragment");
        }
    }

    private String changeSysTxt(Matcher matcher, String[] split, String vendor) {
        String ret = matcher.group(1) + split[0] + SEP + vendor + matcher.group(3);
        return ret;
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        try (PrintStream out = new PrintStream(new FileOutputStream(fileName))) {
            out.print(sysTxtOut);
            String msg = "új térképszolgáltató: " + newVendor;
            Log.d(MainActivity.TAG, msg);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
        finish();
    }

}
