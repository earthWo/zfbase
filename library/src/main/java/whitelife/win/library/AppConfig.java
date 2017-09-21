package whitelife.win.library;

/**
 * Created by wuzefeng on 2017/9/20.
 */

public class AppConfig {

    private static volatile AppConfig sInstance;

    private Builder mBuilder;


    public static void initialize(AppConfig appConfig){
        if(appConfig!=null){
            sInstance=appConfig;
        }
    }


    /**
     * 获取AppConfig单例
     * @return
     */
    public static AppConfig getsInstance(){
        if(sInstance==null){
            synchronized (AppConfig.class){
                if(sInstance==null){
                    sInstance=new AppConfig(new AppConfig.Builder());
                }
            }
        }
        return sInstance;
    }





    private AppConfig(Builder builder){
        if(builder==null){
            mBuilder=new Builder();
        }else{
            mBuilder=builder;
        }
    }


    public boolean isDebugMode() {
        return mBuilder.isDebugMode();
    }


    public boolean isLogEnable() {
        return mBuilder.isLogEnable();
    }

    public String getAppName() {
        return mBuilder.getAppName();
    }

    public String getAppType() {
        return mBuilder.getAppType();
    }

    public String getAppVersion() {
        return mBuilder.getAppVersion();
    }









    public static class Builder{

        /**
         * 是否是debug模式
         */
        private boolean mIsDebugMode;

        /**
         * 是否开始log打印
         */
        private boolean mIsLogEnable;

        /**
         * 项目名字
         */
        private String mAppName;

        /**
         * app类型
         */
        private String mAppType;

        /**
         * app版本
         */
        private String mAppVersion;


        public Builder(){
            mIsDebugMode=true;
            mIsLogEnable=false;
        }


        public boolean isDebugMode() {
            return mIsDebugMode;
        }

        public Builder setDebugMode(boolean mIsDebugMode) {
            this.mIsDebugMode = mIsDebugMode;
            return this;
        }

        public boolean isLogEnable() {
            return mIsLogEnable;
        }

        public Builder setLogEnable(boolean mIsLogEnable) {
            this.mIsLogEnable = mIsLogEnable;
            return this;
        }

        public String getAppName() {
            return mAppName;
        }

        public Builder setAppName(String mAppName) {
            this.mAppName = mAppName;
            return this;
        }

        public String getAppType() {
            return mAppType;
        }

        public Builder setAppType(String mAppType) {
            this.mAppType = mAppType;
            return this;
        }

        public String getAppVersion() {
            return mAppVersion;
        }

        public Builder setAppVersion(String mAppVersion) {
            this.mAppVersion = mAppVersion;
            return this;
        }



        public AppConfig build(){
            return new AppConfig(this);
        }
    }




}
