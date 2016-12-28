package bhj.cancybabe.gotoplayer.bean;

import com.baidu.mapapi.search.core.PoiInfo;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by Cancybabe on 2016/11/10.
 */
public class UserActivtiesInfo extends BmobObject {
    private String actionName;//活动名称
    private String actionClass;//活动类型
    private String actionTime;//活动时间
    private String actionPlace;//活动地点
    private String actionPlaceDetails;//活动详细地址
    private String actionUserId;//活动用户ID
    private String actionCity;//活动城市
    private String actionSite;//活动地址
    private String actionRMB;//活动金额
    private String actionDesc;//活动描述
    private List<String> praiseUser;//活动点赞者
    private List<BmobFile> actionPic;//活动图片
    private List<String> paymentUserId;//活动已支付者
    private List<String> startUserId;//活动参加者
    private Boolean flag;//活动开始标记
    private PoiInfo actionPoiInfo;
    private int priseCount;

    public int getPriseCount() {
        return priseCount;
    }

    public void setPriseCount(int priseCount) {
        this.priseCount = priseCount;
    }

    public PoiInfo getActionPoiInfo() {
        return actionPoiInfo;
    }

    public void setActionPoiInfo(PoiInfo actionPoiInfo) {
        this.actionPoiInfo = actionPoiInfo;
    }

    public void setActionpublishUser(String actionpublishUser) {
        this.actionpublishUser = actionpublishUser;
    }

    public String getActionpublishUser() {
        return actionpublishUser;
    }

    private String actionpublishUser;//活动发布者

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setActionClass(String actionClass) {
        this.actionClass = actionClass;
    }


    public String getActionPlaceDetails() {
        return actionPlaceDetails;
    }

    public void setActionPlaceDetails(String actionPlaceDetails) {
        this.actionPlaceDetails = actionPlaceDetails;
    }


    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

    public void setActionPlace(String actionPlace) {
        this.actionPlace = actionPlace;
    }

    public void setActionUserId(String actionUserId) {
        this.actionUserId = actionUserId;
    }

    public void setActionCity(String actionCity) {
        this.actionCity = actionCity;
    }

    public void setActionSite(String actionSite) {
        this.actionSite = actionSite;
    }

    public void setActionRMB(String actionRMB) {
        this.actionRMB = actionRMB;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public void setPraiseUser(List<String> praiseUser) {
        this.praiseUser = praiseUser;
    }

    public void setActionPic(List<BmobFile> actionPic) {
        this.actionPic = actionPic;
    }

    public void setPaymentUserId(List<String> paymentUserId) {
        this.paymentUserId = paymentUserId;
    }

    public void setStartUserId(List<String> startUserId) {
        this.startUserId = startUserId;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getActionClass() {
        return actionClass;
    }

    public String getActionTime() {
        return actionTime;
    }

    public String getActionPlace() {
        return actionPlace;
    }

    public String getActionUserId() {
        return actionUserId;
    }

    public String getActionCity() {
        return actionCity;
    }

    public String getActionSite() {
        return actionSite;
    }

    public String getActionRMB() {
        return actionRMB;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public List<String> getPraiseUser() {
        return praiseUser;
    }

    public List<BmobFile> getActionPic() {
        return actionPic;
    }

    public List<String> getPaymentUserId() {
        return paymentUserId;
    }

    public List<String> getStartUserId() {
        return startUserId;
    }

    public Boolean getFlag() {
        return flag;
    }
}
