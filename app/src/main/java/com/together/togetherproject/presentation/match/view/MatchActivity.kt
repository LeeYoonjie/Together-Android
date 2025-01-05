package com.together.togetherproject.presentation.match.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.together.togetherproject.R
import com.together.togetherproject.data.model.MatchedItem
import com.together.togetherproject.presentation.match.viewmodel.MatchViewModel

class MatchActivity : AppCompatActivity() {

    private lateinit var startAddressEditText: EditText
    private lateinit var destinationEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var accountNumberEditText: EditText
    private lateinit var paymentAmountEditText: EditText
    private lateinit var completeButton: Button
    private lateinit var cancelButton: Button

    private val matchViewModel: MatchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_form)

        startAddressEditText = findViewById(R.id.et_start_address)
        destinationEditText = findViewById(R.id.et_destination)
        startTimeEditText = findViewById(R.id.et_start_time)
        accountNumberEditText = findViewById(R.id.et_account_number)
        paymentAmountEditText = findViewById(R.id.et_payment_amount)
        completeButton = findViewById(R.id.btn_complete)
        cancelButton = findViewById(R.id.btn_close)

        completeButton.setOnClickListener { handleCompleteButton() }
        cancelButton.setOnClickListener { handleCancelButton() }
    }

    private fun handleCompleteButton() {
        val startAddress = startAddressEditText.text.toString().trim()
        val destination = destinationEditText.text.toString().trim()
        val startTime = startTimeEditText.text.toString().trim()
        val accountNumber = accountNumberEditText.text.toString().trim()
        val paymentAmount = paymentAmountEditText.text.toString().trim()

        val paymentAmountDouble = paymentAmount.toDoubleOrNull()
        if (startAddress.isEmpty() || destination.isEmpty() || startTime.isEmpty() || accountNumber.isEmpty() || paymentAmountDouble == null) {
            Toast.makeText(this, "모든 필드를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val chatRoomId = intent.getStringExtra("CHAT_ROOM_ID") ?: run {
            Toast.makeText(this, "채팅방 ID가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val matchedItem = MatchedItem(
            matchedRoomId = chatRoomId,
            user1Name = "사용자A",
            user2Name = "사용자B",
            startAddress = startAddress,
            destination = destination,
            startTime = startTime,
            accountNumber = accountNumber,
            paymentAmount = paymentAmount,
            individualPayment = (paymentAmountDouble / 2).toString()
        )

        matchViewModel.saveMatchedInfoToChatRoom(
            chatRoomId,
            matchedItem,
            onSuccess = {
                Toast.makeText(this, "매칭 정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            },
            onFailure = { exception ->
                Toast.makeText(this, "매칭 정보 저장 실패: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun handleCancelButton() {
        Toast.makeText(this, "매칭이 취소되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }
}