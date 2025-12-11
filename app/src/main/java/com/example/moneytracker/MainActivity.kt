package com.example.moneytracker

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moneytracker.databinding.ActivityMainBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var database: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

          database = FirebaseDatabase.getInstance().reference
        fetchMoney()
        fetchTodaySpending()
        val userid = "userid1"

        binding.addmoney.setOnClickListener {

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.add_money)
            dialog.setCancelable(true)
            dialog.show()

            val addMoneyButton = dialog.findViewById<Button>(R.id.add_money_dialog)
            val added_Money = dialog.findViewById<TextInputEditText>(R.id.added_amount)



            addMoneyButton.setOnClickListener {

                val total_money = added_Money.text.toString().toDoubleOrNull()

                if( total_money != null && total_money > 0 ) {


                    val moneyAdditionKey = database.child("Users").child("userid").child("added money").push().key
                    val money_addition = mapOf(
                        "amount" to total_money,
                        "time" to System.currentTimeMillis()
                    )

                    if (moneyAdditionKey != null) {

                        database.child("Users").child(userid).child("total money")
                            .setValue(ServerValue.increment(total_money))
                        database.child("Users").child(userid).child("added money").child(moneyAdditionKey)
                            .setValue(money_addition)
                        Toast.makeText(this, "Done its working", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }
                
            }

        }


        binding.spendMoney.setOnClickListener {
            val dialog2 = Dialog(this)
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog2.setContentView(R.layout.add_spending)
            dialog2.setCancelable(true)
            dialog2.show()

            val spendmoneybtn = dialog2.findViewById<Button>(R.id.spend_money)
            val spent_money = dialog2.findViewById<TextInputEditText>(R.id.amout)
            val reason_to_spend = dialog2.findViewById<TextInputEditText>(R.id.Reason)

            spendmoneybtn.setOnClickListener {
                val money_speent = spent_money.text.toString().toDoubleOrNull()
                val reason = reason_to_spend.text.toString()

                if (money_speent != null && money_speent>0 && reason.isNotEmpty()){
                    database.child("Users").child(userid).child("total money").setValue(ServerValue.increment(-money_speent))
                    val spendingkey = database.child("Users").child(userid).child("spending History").push().key
                    val spending_history = mapOf(
                        "amount" to money_speent,
                        "reason" to reason,
                        "time" to System.currentTimeMillis()
                    )
                    if (spendingkey != null){
                        database.child("Users").child(userid).child("spending History").child(spendingkey).setValue(spending_history)
                    }
                Toast.makeText(this, "working", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }

                }


        }

    }

    private fun fetchMoney() {

        val userId = "userid1"  // Replace with actual user ID logic if needed
        val totalMoneyRef = database.child("Users").child(userId).child("total money")


        totalMoneyRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                val totalMoney = snapshot.getValue(Double::class.java) ?: 0.0
                binding.moneyShow.text = "$totalMoney"
            }

             override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to fetch total money: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun fetchTodaySpending() {
        val userId = "userid1"
        val today = getStartOfDayTimestamp()
        val spendingRef = database.child("Users").child(userId).child("spending History")

        spendingRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var todaySpending = 0.0
                for (data in snapshot.children) {
                    val spendingTime = data.child("time").getValue(Long::class.java) ?: 0L
                    val spendingAmount = data.child("amount").getValue(Double::class.java) ?: 0.0
                    if (spendingTime >= today) {
                        todaySpending += spendingAmount
                    }
                }
                binding.spendingToday.text = "$todaySpending"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to fetch today's spending: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getStartOfDayTimestamp(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

}
