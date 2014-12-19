package com.cordova.sendmenu;

import java.util.HashMap;
import java.util.Map;

import org.apache.cordova.CordovaActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.apache.cordova.PluginResult;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.cordova.file.LocalFilesystemURL;
import org.apache.cordova.file.FileUtils;

public class SendMenu extends CordovaPlugin {

    private CallbackContext onNewIntentCallbackContext = null;

    private static String selectedImagePath;
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //public boolean execute(String action, JSONArray args, String callbackId) {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        try {

            if (action.equals("startActivity")) {
                if (args.length() != 1) {
                    //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }

                // Parse the arguments
                final CordovaResourceApi resourceApi = webView.getResourceApi();
                JSONObject obj = args.getJSONObject(0);
                String type = obj.has("type") ? obj.getString("type") : null;
                Uri uri = obj.has("url") ? resourceApi.remapUri(Uri.parse(obj.getString("url"))) : null;
                JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
                Map<String, String> extrasMap = new HashMap<String, String>();

                // Populate the extras if any exist
                if (extras != null) {
                    JSONArray extraNames = extras.names();
                    for (int i = 0; i < extraNames.length(); i++) {
                        String key = extraNames.getString(i);
                        String value = extras.getString(key);
                        extrasMap.put(key, value);
                    }
                }

                startActivity(obj.getString("action"), uri, type, extrasMap);
                //return new PluginResult(PluginResult.Status.OK);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                return true;

            } else if (action.equals("hasExtra")) {
                if (args.length() != 1) {
                    //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }
                Intent i = ((CordovaActivity)this.cordova.getActivity()).getIntent();
                String extraName = args.getString(0);
                //return new PluginResult(PluginResult.Status.OK, i.hasExtra(extraName));
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, i.hasExtra(extraName)));
                return true;

            } else if (action.equals("getExtra")) {
                if (args.length() != 1) {
                    //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }
                Intent i = ((CordovaActivity)this.cordova.getActivity()).getIntent();
                String extraName = args.getString(0);
                if (i.hasExtra(extraName)) {
                    Uri r = ((Uri) i.getParcelableExtra(extraName));
                    selectedImagePath = getRealPathFromUri(this.cordova.getActivity().getApplicationContext(),r);
                    // Get the File plugin from the plugin manager
                    FileUtils filePlugin = (FileUtils)webView.pluginManager.getPlugin("File");
                    // Get a LocalFilesystemURL object for a device path,
                    // or null if it cannot be represented as a cdvfile URL.
                    LocalFilesystemURL url = filePlugin.filesystemURLforLocalPath(selectedImagePath);
                    // Get the string representation of the URL object
                    String cdvfileURL = url.toString();
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, cdvfileURL));
                    return true;
                } else {
                    //return new PluginResult(PluginResult.Status.ERROR);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR));
                    return false;
                }
            } else if (action.equals("getUri")) {
                if (args.length() != 0) {
                    //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }

                Intent i = ((CordovaActivity)this.cordova.getActivity()).getIntent();
                String uri = i.getDataString();
                //return new PluginResult(PluginResult.Status.OK, uri);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, uri));
                return true;
            } else if (action.equals("onNewIntent")) {
                //save reference to the callback; will be called on "new intent" events
                this.onNewIntentCallbackContext = callbackContext;
        
                if (args.length() != 0) {
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }
                
                PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
                result.setKeepCallback(true); //re-use the callback on intent events
                callbackContext.sendPluginResult(result);
                return true;
                //return result;
            } else if (action.equals("sendBroadcast")) 
            {
                if (args.length() != 1) {
                    //return new PluginResult(PluginResult.Status.INVALID_ACTION);
                    callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
                    return false;
                }

                // Parse the arguments
                JSONObject obj = args.getJSONObject(0);

                JSONObject extras = obj.has("extras") ? obj.getJSONObject("extras") : null;
                Map<String, String> extrasMap = new HashMap<String, String>();

                // Populate the extras if any exist
                if (extras != null) {
                    JSONArray extraNames = extras.names();
                    for (int i = 0; i < extraNames.length(); i++) {
                        String key = extraNames.getString(i);
                        String value = extras.getString(key);
                        extrasMap.put(key, value);
                    }
                }

                sendBroadcast(obj.getString("action"), extrasMap);
                //return new PluginResult(PluginResult.Status.OK);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
                return true;
            }
            //return new PluginResult(PluginResult.Status.INVALID_ACTION);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.INVALID_ACTION));
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            String errorMessage=e.getMessage();
            //return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION,errorMessage));
            return false;
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
         
        if (this.onNewIntentCallbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, intent.getDataString());
            result.setKeepCallback(true);
            this.onNewIntentCallbackContext.sendPluginResult(result);
        }
    }

    void startActivity(String action, Uri uri, String type, Map<String, String> extras) {
        Intent i = (uri != null ? new Intent(action, uri) : new Intent(action));
        
        if (type != null && uri != null) {
            i.setDataAndType(uri, type); //Fix the crash problem with android 2.3.6
        } else {
            if (type != null) {
                i.setType(type);
            }
        }
        
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            // If type is text html, the extra text must sent as HTML
            if (key.equals(Intent.EXTRA_TEXT) && type.equals("text/html")) {
                i.putExtra(key, Html.fromHtml(value));
            } else if (key.equals(Intent.EXTRA_STREAM)) {
                // allowes sharing of images as attachments.
                // value in this case should be a URI of a file
                final CordovaResourceApi resourceApi = webView.getResourceApi();
                i.putExtra(key, resourceApi.remapUri(Uri.parse(value)));
            } else if (key.equals(Intent.EXTRA_EMAIL)) {
                // allows to add the email address of the receiver
                i.putExtra(Intent.EXTRA_EMAIL, new String[] { value });
            } else {
                i.putExtra(key, value);
            }
        }
        ((CordovaActivity)this.cordova.getActivity()).startActivity(i);
    }

    void sendBroadcast(String action, Map<String, String> extras) {
        Intent intent = new Intent();
        intent.setAction(action);
        for (String key : extras.keySet()) {
            String value = extras.get(key);
            intent.putExtra(key, value);
        }

        ((CordovaActivity)this.cordova.getActivity()).sendBroadcast(intent);
    }
}