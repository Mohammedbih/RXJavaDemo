package com.chocolatecake.rxjavademo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.chocolatecake.rxjavademo.databinding.ActivityMainBinding
import com.chocolatecake.rxjavademo.rxeventbus.RxBus
import com.chocolatecake.rxjavademo.rxeventbus.RxBusEvent
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        addCallBacks()
    }

    private fun setup() {
        RxBus.listen(RxBusEvent.CounterEvent::class.java)
            .subscribe { binding.textViewCounter.text = it.number.toString() }
            .add(compositeDisposable)
    }

    private fun addCallBacks() {
        with(binding) {
            buttonPlus.setOnClickListener { increaseCounter() }
            buttonMinus.setOnClickListener { decreaseCounter() }

            Observable.create { emitter ->
                buttonNavigate.setOnClickListener {
                    emitter.onNext(Unit)
                }
            }.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    navigateToSecond()
                }.add(compositeDisposable)
        }
    }

    private fun navigateToSecond() {
        startActivity(Intent(this, SecondActivity::class.java))
//        Log.d(TAG, "navigateToSecond: ")
    }

    private fun decreaseCounter() {
        val number = binding.textViewCounter.text.toString().toInt() - 1
        RxBus.publish(RxBusEvent.CounterEvent(number))
    }

    private fun increaseCounter() {
        val number = binding.textViewCounter.text.toString().toInt() + 1
        RxBus.publish(RxBusEvent.CounterEvent(number))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}


