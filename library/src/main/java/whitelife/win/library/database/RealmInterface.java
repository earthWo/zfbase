package whitelife.win.library.database;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import whitelife.win.library.database.annotations.RealmType;
import whitelife.win.library.database.annotations.RealmTypes;

/**
 * Created by wuzefeng on 2017/9/27.
 */

public interface RealmInterface {

    /**
     * 添加数据
     * @param t
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.ADD)
    <T extends RealmObject> T copyToRealm(T... t);

    /**
     * 添加对象列表
     * @param list
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.ADD)
    <T extends RealmObject> List<T> copyToRealm(List<T> list);



    /**
     * 添加或更新
     * @param list
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.UPDATE)
    <T extends RealmObject> List<T> copyToRealmOrUpdate(List<T> list);



    /**
     * 添加或更新
     * @param t
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.UPDATE)
    <T extends RealmObject> T copyToRealmOrUpdate(T... t);


    /**
     * 删除数据
     * @param t
     * @param <T>
     */
    @RealmTypes(RealmType.DELETE)
    <T extends RealmObject>T deleteFromRealm(T... t);


    /**
     * 从结果中删除，如果不传int则全部删除
     * @param t
     * @param i
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.DELETE)
    <T extends RealmObject>T deleteFromRealm(RealmResults<T> t,int... i);


    /**
     * 删除list数据
     * @param list
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.DELETE)
    <T extends RealmObject>List<T> deleteFromRealm(List<T> list);


    /**
     * 查找全部
     * @param cla
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.SEARCH)
    <T extends RealmObject>RealmResults<T> findAll(Class<T>cla);


    /**
     * 查找相等的
     * @param cla
     * @param args
     * @param values
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.SEARCH)
    public <T extends RealmObject>RealmResults<T> findEquals(Class<T>cla,String[] args,Object[] values);


    /**
     * 修改
     * @param cla
     * @param t
     * @param args
     * @param classes
     * @param values
     * @param <T>
     * @return
     */
    @RealmTypes(RealmType.MODIFY)
    <T extends RealmObject>T modify(Class<T> cla,T t,String[] args,Class[] classes,Object[] values);

}
