package com.example.gmail
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        // Create sample email data
        val emailList = listOf(
            Email("Nguyen Huy Tuan", "20210894", "Reminder: Prepare for the upcoming presentation on 1 Nov, 2024", "17:35 PM"),
            Email("Emma Carter", "Team Meeting", "Please join the team meeting scheduled for tomorrow at 10 AM.", "15:20 PM"),
            Email("Accounting Department", "Payment Confirmation", "Your payment for October has been received. Thank you!", "13:50 PM"),
            Email("LinkedIn", "Job Recommendations", "Explore new job opportunities based on your profile.", "12:05 PM"),
            Email("Amazon", "Order Shipped", "Your order #789012 has been shipped and is on its way.", "10:45 AM"),
            Email("Company IT Support", "System Maintenance", "Scheduled maintenance will occur this weekend. Expect some downtime.", "9:30 AM"),
            Email("Fitness Club", "Special Offer", "Get 20% off on personal training packages this month!", "Yesterday")        )
        val adapter = EmailAdapter(emailList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        // Floating Action Button click action
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            Toast.makeText(this, "Compose Email", Toast.LENGTH_SHORT).show()
        }
    }
}