package com.example.infoform

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.infoform.AddressHelper
import org.json.JSONObject
import android.app.DatePickerDialog
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var etMssv: EditText
    private lateinit var etName: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var rbMale: RadioButton
    private lateinit var rbFemale: RadioButton
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnToggleCalendar: Button
    private lateinit var spCity: Spinner
    private lateinit var spDistrict: Spinner
    private lateinit var spWard: Spinner
    private lateinit var cbSports: CheckBox
    private lateinit var cbMovies: CheckBox
    private lateinit var cbMusic: CheckBox
    private lateinit var cbAgreement: CheckBox
    private lateinit var btnSubmit: Button

    // Address Helper
    private lateinit var addressHelper: AddressHelper

    // Selected Date
    private var selectedDate: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        initUI()

        // Initialize AddressHelper
        addressHelper = AddressHelper(resources)

        // Populate Province Spinner
        populateProvinces()

        // Handle Province Selection
        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) { // Position 0 is "Chọn tỉnh thành"
                    val selectedProvince = parent.getItemAtPosition(position) as String
                    populateDistricts(selectedProvince)
                } else {
                    // Reset District and Ward Spinners
                    resetSpinner(spDistrict, "Chọn quận huyện")
                    resetSpinner(spWard, "Chọn phường xã")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Handle District Selection
        spDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) { // Position 0 is "Chọn quận huyện"
                    val selectedProvince = spCity.selectedItem as String
                    val selectedDistrict = parent.getItemAtPosition(position) as String
                    populateWards(selectedProvince, selectedDistrict)
                } else {
                    // Reset Ward Spinner
                    resetSpinner(spWard, "Chọn phường xã")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Toggle Calendar Visibility
        // Set Date Picker Dialog on Button Click
        btnToggleCalendar.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    btnToggleCalendar.text = "Ngày sinh: $selectedDate"
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }


        // Handle Submit Button Click
        btnSubmit.setOnClickListener {
            handleSubmit()
        }
    }

    private fun initUI() {
        etMssv = findViewById(R.id.etMssv)
        etName = findViewById(R.id.etName)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        btnToggleCalendar = findViewById(R.id.btnToggleCalendar)
        spCity = findViewById(R.id.spCity)
        spDistrict = findViewById(R.id.spDistrict)
        spWard = findViewById(R.id.spWard)
        cbSports = findViewById(R.id.cbSports)
        cbMovies = findViewById(R.id.cbMovies)
        cbMusic = findViewById(R.id.cbMusic)
        cbAgreement = findViewById(R.id.cbAgreement)
        btnSubmit = findViewById(R.id.btnSubmit)
    }

    private fun populateProvinces() {
        val provinces = addressHelper.getProvinces().sorted()
        val provinceList = mutableListOf("Chọn tỉnh thành")
        provinceList.addAll(provinces)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            provinceList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCity.adapter = adapter
    }

    private fun populateDistricts(province: String) {
        val districts = addressHelper.getDistricts(province).sorted()
        val districtList = mutableListOf("Chọn quận huyện")
        districtList.addAll(districts)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            districtList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDistrict.adapter = adapter

        // Reset Ward Spinner
        resetSpinner(spWard, "Chọn phường xã")
    }

    private fun populateWards(province: String, district: String) {
        val wards = addressHelper.getWards(province, district).sorted()
        val wardList = mutableListOf("Chọn phường xã")
        wardList.addAll(wards)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            wardList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spWard.adapter = adapter
    }

    private fun resetSpinner(spinner: Spinner, defaultOption: String) {
        val list = mutableListOf(defaultOption)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun handleSubmit() {
        val mssv = etMssv.text.toString().trim()
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        // Determine Gender
        val genderId = radioGroupGender.checkedRadioButtonId
        val gender = when (genderId) {
            R.id.rbMale -> "Nam"
            R.id.rbFemale -> "Nữ"
            else -> ""
        }

        // Collect Hobbies
        val hobbies = mutableListOf<String>()
        if (cbSports.isChecked) hobbies.add("Thể thao")
        if (cbMovies.isChecked) hobbies.add("Điện ảnh")
        if (cbMusic.isChecked) hobbies.add("Âm nhạc")

        // Get Selected Location
        val selectedCity = if (spCity.selectedItemPosition > 0) spCity.selectedItem as String else ""
        val selectedDistrict = if (spDistrict.selectedItemPosition > 0) spDistrict.selectedItem as String else ""
        val selectedWard = if (spWard.selectedItemPosition > 0) spWard.selectedItem as String else ""

        // Form Validation
        if (mssv.isEmpty() ||
            name.isEmpty() ||
            email.isEmpty() ||
            phone.isEmpty() ||
            gender.isEmpty() ||
            selectedDate.isEmpty() ||
            selectedCity.isEmpty() ||
            selectedDistrict.isEmpty() ||
            selectedWard.isEmpty() ||
            !cbAgreement.isChecked
        ) {
            Toast.makeText(
                this,
                "Vui lòng điền đầy đủ thông tin và đồng ý điều khoản",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Display or Handle Form Data
        val message = """
            MSSV: $mssv
            Họ tên: $name
            Giới tính: $gender
            Email: $email
            Số điện thoại: $phone
            Ngày sinh: $selectedDate
            Nơi ở: $selectedWard, $selectedDistrict, $selectedCity
            Sở thích: ${hobbies.joinToString()}
        """.trimIndent()

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        // Optionally, reset the form after submission
        resetForm()
    }

    private fun resetForm() {
        etMssv.text.clear()
        etName.text.clear()
        radioGroupGender.clearCheck()
        etEmail.text.clear()
        etPhone.text.clear()
        selectedDate = ""
        resetSpinner(spCity, "Chọn tỉnh thành")
        resetSpinner(spDistrict, "Chọn quận huyện")
        resetSpinner(spWard, "Chọn phường xã")
        cbSports.isChecked = false
        cbMovies.isChecked = false
        cbMusic.isChecked = false
        cbAgreement.isChecked = false
    }
}
