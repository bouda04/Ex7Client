package com.example.ex7.client;

import java.util.StringTokenizer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity implements OnClickListener{

	private final int MY_PERMISSIONS_REQUEST_CALL_PHONE =1 ;

	static final int EXTERNAL_CONTACT_REQUEST = 1;  // The request code
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button send=(Button)findViewById(R.id.send);
        send.setOnClickListener(this);
        
        Button surf=(Button)findViewById(R.id.surf);
        surf.setOnClickListener(this); 
        
        Button call=(Button)findViewById(R.id.call);
        call.setOnClickListener(this); 
        
        Button register=(Button)findViewById(R.id.register);
        register.setOnClickListener(this); 
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& permissions[0].equals(Manifest.permission.CALL_PHONE)&&
						grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					callPhone();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@SuppressWarnings({"MissingPermission"})
	private void callPhone(){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		String phone = ((EditText)findViewById(R.id.phone)).getText().toString();
		callIntent.setData(Uri.parse("tel:"+phone));
		startActivity(callIntent);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.call:

			int permissionCheck = checkSelfPermission(Manifest.permission.WRITE_CALENDAR);
			if (permissionCheck == PackageManager.PERMISSION_GRANTED)
				callPhone();
			else{
				requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
			}
			break;
		case R.id.surf:
	        String url = ((EditText)findViewById(R.id.url)).getText().toString();
	        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" +url));
	        startActivity(browserIntent);
			break;
		case R.id.send:
	        String[] address = new String[] {((EditText)findViewById(R.id.email)).getText().toString()};
	        Intent intent = new Intent(Intent.ACTION_SEND);
	        intent.setType("plain/text");
	        intent.putExtra(Intent.EXTRA_EMAIL, address);
	        intent.putExtra(Intent.EXTRA_SUBJECT, "<subject...>");
			intent.putExtra(Intent.EXTRA_TEXT, "<mail body...>");
	        startActivity(Intent.createChooser(intent, ""));
			break;
		case R.id.register:
	    	Intent nextIntent = new Intent("com.action.register"); //implicit intent
//	    	nextIntent.setType("text/plain");
	    	String strContact = ((EditText)findViewById(R.id.contact)).getText().toString();
	    	if (strContact.isEmpty());
	    	else {
	    		String[] tokens = strContact.split(" +", 3);
	    		if (tokens.length==3){
					nextIntent.putExtra("gender", tokens[0]);
			    	nextIntent.putExtra("fname", tokens[1]);
					nextIntent.putExtra("lname", tokens[2]);
	    		}
	    	}
	    	startActivityForResult(nextIntent,EXTERNAL_CONTACT_REQUEST);
			break;
		}
		
		/*
        Intent read1=new Intent();
        read1.setAction(android.content.Intent.ACTION_VIEW);
        read1.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivity(read1);
        
        
        */		
	}//onClick()
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == EXTERNAL_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
            	
    			
        		String fnString = data.getExtras().getString("fname");
           		String lnString = data.getExtras().getString("lname");
           		String strGender = data.getExtras().getString("gender");
           		((EditText)findViewById(R.id.contact)).setText(strGender + " " + fnString + " " +lnString);
           }
        }
    }

	
}
