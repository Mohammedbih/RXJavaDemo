package com.chocolatecake.rxjavademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chocolatecake.rxjavademo.databinding.ActivitySecondBinding
import com.chocolatecake.rxjavademo.rxeventbus.RxBus
import com.chocolatecake.rxjavademo.rxeventbus.RxBusEvent
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        RxBus.listen(RxBusEvent.CounterEvent::class.java)
            .subscribe { binding.textViewCounter.text = it.number.toString() }
            .add(compositeDisposable)
        binding.buttonBack.setOnClickListener { navigateBack() }
    }

    private fun navigateBack() { finish() }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}