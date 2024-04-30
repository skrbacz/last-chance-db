package com.example.lastchancedb.admin_popups.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.R
import com.example.lastchancedb.database.user.UserSuspendedFunctions
import com.example.lastchancedb.database.user.User
import com.example.lastchancedb.databinding.FragmentDeleteUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteUserPopUp : DialogFragment() {

    private var _binding: FragmentDeleteUserBinding? = null
    private val binding get() = _binding!!

    private var deleteBTN: Button? = null

    private val couritineScoupe = CoroutineScope(Dispatchers.Main)
    private var users: Set<User?>? = null
    override fun onResume() {
        super.onResume()

        couritineScoupe.launch {
            users = UserSuspendedFunctions.getAllUsers()

            binding.let {


            }
        }

        val usersEmails = getEmailsArray(users)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, usersEmails)
        binding.userDeleteACTV.setAdapter(arrayAdapter)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteUserBinding.inflate(inflater, container, false)

        deleteBTN = binding.deleteUserBTN

        deleteBTN?.setOnClickListener {

            if (binding.userDeleteACTV.text.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please select users email first",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                couritineScoupe.launch {
                    UserSuspendedFunctions.deleteUser(
                        binding.userDeleteACTV.text.toString(),
                        requireContext()
                    )
                }
            }
        }

        return binding.root
    }

    private fun getEmailsArray(users: Set<User?>?): Array<String> {
        val emails = mutableListOf<String>()
        users?.forEach { user ->
            emails.add(user?.email.toString())
        }
        return emails.toTypedArray()
    }

}