package com.example.lab4.ui.dialogfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.lab4.R
import com.example.lab4.databinding.FragmentConfirmDeleteDialogBinding

class ConfirmDeleteDialogFragment : DialogFragment() {

    interface ConfirmDeleteListener {
        fun onConfirmDelete(position: Int)
        fun onCancelDelete(position: Int)
    }

    private var _binding: FragmentConfirmDeleteDialogBinding? = null
    private val binding get() = _binding!!

    private var listener: ConfirmDeleteListener? = null
    private var position: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmDeleteDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btConfirm.setOnClickListener {
            position?.let { pos ->
                listener?.onConfirmDelete(pos)
            }
            dismiss()
        }

        binding.btCancel.setOnClickListener {
            position?.let { pos ->
                listener?.onCancelDelete(pos)
            }
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ConfirmDeleteListener) {
            listener = context
        } else if (parentFragment is ConfirmDeleteListener) {
            listener = parentFragment as ConfirmDeleteListener
        } else {
            throw RuntimeException("$context must implement ConfirmDeleteListener")
        }
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): ConfirmDeleteDialogFragment {
            val fragment = ConfirmDeleteDialogFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(ARG_POSITION)
    }
}