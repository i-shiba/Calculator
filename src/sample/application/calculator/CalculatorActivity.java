package sample.application.calculator;

//import sample.application.memopad.R;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Selection;

public class CalculatorActivity extends Activity {
	
	String strTemp   = "";
	String strResult = "0";
	Integer operator = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        readPreferences();
    }
    
    @Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
        writePreferences();
	}

	public void numKeyOnClick(View v){
    	String strInKey = (String) ((Button)v).getText();
    	
    	if(strInKey.equals(".")){
    		if(this.strTemp.length() == 0){
    			this.strTemp = "0.";
    		}else{
    			if(this.strTemp.indexOf(".") == -1){
    				this.strTemp = this.strTemp + ".";
    			}else{
    				this.strTemp = this.strTemp + strInKey;
    			}
    		}
    	}else{
    		this.strTemp = this.strTemp + strInKey;
    	}
    	this.showNumber(this.strTemp);
/*    	
    	Button button = (Button)v;
     	Log.d("[buttonのtext]",button.getText().toString());
     	TextView tv = (TextView) this.findViewById(R.id.displayPanel);
     	String str = button.getText().toString();
     	String strtv = tv.getText().toString();
     	Log.d("[tvのtext]",tv.getText().toString());
     	if(strtv.equals("0")){
     		if(str.equals(".")){
         		tv.setText(strtv + str);
    		}else{
    			tv.setText(str);
    		}
     	}else{
     		tv.setText(strtv + str);
     	}
*/
    }
    
    private void showNumber(String strNum){
    	DecimalFormat form = new DecimalFormat("#,##0");
    	String strDecimal = "";
    	String strInt = "";
    	String fText = "";
    	
    	if(strNum.length() > 0){
    		int decimalPoint = strNum.indexOf(".");
    		if(decimalPoint > -1){
    			strDecimal = strNum.substring(decimalPoint);
    			strInt = strNum.substring(0,decimalPoint);
    		}else{
    			strInt = strNum;
    		}
    		fText = form.format(Double.parseDouble(strInt)) + strDecimal;
    	}else{
    		fText = "0";
    	}
    	((TextView)findViewById(R.id.displayPanel)).setText(fText);
    }
    
    public void operatorKeyOnClick(View v){
    	if(this.operator != 0){
    		if(this.strTemp.length() > 0){
    			this.strResult = this.doCalc();
    			this.showNumber(this.strResult);
    		}
    	}else{
    		if(this.strTemp.length() > 0){
    			this.strResult = this.strTemp;
    		}
    	}
    	this.strTemp = "";
    	if(v.getId() == R.id.keypadEq){
    		this.operator = 0;
    	}else{
    		this.operator = v.getId();
    	}
    }
    
    private String doCalc(){
    	BigDecimal bd1 = new BigDecimal(strResult);
    	BigDecimal bd2 = new BigDecimal(strTemp);
    	BigDecimal result = BigDecimal.ZERO;
    	
    	switch(this.operator){
    	case R.id.keypadAdd:
    		result = bd1.add(bd2);
    		break;
    	case R.id.keypadSub:
    		result = bd1.subtract(bd2);
    		break;
    	case R.id.keypadMulti:
    		result = bd1.multiply(bd2);
    		break;
    	case R.id.keypadDiv:
    		if(!bd2.equals(BigDecimal.ZERO)){
    			result = bd1.divide(bd2, 12, 3);
    		}else{
    			Toast toast = Toast.makeText(this, R.string.toast_div_by_zero, 1000);
    			toast.show();
    		}
    		break;
    	}
    	
    	if(result.toString().indexOf(".") >= 0){
    		return result.toString().replaceAll("¥¥.0+$|0+$", "");
    	}else{
    		return result.toString();
    	}
    }
    
    public void writePreferences(){
    	SharedPreferences prefs = getSharedPreferences("CalcPrefs", MODE_PRIVATE);
    	SharedPreferences.Editor editor = prefs.edit();
    	editor.putString("strTemp", strTemp);
    	editor.putString("strResult", strResult);
    	editor.putInt("operator", operator);
    	editor.putString("strDisplay", ((TextView)findViewById(R.id.displayPanel)).getText().toString());
    	editor.commit();
    }
    
    public void readPreferences(){
    	SharedPreferences prefs = getSharedPreferences("CalcPrefs", MODE_PRIVATE);
    	strTemp = prefs.getString("strTemp", "");
    	strResult = prefs.getString("strResult", "0");
    	operator = prefs.getInt("operator", 0);
    	((TextView)findViewById(R.id.displayPanel)).setText(prefs.getString("strDisplay", "0"));
    }
}
