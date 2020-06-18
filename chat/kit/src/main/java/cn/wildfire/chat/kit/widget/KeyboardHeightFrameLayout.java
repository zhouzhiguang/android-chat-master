package cn.wildfire.chat.kit.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;

public class KeyboardHeightFrameLayout extends FrameLayout implements InputAwareLayout.InputView {
    public KeyboardHeightFrameLayout(@NonNull Context context) {
        super(context);
        LogUtils.e("初始化了吗");
    }

    public KeyboardHeightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LogUtils.e("初始化了吗");
    }

    public KeyboardHeightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtils.e("初始化了吗");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KeyboardHeightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LogUtils.e("初始化了吗");
    }


    @Override
    public void show(int height, boolean immediate) {
        // TODO
        Toast.makeText(getContext(), "显示了", Toast.LENGTH_LONG).show();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        LogUtils.e("弹出底部view高度" + height);
        getChildAt(0).setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }

    @Override
    public void hide(boolean immediate) {
        LogUtils.e("弹出底部view高度" + "hidehidehide");
        Toast.makeText(getContext(), "影藏了", Toast.LENGTH_LONG).show();
        setVisibility(GONE);
    }

    @Override
    public boolean isShowing() {

        return getVisibility() == VISIBLE;
    }
}
