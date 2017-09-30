package whitelife.win.viewlibrary.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

/**
 * 将EditText和一个Clean View绑定的FrameLayout
 * Created by wuzefeng on 2017/9/27.
 */

public class CleanEditTextLayout extends FrameLayout implements View.OnClickListener,TextWatcher,View.OnFocusChangeListener{


    private EditText mEditText;


    private View cleanView;


    private static final String EDITTEXT_TAG="edit_tag";

    private static final String CLEANVIEW_TAG="clean_tag";


    public CleanEditTextLayout( Context context) {
        super(context);
    }

    public CleanEditTextLayout( Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public CleanEditTextLayout( Context context,  AttributeSet attrs,  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if(EDITTEXT_TAG.equals(child.getTag())&&child instanceof EditText){
            mEditText= (EditText) child;
        }else if(CLEANVIEW_TAG.equals(child.getTag())){
            cleanView=child;
            cleanView.setVisibility(GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(cleanView!=null)cleanView.setOnClickListener(this);
        if(mEditText!=null){
            mEditText.addTextChangedListener(this);
            mEditText.setOnFocusChangeListener(this);
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(cleanView!=null)cleanView.setOnClickListener(null);
        if(mEditText!=null){
            mEditText.removeTextChangedListener(this);
            mEditText.setOnFocusChangeListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        if(mEditText!=null){
            mEditText.setText(null);
            cleanView.setVisibility(GONE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.toString().length()>0&&cleanView!=null&&cleanView.getVisibility()==View.GONE){
            cleanView.setVisibility(VISIBLE);
        }else if(s.toString().length()==0&&cleanView!=null&&cleanView.getVisibility()==View.VISIBLE){
            cleanView.setVisibility(GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus&&mEditText.getText().toString().length()>0&&cleanView!=null&&cleanView.getVisibility()==View.GONE){
            cleanView.setVisibility(VISIBLE);
        }else if((!hasFocus||mEditText.getText().toString().length()==0)&&cleanView!=null&&cleanView.getVisibility()==View.GONE){
            cleanView.setVisibility(GONE);
        }
    }

}
