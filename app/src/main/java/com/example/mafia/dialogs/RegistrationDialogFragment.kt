package com.example.mafia.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.mafia.Preferences
import com.example.mafia.R
import com.example.mafia.activities.common_activities.preferences

class RegistrationDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext(), R.style.RegistrationDialog)
        val inflater = requireActivity().layoutInflater.inflate(R.layout.dialog_registration, null)
        val editText = inflater.findViewById<EditText>(R.id.editTextUsername)
        return builder
            .setView(inflater)
            .setPositiveButton(R.string.toRegister) { _, _ ->
                val username = editText.text.toString()
                val editor = preferences.edit()
                editor.putString(Preferences.USERNAME_TAG, username)
                editor.apply()
            }
            .create()
    }
}