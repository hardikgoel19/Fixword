package game.word.fix.com.fixword;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastShow {

    public static final int SUCCESS = 1;
    public static final int ERROR = 2;

    public static void showToast(Context context,int TYPE, String Message){
        if(TYPE == SUCCESS){
            View view = LayoutInflater.from(context).inflate(R.layout.toast_success,null,false);
            TextView tv = view.findViewById(R.id.textsuccess);
            tv.setText(Message);
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
        }
        else if(TYPE == ERROR){
            View view = LayoutInflater.from(context).inflate(R.layout.toast_error,null,false);
            TextView tv = view.findViewById(R.id.texterror);
            tv.setText(Message);
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
        }
        else{
            Toast.makeText(context, Message, Toast.LENGTH_SHORT).show();
        }
    }

}
