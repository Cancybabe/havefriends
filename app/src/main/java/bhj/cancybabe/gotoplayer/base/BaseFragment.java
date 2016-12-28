package bhj.cancybabe.gotoplayer.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Cancybabe on 2016/11/10.
 */
public class BaseFragment extends Fragment {
    public BaseActivity act;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        act = (BaseActivity) activity;
    }

    public void toast(String str){
        Toast.makeText(act,str,Toast.LENGTH_SHORT).show();
    }

    public View findById(int id){
        return act.findViewById(id);
    }



    public void showDilaog(SweetAlertDialog pDialog) {
        pDialog = new SweetAlertDialog(act, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF567F"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
