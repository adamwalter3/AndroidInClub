package com.clubcom.inclub.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.inclub.R;

import java.io.File;

/**
 * Created by adamwalter on 5/29/14.
 */
public class ThemedAlertDialogBuilder extends AlertDialog.Builder {

    public static double DEFAULT_WIDTH_RATIO = 0.35;
    public static double DEFAULT_HEIGHT_RATIO = 0.35;

    private final Context mContext;
    protected final TextView vMessage;
    private final TextView vTitle;
    private final View vTitleContainer;
    private final ImageView vImage;
    private final ListView vListView;
    private final EditText vInput;
    private AlertDialog mDialogInterface;
    private final RelativeLayout vView;
    private final View vDialogView;
    private Button vPositiveButton;
    private Button vNegativeButton;

    public ThemedAlertDialogBuilder(Context context) {
        this(context, null);
    }

    @SuppressLint("InflateParams")
    public ThemedAlertDialogBuilder(Context context, File imageFile) {
        super(context);
        mContext = context;
        vDialogView = LayoutInflater.from(context).inflate(R.layout.themed_alert_dialog, null);
        vMessage = (TextView) vDialogView.findViewById(R.id.message);
        vTitle = (TextView) vDialogView.findViewById(R.id.title);
        vTitleContainer = vDialogView.findViewById(R.id.title_container);
        vListView = (ListView) vDialogView.findViewById(R.id.list_view);
        vInput = (EditText) vDialogView.findViewById(R.id.input);
        vImage = (ImageView) vDialogView.findViewById(R.id.image);
        vView = (RelativeLayout) vDialogView.findViewById(R.id.view);
        setTopBarIcon(imageFile);
        mDialogInterface = this.create();
        setView(vDialogView);
    }

    public ThemedAlertDialogBuilder setCustomView(View view) {
        vView.addView(view);
        vListView.setVisibility(View.GONE);
        vMessage.setVisibility(View.GONE);
        vInput.setVisibility(View.GONE);
        vView.setVisibility(View.VISIBLE);
        return this;
    }

    @Override
    public ThemedAlertDialogBuilder setMessage(CharSequence message) {
        vMessage.setText(message);
        return this;
    }

    @Override
    public ThemedAlertDialogBuilder setTitle(int titleId) {
        return setTitle(mContext.getString(titleId));
    }

    @Override
    public ThemedAlertDialogBuilder setTitle(CharSequence title) {
        vTitle.setVisibility(View.VISIBLE);
        vTitle.setText(title);
        vTitleContainer.setVisibility(View.VISIBLE);

        return this;
    }

    @Override
    public ThemedAlertDialogBuilder setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        return this;
    }

    public ThemedAlertDialogBuilder setTopBarIcon(File iconFile) {
        //ImageLoader.loadImage(mContext, R.drawable.gym_logo_white, vImage);
        return this;
    }

    @Override
    public ThemedAlertDialogBuilder setSingleChoiceItems(ListAdapter adapter, int checkedItem, final DialogInterface.OnClickListener listener) {
        vListView.setAdapter(adapter);
        if (listener != null) {
            vListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    listener.onClick(mDialogInterface, position);
                }
            });
        }

        return this;
    }

    @Override
    public AlertDialog create() {
        mDialogInterface = super.create();
        return mDialogInterface;
    }

    public ThemedAlertDialogBuilder useInput(String hint, int type) {
        vListView.setVisibility(View.GONE);
        vMessage.setVisibility(View.GONE);
        vInput.setVisibility(View.VISIBLE);
        vInput.setHint(hint);
        vInput.setInputType(type);
        return this;
    }

    public void setMessageVisibility(int visibility) {
        vMessage.setVisibility(visibility);
    }

    public void setListViewVisiblity(int visibility) {
        vListView.setVisibility(visibility);
    }

    public EditText getInput() {
        return vInput;
    }

    @Override
    public ThemedAlertDialogBuilder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
        super.setNegativeButton(text, listener);
        return this;
    }

    @Override
    public ThemedAlertDialogBuilder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
        super.setPositiveButton(text, listener);
        return this;
    }

    public EditText getEditText() {
        return vInput;
    }

    public AlertDialog show(int width, int height) {
        AlertDialog alertDialog = super.show();
        alertDialog.getWindow().setLayout(width, height);
        return alertDialog;
    }

    public AlertDialog show() {
        int width = (int) Math.round(CCDisplay.getScreenWidth(mContext) * DEFAULT_WIDTH_RATIO);
        int height = (int) Math.round(CCDisplay.getScreenHeight(mContext) * DEFAULT_HEIGHT_RATIO);
        return show(width, height);
    }

    public AlertDialog showProgressDialog() {
        AlertDialog alertDialog = super.show();
        alertDialog.getWindow().setLayout(mContext.getResources().getDimensionPixelSize(R.dimen.cc_progress_dialog_width), mContext.getResources().getDimensionPixelSize(R.dimen.cc_progress_dialog_height));
        return alertDialog;
    }

    public Button getPositiveButton() {
        return vPositiveButton;
    }

    public void setPositiveButton(Button vPositiveButton) {
        this.vPositiveButton = vPositiveButton;
    }

    public Button getNegativeButton() {
        return vNegativeButton;
    }

    public void setNegativeButton(Button vNegativeButton) {
        this.vNegativeButton = vNegativeButton;
    }
}
