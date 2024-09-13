package com.ct07.ttn.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.ct07.ttn.R
import com.ct07.ttn.ui.RoomActivity
import com.ct07.ttn.ui.room.RoomViewModel
import com.ct07.ttn.util.Resource

class AddressDialogFragment(private val listener: OnAddressSelectedListener) : DialogFragment() {

    lateinit var roomViewModel: RoomViewModel

    var provinceCode: Int? = null
    var districtCode: Int? = null
    var wardCode: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.fragment_address_dialog, null)

            // Lấy dữ liệu tương ứng với các view
            val province = view.findViewById<AutoCompleteTextView>(R.id.addProvince)
            val district = view.findViewById<AutoCompleteTextView>(R.id.addDistrict)
            val ward = view.findViewById<AutoCompleteTextView>(R.id.addWard)
            val road = view.findViewById<EditText>(R.id.addRoad)

            // Ngăn chặn người dùng nhập liệu
            province.keyListener = null
            district.keyListener = null
            ward.keyListener = null

            // Hiển thị danh sách khi người dùng nhấn vào trường nhập liệu
            province.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    province.showDropDown()
                }
                false
            }

            district.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    district.showDropDown()
                }
                false
            }

            ward.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    ward.showDropDown()
                }
                false
            }

            // Gọi API và lấy dữ liệu
            roomViewModel = (activity as RoomActivity).roomViewModel

            roomViewModel.getProvince()
            roomViewModel.provinces.observe(this, Observer { resource ->
                if (resource is Resource.Success) {
                    resource.data?.data?.let { provinceList ->
                        val provinceNames = provinceList.map { it.full_name }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, provinceNames)
                        province.setAdapter(adapter)
                    }
                }
            })

            province.setOnItemClickListener { parent, view, position, id ->
                val selectedProvince = parent.getItemAtPosition(position).toString()
                roomViewModel.getDistricts(selectedProvince)
            }
            roomViewModel.districts.observe(this, Observer { resource ->
                if (resource is Resource.Success) {
                    resource.data?.data?.let { districtList ->
                        val districtNames = districtList.map { it.full_name }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, districtNames)
                        district.setAdapter(adapter)
                    }
                }
            })

            district.setOnItemClickListener { parent, view, position, id ->
                val selectedDistrict = parent.getItemAtPosition(position).toString()
                roomViewModel.getWards(selectedDistrict)
            }
            roomViewModel.wards.observe(this, Observer { resource ->
                if (resource is Resource.Success) {
                    resource.data?.data?.let { wardList ->
                        val wardNames = wardList.map { it.full_name }
                        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, wardNames)
                        ward.setAdapter(adapter)
                    }
                }
            })

            builder.setView(view)
                .setPositiveButton("Hoàn thành") { _, _ ->
                    val provinceText = province.text.toString()
                    val districtText = district.text.toString()
                    val wardText = ward.text.toString()
                    val roadText = road.text.toString()
                    listener?.onAddressSelected(provinceText, districtText, wardText, roadText)
                }
            builder.create()
        } ?: throw IllegalStateException("Chưa có hoạt động nào!")
    }
}
