package whitelife.win.recyclerviewlibrary.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 自定义ViewHolder，实现view的绑定，view数据的绑定
 * Created by wuzefeng on 2017/9/28.
 */

public class MultiRecyclerViewHolder extends RecyclerView.ViewHolder {

    public MultiRecyclerViewHolder(View itemView) {
        super(itemView);
        this.rootView=itemView;
    }

    protected static View inflateView(Context context, @LayoutRes int res, ViewGroup viewParent){
        return LayoutInflater.from(context).inflate(res,viewParent,false);
    }


    private View rootView;


    public View getRootView() {
        return rootView;
    }

    //-------------------------------------绑定数据相关---------------------------------------------//


    /**
     * 绑定text
     * @param vid
     * @param data
     * @return
     */
    public MultiRecyclerViewHolder bindTextViewRes(@IdRes int vid,String data){
        TextView textView= (TextView) rootView.findViewById(vid);
        if(textView!=null){
            textView.setText(data);
        }
        return this;
    }

    /**
     * 绑定text
     * @param vid
     * @param sid
     * @return
     */
    public MultiRecyclerViewHolder bindTextViewRes(@IdRes int vid,@StringRes int sid){
        TextView textView= (TextView) rootView.findViewById(vid);
        if(textView!=null){
            textView.setText(sid);
        }
        return this;
    }

    /**
     * 绑定text文字颜色
     * @param vid
     * @param cid
     * @return
     */
    public MultiRecyclerViewHolder bindTextViewColor(@IdRes int vid,int cid){
        TextView textView= (TextView) rootView.findViewById(vid);
        if(textView!=null){
            textView.setTextColor(cid);
        }
        return this;
    }


    /**
     * 绑定text文字颜色
     * @param vid
     * @param color
     * @return
     */
    public MultiRecyclerViewHolder bindTextViewColor(@IdRes int vid,ColorStateList color){
        TextView textView= (TextView) rootView.findViewById(vid);
        if(textView!=null){
            textView.setTextColor(color);
        }
        return this;
    }


    /**
     * 绑定view是否显示
     * @param vid
     * @param visiable
     * @return
     */
    public MultiRecyclerViewHolder bindViewVisiable(@IdRes int vid,int visiable){
        View view= rootView.findViewById(vid);
        if(view!=null){
            view.setVisibility(visiable);
        }
        return this;
    }


    /**
     * 绑定view的背景
     * @param vid
     * @param res
     * @return
     */
    public MultiRecyclerViewHolder bindViewBackground(@IdRes int vid,int res){
        View view= rootView.findViewById(vid);
        if(view!=null){
            view.setBackgroundResource(res);
        }
        return this;
    }

    /**
     * 绑定view的背景
     * @param vid
     * @param drawable
     * @return
     */
    public MultiRecyclerViewHolder bindViewBackground(@IdRes int vid,Drawable drawable){
        View view= rootView.findViewById(vid);
        if(view!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            }else{
                view.setBackgroundDrawable(drawable);
            }
        }
        return this;
    }



    /**
     * 绑定imageview的src
     * @param vid
     * @param res
     * @return
     */
    public MultiRecyclerViewHolder bindImageViewSrc(@IdRes int vid,int res){
        ImageView imageView= (ImageView) rootView.findViewById(vid);
        if(imageView!=null){
            imageView.setImageResource(res);
        }
        return this;
    }


    /**
     * 绑定imageview的src
     * @param vid
     * @param bitmap
     * @return
     */
    public MultiRecyclerViewHolder bindImageViewSrc(@IdRes int vid,Bitmap bitmap){
        ImageView imageView= (ImageView) rootView.findViewById(vid);
        if(imageView!=null){
            imageView.setImageBitmap(bitmap);
        }
        return this;
    }


    /**
     * 绑定imageview的src
     * @param vid
     * @param drawable
     * @return
     */
    public MultiRecyclerViewHolder bindImageViewSrc(@IdRes int vid,Drawable drawable){
        ImageView imageView= (ImageView) rootView.findViewById(vid);
        if(imageView!=null){
            imageView.setImageDrawable(drawable);
        }
        return this;
    }


    /**
     * 绑定view的tag
     * @param vid
     * @param tag
     * @return
     */
    public MultiRecyclerViewHolder bindViewTag(@IdRes int vid,Object tag){
        View view= rootView.findViewById(vid);
        if(view!=null){
            view.setTag(tag);
        }
        return this;
    }

    /**
     * 绑定view的透明度
     * @param vid
     * @param alpha
     * @return
     */
    public MultiRecyclerViewHolder bindViewAlpha(@IdRes int vid,float alpha){
        View view= rootView.findViewById(vid);
        if(view!=null){
            view.setAlpha(alpha);
        }
        return this;
    }


}
