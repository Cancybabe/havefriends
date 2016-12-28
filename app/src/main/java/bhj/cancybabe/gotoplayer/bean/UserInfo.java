package bhj.cancybabe.gotoplayer.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Cancybabe on 2016/11/8.
 */
public class UserInfo extends BmobUser {
    private int sex;
    private String nickName;
    private BmobFile userHead;
    private List<String> praiseActions;

    public List<String> getPraiseActions() {
        return praiseActions;
    }

    public void setPraiseActions(List<String> praiseActions) {
        this.praiseActions = praiseActions;
    }

    public UserInfo() {
    }


    public BmobFile getUserHead() {
        return userHead;
    }

    public void setUserHead(BmobFile userHead) {
        this.userHead = userHead;
    }


    public String getNickName() {
        return nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
