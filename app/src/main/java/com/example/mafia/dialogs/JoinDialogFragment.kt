package com.example.mafia.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.mafia.R

class JoinDialogFragment : DialogFragment() {
    private lateinit var listener: JoinDialogListener

    interface JoinDialogListener {
        fun sendCode(code: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as JoinDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement NoticeDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.RegistrationDialog)
        val inflater = requireActivity().layoutInflater.inflate(R.layout.dialog_registration, null)
        val editText = inflater.findViewById<EditText>(R.id.editTextUsername)
        return builder
            .setView(inflater)
            .setPositiveButton(R.string.toRegister) { _, _ ->
                val code = editText.text.toString()
                listener.sendCode(code)
            }
            .setNegativeButton("Закрыть") { _, _ -> }
            .create()
    }
}