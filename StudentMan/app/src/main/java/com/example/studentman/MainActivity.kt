package com.example.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentman.R
import com.example.studentman.StudentModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )


    private val studentAdapter = StudentAdapter(students, this::editStudent, this::deleteStudent)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = LayoutInflater.from(this@MainActivity)
            .inflate(R.layout.layout_alert_dialog, null)

        val editHoten = dialogView.findViewById<EditText>(R.id.edit_hoten)
        val editMssv = dialogView.findViewById<EditText>(R.id.edit_mssv)

        AlertDialog.Builder(this)
            .setTitle("Nhap thong tin sinh vien")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val hoten = editHoten.text.toString()
                val mssv = editMssv.text.toString()
                if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
                    addStudent(StudentModel(hoten, mssv))
                }
            }
            .setNegativeButton("Cancel", null)
            .create().show()
    }

    private fun addStudent(student: StudentModel) {
        students.add(student)
        studentAdapter.notifyItemInserted(students.size - 1)
    }

    private fun editStudent(position: Int) {
        val student = students[position]

        val dialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.layout_alert_dialog, null)

        val editHoten = dialogView.findViewById<EditText>(R.id.edit_hoten)
        val editMssv = dialogView.findViewById<EditText>(R.id.edit_mssv)

        editHoten.setText(student.studentName)
        editMssv.setText(student.studentId)

        AlertDialog.Builder(this)
            .setTitle("Sua thong tin sinh vien")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val newHoten = editHoten.text.toString()
                val newMssv = editMssv.text.toString()
                if (newHoten.isNotEmpty() && newMssv.isNotEmpty()) {
                    students[position] = StudentModel(newHoten, newMssv)
                    studentAdapter.notifyItemChanged(position)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun deleteStudent(position: Int) {
        val deletedStudent = students[position]
        AlertDialog.Builder(this)
            .setTitle("Xóa sinh viên")
            .setMessage("Bạn có chắc muốn xóa sinh viên này?")
            .setPositiveButton("Delete") { _, _ ->
                students.removeAt(position)
                studentAdapter.notifyItemRemoved(position)

                Snackbar.make(findViewById(R.id.recycler_view_students),
                    "Đã xóa sinh viên: ${deletedStudent.studentName}",
                    Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        students.add(position, deletedStudent)
                        studentAdapter.notifyItemInserted(position)
                    }
                    .show()
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}