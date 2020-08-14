package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val adapter = RecyclerAdapter()

    val viewModel = MainViewModel()

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        elementsList.layoutManager = LinearLayoutManager(this)
        elementsList.adapter = adapter

        adapter.register(TwoImagesHolder.Factory())

        disposables.add(
            viewModel.elements
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { adapter.reload(it)}
        )
    }
}