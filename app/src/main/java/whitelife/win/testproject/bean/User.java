package whitelife.win.testproject.bean;

import io.realm.RealmObject;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class User extends RealmObject {

    private int uid;

    private String name;

    private int age;

    private String sex;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
