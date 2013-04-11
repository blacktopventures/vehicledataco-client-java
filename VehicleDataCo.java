/**
 * VehicleData.co Client - Java
 *
 * This is the source for the Java client for the VehicleData.co API, a service which
 * provides various pieces of vehicle-related data. It currently is focused on cars, but
 * is intended to be expanded to other vehicles types.
 *
 * For more information, please visit http://www.vehicledata.co.
 *
 * Copyright 2013 Blacktop Ventures LLC
 */

import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

import java.security.Key;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.xml.bind.DatatypeConverter;

import java.lang.StringBuilder;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.MalformedURLException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class VehicleDataCo {
	
	private final String VEHICLEDATACO_CLIENT_VERSION = "0.1";
	private String mVersion = "0.2";
    private String mHost = "api.vehicledata.co";
    private String mAppkey = null;
    private String mSecret = null;
    private boolean mInitialized = false;

	private HttpURLConnection mConnection = null;

    public VehicleDataCo (String appkey, String secret) {
    	mAppkey = appkey;
    	mSecret = secret;

    	mInitialized = true;
    }

    public VehicleDataCo (String appkey, String secret, String version) {
    	mAppkey = appkey;
    	mSecret = secret;
		mVersion = version;

    	mInitialized = true;
    }

    public JSONObject makes_getAll() {
    	if (!mInitialized) return null;
    	return doCall("makes.getAll");
    }

    public JSONObject makes_getInfo(String make) {
    	if (!mInitialized) return null;
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("make", make);
    	return doCall("makes.getInfo", args);
    }

    public JSONObject service_getSupportedFunctions() {
    	if (!mInitialized) return null;
    	return doCall("service.getSupportedFunctions");
    }

    public JSONObject vehicles_getMakes(String year) {
    	if (!mInitialized) return null;
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("year", year);
    	return doCall("vehicles.getMakes", args);
    }

    public JSONObject vehicles_getModels(String year, String make) {
    	if (!mInitialized) return null;
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("year", year);
    	args.put("make", make);
    	return doCall("vehicles.getModels", args);
    }

    public JSONObject vehicles_getTrims(String year, String make, String model) {
    	if (!mInitialized) return null;
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("year", year);
    	args.put("make", make);
    	args.put("model", model);
    	return doCall("vehicles.getTrims", args);
    }

    public JSONObject vehicles_getStyleTrims(String year, String make, String model, String trim) {
    	if (!mInitialized) return null;
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("year", year);
    	args.put("make", make);
    	args.put("model", model);
    	args.put("trim", trim);
    	return doCall("vehicles.getStyleTrims", args);
    }

    public JSONObject vehicles_getTransmissions(String year, String make, String model, String style, String trim) {
    	if (!mInitialized) return null;
    	HashMap<String, String> args = new HashMap<String, String>();
    	args.put("year", year);
    	args.put("make", make);
    	args.put("model", model);
    	args.put("style", style);
    	args.put("trim", trim);
    	return doCall("vehicles.getTransmissions", args);
    }

    public JSONObject vin_decode(String vin) {
        if (!mInitialized) return null;
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("vin", vin);
        return doCall("vin.decode", args);
    }

    private JSONObject doCall (String function) {
    	return doCall (function, null);
    }

    private JSONObject doCall (String function, HashMap args) {
		Date d = new Date();
		String sargs = "";

		if (args != null)
			sargs = (new JSONObject(args)).toString();

		String uri = null;
		try {
			uri = "app=" + mAppkey + "&v=" + mVersion + "&t=" + d.getTime() + "&f=" + function + "&a=" +
				URLEncoder.encode(sargs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return (returnError("Encoding error"));
		}

    	String hash = null;
    	try {
	    	hash = getHash(uri);
	    } catch (SecurityException e) {
	    	return (returnError("Hashing error"));
	    }

		// do remote call
		try {
			URL serverAddress = new URL("http://" + mHost + "/?" + uri + "&hash=" + URLEncoder.encode(hash, "UTF-8"));
			mConnection = (HttpURLConnection)serverAddress.openConnection();
			mConnection.setRequestMethod("GET");
			mConnection.setDoOutput(true);
			mConnection.setReadTimeout(5000);
			mConnection.connect();

			BufferedReader rd  = new BufferedReader(new InputStreamReader(mConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();

			String line = "";
			while ((line = rd.readLine()) != null) {
				sb.append(line + '\n');
			}

			JSONValue val = new JSONValue();
			JSONObject ret = (JSONObject) JSONValue.parse(sb.toString());
			return new JSONObject(ret);
		} catch (MalformedURLException e) {
			return (returnError("Invalid URL"));
		} catch (IOException e) {
			return (returnError("Unable to connect"));
		} 
    }

    private String getHash(String uri) {
		try {
			Key sk = new SecretKeySpec(mSecret.getBytes(), "HmacSHA256");
			Mac mac = Mac.getInstance(sk.getAlgorithm());
			mac.init(sk);
			final byte[] hmac = mac.doFinal(uri.getBytes());
			return DatatypeConverter .printBase64Binary(hmac);
		} catch (NoSuchAlgorithmException e1) {
			return null;
		} catch (InvalidKeyException e) {
			return null;
		}
	}

	private JSONObject returnError(String msg) {
    	HashMap<String, String> ret = new HashMap<String, String>();
    	ret.put("status", "fail");
    	ret.put("error", "-1");
    	ret.put("message", msg);
    	return new JSONObject(ret);
	}
}