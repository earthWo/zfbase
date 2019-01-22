package whitelife.win.library.helper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import whitelife.win.library.base.BaseAutoDataUpdate;
import whitelife.win.library.bean.Response;

/**
 * Created by wuzefeng on 2017/10/10.
 */

public class AutoUpdateDataHelper {


    private Map<Integer,LinkedList<BaseAutoDataUpdate>> dataEntryMap;


    private AutoUpdateDataHelper(){
        dataEntryMap=new HashMap<>();
    }


    public static volatile AutoUpdateDataHelper instance;


    public static AutoUpdateDataHelper getInstance(){
        if(instance==null){
            synchronized (AutoUpdateDataHelper.class){
                if(instance==null){
                    instance=new AutoUpdateDataHelper();
                }
            }
        }
        return instance;
    }



    public<B extends BaseAutoDataUpdate> void bind(B b){
        int[]codes=b.getCodes();

        if(codes!=null&&codes.length>0){
            for(int i=0;i<codes.length;i++){
                int code=codes[i];
                if(dataEntryMap.containsKey(code)){
                    LinkedList linkedList=dataEntryMap.get(code);
                    linkedList.add(b);
                }else{
                    LinkedList linkedList=new LinkedList();
                    linkedList.add(b);
                    dataEntryMap.put(code,linkedList);
                }
            }
        }
    }


    public<B extends BaseAutoDataUpdate> void unBind(B b){
        int[]codes=b.getCodes();
        if(codes!=null&&codes.length>0){
            for(int i=0;i<codes.length;i++){
                int code=codes[i];
                if(dataEntryMap.containsKey(code)){
                    LinkedList linkedList=dataEntryMap.get(code);
                    linkedList.remove(b);
                }
            }
        }
    }


    public void updatePresentData(int code, Response response){
        if(dataEntryMap.containsKey(code)){
            LinkedList<BaseAutoDataUpdate> linkedList=dataEntryMap.get(code);
            while(linkedList.iterator().hasNext()){
                BaseAutoDataUpdate baseConnectBinder=linkedList.iterator().next();
                baseConnectBinder.autoUpdateData(code,response);
            }
        }
    }

}
