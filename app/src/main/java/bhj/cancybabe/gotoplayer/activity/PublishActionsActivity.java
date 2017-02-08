package bhj.cancybabe.gotoplayer.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.baidu.mapapi.search.core.PoiInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import bhj.cancybabe.gotoplayer.R;
import bhj.cancybabe.gotoplayer.adpter.PublishActSpinnerAdapter;
import bhj.cancybabe.gotoplayer.application.MyApplication;
import bhj.cancybabe.gotoplayer.base.BaseActivity;
import bhj.cancybabe.gotoplayer.base.BaseInterface;
import bhj.cancybabe.gotoplayer.bean.UserActivtiesInfo;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by Cancybabe on 2016/11/13.
 */
public class PublishActionsActivity extends BaseActivity implements BaseInterface,View.OnClickListener{
    private  RelativeLayout mRelativeLayoutTime,mRelativeLayoutSpinner,mRelativeLayoutPlace;
    private LinearLayout mLinearLayoutTopPics,mLinearLayoutButtomPics;
    private TextView mTvTime,mTvplace,mEdMoney;
    private EditText mEdActionName,mEdActionDesc;
    private Spinner mSpinner;
    private String[] datas = {"周边","少儿","DIY","健身","赶集","演出",
            "展览","沙龙","品茶","聚会"};
    private int[] images = {R.drawable.more_zhoubian,R.drawable.more_shaoer,R.drawable.more_diy,
            R.drawable.more_jianshen,R.drawable.more_jishi,R.drawable.more_yanchu,
            R.drawable.more_zhanlan,R.drawable.more_shalong,R.drawable.more_pincha,R.drawable.more_juhui};
    private File saveUserActionFile = new File("sdcard/userAction.jpg");
    private int REQUESD_CODE_SAVEACYION =2;//跳转相册返回发布界面的返回码
    private int ALBUM_OK = 3; //跳转相册返回发布界面的返回码
    private ArrayList<Bitmap> bitmaps;//展示到界面上的图片集合
    private  String[] filePaths;//上传bmob云的文件的路径
    private ImageView ivAdd;//添加图片的图片
    private int width;//图片的宽
    private int height;//图片的高
    private int currentActionType = 0;//
    private int picPaddingLeftAndRight;
    private int picBetween = 40;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initListener();
        mSpinner.setAdapter(new PublishActSpinnerAdapter(this,datas,images));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentActionType = i;
                toast("currentActionType"+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void initView() {
        mRelativeLayoutTime = (RelativeLayout) findById(R.id.act_publish_layout_time);
        mRelativeLayoutSpinner = (RelativeLayout) findById(R.id.act_publish_layout_spinner);
        mLinearLayoutTopPics = (LinearLayout) findById(R.id.act_publish_layouttop_pics);
        mLinearLayoutButtomPics = (LinearLayout) findById(R.id.act_publish_layoutbuttom_pics);
        mRelativeLayoutPlace = (RelativeLayout) findById(R.id.act_publish_relativelayout_place);
        mTvTime = (TextView) findById(R.id.act_publish_tv_time);
        mSpinner = (Spinner) findViewById(R.id.act_publish_spinner);
        mTvplace = (TextView) findById(R.id.act_publish_tv_place);
        mEdMoney = (TextView) findById(R.id.act_publish_et_money);
        mEdActionName = (EditText) findById(R.id.act_publish_ed_actionname);
        mEdActionDesc = (EditText) findById(R.id.act_publish_et_actiondesc);
        showAddPic();
    }


    /**
     * 展示add图片并添加监听
     */

    private void showAddPic() {
        WindowManager windowManager =  getWindowManager();
        width =  windowManager.getDefaultDisplay().getWidth()/4;
        height = width;
        picPaddingLeftAndRight = (width*4-(width*3)-picBetween*2-20)/2;
        Log.i("myTag",""+width);

        ivAdd = new ImageView(this);
        ivAdd.setLayoutParams(new LinearLayout.LayoutParams(width,width));
        ivAdd.setImageResource(R.drawable.act_publish_add);
        mLinearLayoutTopPics.addView(ivAdd);

        bitmaps = new ArrayList<>();
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转相册去选择图片
                jumpToChoseAndCropPics();
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.h);
//                bitmaps.add(bitmap);
            }
        });
    }

    /**
     * 
     * 跳转相册并裁剪活动图片
     */
    private void jumpToChoseAndCropPics() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setType("image/*"); // 开启Pictures画面Type设定为image
        intent.setAction(Intent.ACTION_GET_CONTENT); // 使用Intent.ACTION_GET_CONTENT这个Action
        startActivityForResult(intent, REQUESD_CODE_SAVEACYION);
    }

/**
 * 质量压缩图片再上传
 */
    public Bitmap qualityCompressPic(Bitmap bitmap){
        ByteArrayOutputStream baso = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baso);
        int options = 100;
        while(baso.toByteArray().length/1024>100){//循环判断如果压缩后的图片大于100kb则继续压缩
            baso.reset();//重置baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,options,baso);
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baso.toByteArray());
        Bitmap bitmap1 = BitmapFactory.decodeStream(isBm,null,null);//把ByteArrayInputStream生成图片
        return bitmap1;
    }

/**
 * 图片大小压缩
 */

    public Bitmap sizeCompressPic(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        if (baos.toByteArray().length/1024 >1024){//判断图片大于1M,进行压缩防止内存溢出
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);//压缩到50%
        }
        //将图片转换成流
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap1 = BitmapFactory.decodeStream(isBm,null,options);
       //获取图片的宽高
        int bitWidth = options.outWidth;
        int bitHeight =  options.outHeight;
        //获取屏幕的宽高
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        Point outSize = new Point();
        display.getSize(outSize);
        int screenWidth = outSize.x;
        int screenHeight = outSize.y;
        //计算压缩比
        int x =  bitWidth/(width);
        int y = bitHeight/(width);
        int ratio = x > y ? x: y;
        if(ratio < 1){
            ratio = 1;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = ratio;//设置缩放比例
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap1 = BitmapFactory.decodeStream(isBm,null,options);
        return bitmap1;
    }

/**
 * 裁剪图片后带数据返回
 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESD_CODE_SAVEACYION){
            ContentResolver resolver = getContentResolver();
            Uri originalUri = null;
            if(data != null) {
                originalUri = data.getData();
            }
            Bitmap photo = null;
            try {
               photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //保存进集合
           //Bitmap bitmap =  BitmapFactory.decodeFile(saveUserActionFile.getAbsolutePath());
            //图片压缩之后再进行保存
            Bitmap bitmap  = qualityCompressPic(photo);
            bitmaps.add(bitmap);
            if(bitmaps.size() == 0){
                LinearLayout.LayoutParams addLayoutParams = new LinearLayout.LayoutParams(width,height);
                addLayoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                ivAdd.setLayoutParams(addLayoutParams);
                mLinearLayoutTopPics.addView(ivAdd);

            }else if (bitmaps.size() < 3){
                mLinearLayoutTopPics.removeAllViews();
                for (int i = 0;i < bitmaps.size(); i++){
                    ImageView iv = new ImageView(PublishActionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                    LinearLayout.LayoutParams addLayoutParams = new LinearLayout.LayoutParams(width,height);
                    addLayoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    ivAdd.setLayoutParams(addLayoutParams);
                    if(i==0){
                        layoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    }else{
                        layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    }
                    Bitmap sizebitmap = sizeCompressPic(bitmaps.get(i));
                    iv.setImageBitmap(sizebitmap);
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv.setLayoutParams(layoutParams);
                    mLinearLayoutTopPics.addView(iv);
                }
                mLinearLayoutTopPics.addView(ivAdd);
            }else if (bitmaps.size() == 3){
                mLinearLayoutTopPics.removeAllViews();
                for (int i = 0;i < bitmaps.size(); i++){
                    ImageView iv = new ImageView(PublishActionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                    LinearLayout.LayoutParams addLayoutParams = new LinearLayout.LayoutParams(width,height);
                    addLayoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    ivAdd.setLayoutParams(addLayoutParams);
                    if(i==0){
                        layoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    }else if(i==2){
                        layoutParams.setMargins(picBetween/2,picBetween/2,picPaddingLeftAndRight,picBetween/2);
                    }else{
                        layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    }
                    iv.setImageBitmap(bitmaps.get(i));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv.setLayoutParams(layoutParams);
                    mLinearLayoutTopPics.addView(iv);
                }
                mLinearLayoutButtomPics.addView(ivAdd);
            }else if(bitmaps.size() < 6){
                mLinearLayoutTopPics.removeAllViews();
                mLinearLayoutButtomPics.removeAllViews();
                for (int i = 0;i < 3; i++){
                    ImageView iv = new ImageView(PublishActionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);

                    if(i==0){
                        layoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    }else if(i==2){
                        layoutParams.setMargins(picBetween/2,picBetween/2,picPaddingLeftAndRight,picBetween/2);
                    }else{
                        layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    }

                    iv.setImageBitmap(bitmaps.get(i));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv.setLayoutParams(layoutParams);
                    mLinearLayoutTopPics.addView(iv);
                }

                for (int j = 3; j < bitmaps.size(); j++){
                    ImageView iv = new ImageView(PublishActionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                    LinearLayout.LayoutParams addLayoutParams = new LinearLayout.LayoutParams(width,height);
                    addLayoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    ivAdd.setLayoutParams(addLayoutParams);
                    if(j==3){
                        layoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    }else{
                        layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    }


                    iv.setImageBitmap(bitmaps.get(j));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv.setLayoutParams(layoutParams);
                    mLinearLayoutButtomPics.addView(iv);
                }
                mLinearLayoutButtomPics.addView(ivAdd);

            }else if (bitmaps.size() == 6){
                mLinearLayoutTopPics.removeAllViews();
                mLinearLayoutButtomPics.removeAllViews();
                for (int i = 0;i < 3; i++){
                    ImageView iv = new ImageView(PublishActionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                    layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    ivAdd.setLayoutParams(layoutParams);
                    if(i==0){
                        layoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    }else if(i==2){
                        layoutParams.setMargins(picBetween/2,picBetween/2,picPaddingLeftAndRight,picBetween/2);
                    }else{
                        layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    }

                    iv.setImageBitmap(bitmaps.get(i));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv.setLayoutParams(layoutParams);
                    mLinearLayoutTopPics.addView(iv);
                }

                for (int j = 3; j < bitmaps.size(); j++){
                    ImageView iv = new ImageView(PublishActionsActivity.this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
                    layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    ivAdd.setLayoutParams(layoutParams);
                    if(j==3){
                        layoutParams.setMargins(picPaddingLeftAndRight,picBetween/2,picBetween/2,picBetween/2);
                    }else if(j==5){
                        layoutParams.setMargins(picBetween/2,picBetween/2,picPaddingLeftAndRight,picBetween/2);
                    }else{
                        layoutParams.setMargins(picBetween/2,picBetween/2,picBetween/2,picBetween/2);
                    }

                    iv.setImageBitmap(bitmaps.get(j));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    iv.setLayoutParams(layoutParams);
                    mLinearLayoutButtomPics.addView(iv);
                }
            }
        }
    }


    @Override
    public void initListener() {
        mRelativeLayoutTime.setOnClickListener(this);
        mRelativeLayoutSpinner.setOnClickListener(this);
        mRelativeLayoutPlace.setOnClickListener(this);
    }

    private PoiInfo mPoiInfo;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.act_publish_layout_time:
                toast("act_publish_layout_time");
                showtTimerpicker();
                break;
            case R.id.act_publish_relativelayout_place:
                toast("act_publish_relativelayout_place");
                Intent intent = new Intent(PublishActionsActivity.this,PublishActionBaiduMapactivity.class);
                startActivity(intent);
                PublishActionBaiduMapactivity.setonChosePoiInfoClick(new PublishActionBaiduMapactivity.onChosePoiInfoClickListener() {
                    @Override
                    public void setPoiInfo(String str,PoiInfo poiInfo) {
                        mTvplace.setText(str);
                        mPoiInfo = poiInfo;
                        Log.i("myTag","mPoiInfo.address"+mPoiInfo.address);
                    }
                });
                break;
        }
    }


    /**
     * 展示日期选择框
     */
    private void showtTimerpicker() {
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);
        final int hour = calender.get(Calendar.HOUR_OF_DAY);
        final int minute = calender.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialpg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                if (y<year){
                    toast("选择的年份有误，请重新选择");
                    return;
                }else if(y == year){
                    if (m < month){
                        toast("选择的月份有误，请重新选择");
                        return;
                    }else if(m == month){
                        if (d < day){
                            toast("选择的日期有误，请重新选择");
                            return;
                        }else {
                            String time = y + "." + m + "." + d + " ";
                            TimePickerDialog  timerPickerDialog = getTimePickerDialog(time,hour,minute);
                            timerPickerDialog.show();
                        }
                    }
                }
            }
        },year,month,day);
        datePickerDialpg.show();


    }

    /**
     * 展示时间选择框
     */
    @NonNull
    private TimePickerDialog getTimePickerDialog(final String time , final int hour, final int minute) {
        return new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                String timet = time + h + ":" + m;
                toast(timet);
                mTvTime.setText(timet);
            }
        }, hour, minute, true);
    }

    /**
     * 发布按钮被点击
     * @param view
     */
    private List<BmobFile> bmobFilelist;
    public void onPublishClick(View view){
        final String actionName = mEdActionName.getText().toString().trim();
        final String actionDesc = mEdActionDesc.getText().toString().trim();
        //获取spinner当前对象
        final String actionType = datas[currentActionType];
        final String actionPlace = mTvplace.getText().toString().trim();

        final String actionMoney = mEdMoney.getText().toString().trim();
        final String actionTime = mTvTime.getText().toString().trim();
        if (actionName == null || actionName.equals("")){
            toast("活动名不能为空");
            return;
        }
        if (actionDesc == null || actionDesc.equals("")){
            toast("活动简介不能为空");
            return;
        }

        if (actionPlace == null || actionPlace.equals("")){
            toast("活动地点不能为空");
            return;
        }
        if (actionTime == null || actionTime.equals("")){
            toast("活动时间不能为空");
            return;
        }

        if (bitmaps.size() != 0) {
            toast("bitmaps.size()"+bitmaps.size());
            filePaths = new String[bitmaps.size()];
            //获取存在arraylist中要上传的图片,保存到本地
            for (int i= 0; i < bitmaps.size(); i++){
                filePaths[i] = "sdcard/bmobfile"+i+".png";
                try {
                    bitmaps.get(i).compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(filePaths[i]));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            //批量上传文件
            BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
                int count = 0;
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    toast("批量上传文件成功");
                    Log.i("myTag","批量上传文件成功List<BmobFile> :" + list.size());
//                    bmobFilelist = list;
                    count++;
                    if (count == filePaths.length){
                        //把活动信息保存至服务器
                        saveUserActionsInfo(actionName,actionDesc,actionType,actionPlace,actionMoney,actionTime,list);
                    }
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                    Log.i("myTag","onProgress"+curIndex);
                    Log.i("myTag","onProgress"+curPercent);
                    Log.i("myTag","onProgress"+total);
                    Log.i("myTag","onProgress"+totalPercent);
                    //显示上传进度
                }

                @Override
                public void onError(int i, String s) {
                    toast("批量上传文件onError");
                }

            });

        }

    }

    /**
     * 封装对象,上传服务器
     * @param list
     */

    private void saveUserActionsInfo(String actionName,String actionDesc,String actionType,
                                     String actionPlace,String actionMoney, String actionTime,List<BmobFile> list) {

        //上传活动信息
        UserActivtiesInfo activitiseInfo = new UserActivtiesInfo();
        activitiseInfo.setActionClass(actionType);
        activitiseInfo.setActionName(actionName);
        activitiseInfo.setActionUserId(MyApplication.userInfo.getObjectId());
        activitiseInfo.setActionPic(list);
        activitiseInfo.setActionRMB(actionMoney);
        activitiseInfo.setActionPlace(actionPlace);
        activitiseInfo.setActionDesc(actionDesc);
        activitiseInfo.setActionTime(actionTime);
        activitiseInfo.setActionPic(list);
        activitiseInfo.setActionPoiInfo(mPoiInfo);

        activitiseInfo.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    toast("发布活动成功");

                    finish();
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

    }



}
