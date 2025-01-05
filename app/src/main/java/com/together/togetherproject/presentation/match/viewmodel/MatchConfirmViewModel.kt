package com.together.togetherproject.presentation.match.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MatchConfirmViewModel : ViewModel() {

    private val _startAddress = MutableLiveData<String>()
    val startAddress: LiveData<String> get() = _startAddress

    private val _destination = MutableLiveData<String>()
    val destination: LiveData<String> get() = _destination

    private val _startTime = MutableLiveData<String>()
    val startTime: LiveData<String> get() = _startTime

    private val _accountNumber = MutableLiveData<String>()
    val accountNumber: LiveData<String> get() = _accountNumber

    private val _paymentAmount = MutableLiveData<String>()
    val paymentAmount: LiveData<String> get() = _paymentAmount

    private val _individualPayment = MutableLiveData<String>()
    val individualPayment: LiveData<String> get() = _individualPayment

    // 데이터를 설정하는 메서드
    fun setData(
        startAddress: String,
        destination: String,
        startTime: String,
        accountNumber: String,
        paymentAmount: String,
        individualPayment: String
    ) {
        _startAddress.value = startAddress
        _destination.value = destination
        _startTime.value = startTime
        _accountNumber.value = accountNumber
        _paymentAmount.value = paymentAmount
        _individualPayment.value = individualPayment
    }
}