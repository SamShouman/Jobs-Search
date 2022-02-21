package com.example.jobproject2.Tools;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;

import com.example.jobproject2.R;

public class Utils {
    public static ProgressDialog createAndShowProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(R.string.authenticating));
        progressDialog.setTitle(context.getString(R.string.grab_a_cup_of_coffee));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        return progressDialog;
    }

    public static  void animateRecyclerViewItemFromLeftToRight(View itemView) {
        /* show the items of recycler view by animating them from left to right
         *  @param itemView: view to be animated */
        ObjectAnimator animationLeft = ObjectAnimator.ofFloat(itemView, "translationX", -800f, 80f, 0f, 0, 0f);
        animationLeft.setDuration(1500);
        animationLeft.start();
    }
}
