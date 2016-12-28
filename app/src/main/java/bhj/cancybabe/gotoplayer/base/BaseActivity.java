package bhj.cancybabe.gotoplayer.base;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import bhj.cancybabe.gotoplayer.R;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Cancybabe on 2016/11/7.
 */
public class BaseActivity extends AppCompatActivity {

    public void toast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }

    public View findById(int id){
        return findViewById(id);
    }

    /**
     * 弹出对话框
     */

    public void showDilaog() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF567F"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
