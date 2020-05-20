package com.eugene_poroshin.weatherforecast.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.eugene_poroshin.weatherforecast.R

class AddCityDialogFragment : DialogFragment() {

    private var editText: EditText? = null

    interface EditNameDialogListener {
        fun onFinishEditDialog(inputText: String?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_add_city, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        builder.setTitle("Adding a city")
        builder.setView(inflater.inflate(R.layout.dialog_fragment_add_city, null))
            .setPositiveButton("Add") { _, _ -> sendBackResult() }
            .setNegativeButton("Cancel") { _, _ -> dismiss() }
        return builder.create()
    }

    private fun sendBackResult() {
        editText = dialog!!.findViewById(R.id.edit_text_city_name)
        editText?.requestFocus()
        dialog!!.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        val listener = targetFragment as EditNameDialogListener?
        listener!!.onFinishEditDialog(editText?.text.toString())
        dismiss()
    }
}