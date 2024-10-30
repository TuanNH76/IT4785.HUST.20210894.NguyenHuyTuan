package com.example.findinlist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.findinlist.adapter.StudentAdapter
import com.example.findinlist.model.Student

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private lateinit var studentList: List<Student>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Tạo danh sách sinh viên mẫu
        studentList = listOf(
            Student("Nguyen An Tuan", "20220001"),
            Student("Tran Minh Anh", "20220002"),
            Student("Le Van Nam", "20220003"),
            Student("Pham Thi Lan", "20220004"),
            Student("Do Hoang Phuc", "20220005"),
            Student("Ngo Minh Chau", "20220006"),
            Student("Ly Thi Hong", "20220007"),
            Student("Phan Quoc Bao", "20220008"),
            Student("Nguyen Thi Mai", "20220009"),
            Student("Tran Van Hieu", "20220010"),
            Student("Le Thi Thu", "20220011"),
            Student("Pham Minh Tri", "20220012"),
            Student("Do Thi Hoa", "20220013"),
            Student("Ngo Quoc Toan", "20220014"),
            Student("Ly Minh Khoa", "20220015"),
            Student("Phan Thi Linh", "20220016"),
            Student("Nguyen Van Cuong", "20220017"),
            Student("Tran Thi Hanh", "20220018"),
            Student("Le Hoang Bao", "20220019"),
            Student("Pham Thanh Thao", "20220020"),
            Student("Do Duy Khanh", "20220021"),
            Student("Ngo Thi Kim", "20220022"),
            Student("Ly Van Tuan", "20220023"),
            Student("Phan An Binh", "20220024"),
            Student("Nguyen Quang Loc", "20220025"),
            Student("Tran Thi Dao", "20220026"),
            Student("Le Hoang Minh", "20220027"),
            Student("Pham Thi Yen", "20220028"),
            Student("Do Thanh Tuan", "20220029"),
            Student("Ngo Thi Mai", "20220030")
        )


        // Thiết lập RecyclerView với danh sách sinh viên
        studentAdapter = StudentAdapter(studentList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter

        // Thiết lập tìm kiếm khi người dùng nhập từ khóa
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()
                if (keyword.length > 2) {
                    filterList(keyword)
                } else {
                    studentAdapter.updateList(studentList)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Hàm lọc danh sách sinh viên dựa trên từ khóa
    private fun filterList(keyword: String) {
        val filteredList = studentList.filter {
            it.name.contains(keyword, ignoreCase = true) || it.mssv.contains(keyword)
        }
        studentAdapter.updateList(filteredList)
    }
}