package bhj.cancybabe.gotoplayer.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bhj.cancybabe.gotoplayer.R;

/**
 * Created by Cancybabe on 2016/11/9.
 */
public class MessageFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,null);
        return view;
    }
}

