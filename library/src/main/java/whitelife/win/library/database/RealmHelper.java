package whitelife.win.library.database;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import whitelife.win.library.database.annotations.RealmType;
import whitelife.win.library.database.annotations.RealmTypes;

/**
 * Created by wuzefeng on 2017/9/26.
 */

public class RealmHelper {

    private Realm realm=Realm.getDefaultInstance();

    public <T> T create(Class<T> t){
        return (T) Proxy.newProxyInstance(t.getClassLoader(), new Class[]{t},new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if(method.getDeclaringClass()==Object.class){
                    return method.invoke(this,args);
                }

                realm.beginTransaction();
                Object o=parseMethod(method,args);
                realm.commitTransaction();
                return o;
            }
        });
    }


    private Object parseMethod(Method method,Object[] args){

        RealmType type=method.getAnnotation(RealmTypes.class).value();

        if(type==RealmType.ADD){
            return parseAddMethod(args);
        }else if(type==RealmType.DELETE){
            return parseDeleteMethod(args);
        }else if(type==RealmType.SEARCH){
            return parseSearchMethod(args);
        }else if(type==RealmType.UPDATE){
            return parseUpdateMethod(args);
        }else if(type==RealmType.MODIFY){
            return parseModifyMethod(args);
        }
        return new Object();

    }


    private Object parseModifyMethod(Object[]args){
        Class c= (Class) args[0];
        RealmObject realmObject= (RealmObject) args[1];
        String[]names= (String[]) args[2];
        Class[]classes= (Class[]) args[3];
        Object[]values= (Object[]) args[4];
        for(int i=0;i<names.length;i++){
            try {
                Method method= c.getDeclaredMethod(names[i],classes[i]);
                method.invoke(realmObject,values[i]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
       return realmObject;
    }


    private Object parseUpdateMethod(Object[]args){
        Object returnObject = null;
        if(args!=null&&args.length>0){
            Object o=args[0];
            if(o instanceof List){
                return realm.copyToRealmOrUpdate((Iterable<RealmModel>) o);
            }else if(o.getClass().isArray()){
                for(Object ob:(RealmModel[])o){
                    returnObject=realm.copyToRealmOrUpdate((RealmModel) ob);
                }
            }
        }
        return returnObject;
    }

    private Object parseSearchMethod(Object[]args){
        Object returnObject = null;
        if(args!=null&&args.length>0){
            Object o=args[0];
            if(o instanceof Class&&args.length==1){
                returnObject=realm.where((Class<RealmModel>) o).findAll();
            }else if(o instanceof Class){
                String []items= (String[]) args[1];
                Object []values= (Object[]) args[2];
                RealmQuery realmQuery=  realm.where((Class<RealmModel>)o);

                for(int i=0;i<items.length;i++){
                    if(values[i] instanceof String){
                        realmQuery=realmQuery.equalTo(items[i], (String) values[i]);
                    }else if(values[i] instanceof Integer){
                        realmQuery=realmQuery.equalTo(items[i], (Integer) values[i]);
                    }else if(values[i] instanceof Long){
                        realmQuery=realmQuery.equalTo(items[i], (Long) values[i]);
                    }else if(values[i] instanceof Float){
                        realmQuery=realmQuery.equalTo(items[i], (Float) values[i]);
                    }else if(values[i] instanceof Double){
                        realmQuery=realmQuery.equalTo(items[i], (Double) values[i]);
                    }else if(values[i] instanceof Short){
                        realmQuery=realmQuery.equalTo(items[i], (Short) values[i]);
                    }else if(values[i] instanceof Byte){
                        realmQuery=realmQuery.equalTo(items[i], (Byte) values[i]);
                    }else if(values[i] instanceof byte[]){
                        realmQuery=realmQuery.equalTo(items[i], (byte[]) values[i]);
                    }else if(values[i] instanceof Boolean){
                        realmQuery=realmQuery.equalTo(items[i], (Boolean) values[i]);
                    }else if(values[i] instanceof Date){
                        realmQuery=realmQuery.equalTo(items[i], (Date) values[i]);
                    }
                }
                returnObject=realmQuery.findAll();
            }
        }
        return returnObject;
    }

    private Object parseAddMethod(Object[] args){
        Object returnObject = null;
        if(args!=null&&args.length>0){
           Object o=args[0];
            if(o instanceof List){
                return realm.copyToRealm((Iterable<RealmModel>) o);
            }else if(o.getClass().isArray()){
                for(Object ob:(RealmModel[])o){
                    returnObject=realm.copyToRealm((RealmModel) ob);
                }
            }
        }
        return returnObject;
    }

    private Object parseDeleteMethod(Object[]args){
        Object returnObject = null;
        if(args!=null&&args.length>0){
            Object o=args[0];
            //删除整个数组
            if(o instanceof RealmResults){
                //如果写了数字，则删除该位置的数据
                if(args.length>1){
                    int[]integers= (int[]) args[1];
                    for(int ob:integers){
                        // 写的是数字
                        ((RealmResults)o).deleteFromRealm(ob);
                        returnObject=((RealmResults)o).get(ob);
                    }
                }else{//不过不写则全部删除
                    ((RealmResults) o).deleteAllFromRealm();
                }
            }else if(o.getClass().isArray()){
                for(Object ob:(RealmObject[])o){
                    ((RealmObject)ob).deleteFromRealm();
                    returnObject=ob;
                }
            }else if(o instanceof List){
                for(Object ob:(List)o){
                    ((RealmObject)ob).deleteFromRealm();
                }
                return o;
            }
        }
        return returnObject;
    }

}
