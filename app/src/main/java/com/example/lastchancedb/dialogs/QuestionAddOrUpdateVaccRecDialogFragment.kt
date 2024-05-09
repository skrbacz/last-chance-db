package com.example.lastchancedb.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.lastchancedb.databinding.FragmentQuestionAddOrUpdateVaccRecordBinding

class QuestionAddOrUpdateVaccRecDialogFragment : DialogFragment() {

    private var _binding: FragmentQuestionAddOrUpdateVaccRecordBinding? = null
    private val binding get() = _binding!!

    private var submitBtn: Button? = null

    private var updateBtn: RadioButton? = null
    private var addBtn: RadioButton? = null

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = params
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionAddOrUpdateVaccRecordBinding.inflate(inflater, container, false)

        submitBtn = binding.submitAnswBtn
        addBtn = binding.addNewVRBTN
        updateBtn = binding.updateNewVRBTN

        addBtn?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                submitBtn?.setOnClickListener {
                    val showDialog=  AddVaccRecDialogFragment()
                    showDialog.show((context as AppCompatActivity).supportFragmentManager, "dialog")
                    dismiss()
                }
            }
        }

        updateBtn?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                submitBtn?.setOnClickListener {
                    val showDialog= UpdateVaccRecDialogFragment()
                    showDialog.show((context as AppCompatActivity).supportFragmentManager, "dialog")
                    dismiss()
                }
            }
        }

        return binding.root
    }

}

