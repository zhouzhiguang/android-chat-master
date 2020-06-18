package cn.wildfire.chat.kit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.apkfuns.logutils.LogUtils;

import cn.wildfire.chat.kit.utils.ServiceUtil;

//布局里面
public class InputAwareLayout extends KeyboardAwareLinearLayout implements KeyboardAwareLinearLayout.OnKeyboardShownListener {
    private InputView current;

    public InputAwareLayout(Context context) {
        this(context, null);
    }

    public InputAwareLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputAwareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnKeyboardShownListener(this);
    }

    @Override
    public void onKeyboardShown() {

        hideAttachedInput(true);
    }

    public void show(@NonNull final EditText imeTarget, @NonNull final InputView input) {
        LogUtils.e("看一下显示了吗？");
        if (isKeyboardOpen()) {
            hideSoftkey(imeTarget, new Runnable() {
                @Override
                public void run() {
                    hideAttachedInput(true);
                    input.show(getKeyboardHeight(), true);
                    current = input;
                }
            });
        } else {
            if (current != null) current.hide(true);
            input.show(getKeyboardHeight(), current != null);
            current = input;
        }
    }

    public InputView getCurrentInput() {
        return current;
    }

    public void hideCurrentInput(EditText imeTarget) {
        if (isKeyboardOpen()) hideSoftkey(imeTarget, null);
        else hideAttachedInput(false);
    }

    public void hideAttachedInput(boolean instant) {
        if (current != null) current.hide(instant);
        current = null;
    }

    public boolean isInputOpen() {
        return (isKeyboardOpen() || (current != null && current.isShowing()));
    }

    public void showSoftkey(final EditText inputTarget) {
        LogUtils.e("是调用你弹出输入法的吗--------》");
        postOnKeyboardOpen(new Runnable() {
            @Override
            public void run() {
                hideAttachedInput(true);
            }
        });
        inputTarget.post(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("是调用你弹出输入法的吗--------》");
                inputTarget.requestFocus();
                ServiceUtil.getInputMethodManager(inputTarget.getContext()).showSoftInput(inputTarget, 0);
            }
        });
    }

    public void hideSoftkey(final EditText inputTarget, @Nullable Runnable runAfterClose) {
        if (runAfterClose != null) postOnKeyboardClose(runAfterClose);

        ServiceUtil.getInputMethodManager(inputTarget.getContext())
                .hideSoftInputFromWindow(inputTarget.getWindowToken(), 0);
    }

    public interface InputView {
        void show(int height, boolean immediate);

        void hide(boolean immediate);

        boolean isShowing();
    }
}

