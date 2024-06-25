package com.example.lab4.ui.dialogfragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.fragment.app.DialogFragment
import com.example.lab4.R
import com.example.lab4.databinding.FragmentDialogLoadingBinding
import kotlin.math.floor

class LoadingDialogFragment : DialogFragment() {
    companion object {
        const val LOADING_DIALOG_FRAGMENT_TAG: String = "LOADING_DIALOG_FRAGMENT_TAG"

        const val LOADING_DIALOG_DURATION: Long = 1000
        const val LOADING_DIALOG_FRAME_COUNT: Int = 12
    }

    private lateinit var binding: FragmentDialogLoadingBinding

    @SuppressLint("NewApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        binding =
            FragmentDialogLoadingBinding.inflate(context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
        dialog.setContentView(binding.root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, _ -> return@OnKeyListener (keyCode == KeyEvent.KEYCODE_BACK) })
        dialog.window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.argb(0.4F, 0F, 0F, 0F)))

        configAnimation()

        return dialog
    }

    private fun configAnimation() {
        val a: Animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation_rotate)
        a.duration = LOADING_DIALOG_DURATION
        a.interpolator =
            Interpolator { input -> floor((input * LOADING_DIALOG_FRAME_COUNT).toDouble()).toFloat() / LOADING_DIALOG_FRAME_COUNT }
        binding.ivLoading.startAnimation(a)
    }

    override fun getTheme(): Int {
        return android.R.style.Theme_DeviceDefault_NoActionBar
    }

}