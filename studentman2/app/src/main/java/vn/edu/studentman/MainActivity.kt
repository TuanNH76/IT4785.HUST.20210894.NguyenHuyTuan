package vn.edu.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vn.edu.hust.R

class MainActivity : AppCompatActivity() {

  // Danh sách sinh viên
  val students = mutableListOf(
    StudentModel("Phùng Tiến Đạt", "SV001"),
    StudentModel("Đỗ Thùy Dương", "SV002"),
    StudentModel("Mai Việt Dũng", "SV003"),
    StudentModel("Nguyễn Huy Tuấn", "SV004"),
    StudentModel("Lê Quang Minh", "SV005")
  )

  // Khởi tạo Adapter cho ListView
  lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Khởi tạo Adapter và truyền callback xử lý
    studentAdapter = StudentAdapter(
      this,
      students,
      onEditClick = { student ->
        // Mở Fragment chỉnh sửa sinh viên
        openEditStudentFragment(student)
      },
      onRemoveClick = { student ->
        // Xóa sinh viên khỏi danh sách
        students.remove(student)
        studentAdapter.notifyDataSetChanged() // Cập nhật lại ListView
        Toast.makeText(this, "Removed ${student.studentName}", Toast.LENGTH_SHORT).show()
      }
    )

    val listView = findViewById<ListView>(R.id.list_view_students)
    listView.adapter = studentAdapter

    // Đăng ký context menu cho ListView
    registerForContextMenu(listView)

    // Set OnItemClickListener cho ListView
    listView.setOnItemClickListener { _, _, position, _ ->
      val student = students[position]
      openEditStudentFragment(student)
    }

    // Add "Add new" button click listener
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      openAddStudentFragment()
    }
  }

  // Mở Fragment thêm sinh viên
  private fun openAddStudentFragment() {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container, AddStudentFragment())
    transaction.addToBackStack(null)
    transaction.commit()
  }

  // Mở Fragment để chỉnh sửa thông tin sinh viên
  private fun openEditStudentFragment(student: StudentModel) {
    val transaction = supportFragmentManager.beginTransaction()
    val fragment = EditStudentFragment.newInstance(student)
    transaction.replace(R.id.container, fragment)
    transaction.addToBackStack(null)
    transaction.commit()
  }

  // Thêm sinh viên vào danh sách
  fun addStudent(newStudent: StudentModel) {
    students.add(newStudent)
    studentAdapter.notifyDataSetChanged()  // Cập nhật lại ListView
  }
}

