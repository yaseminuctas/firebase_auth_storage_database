package com.yaseminuctas.arabamisat.ui.carsdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.yaseminuctas.arabamisat.model.CarData
import com.yaseminuctas.arabamisat.R
import com.yaseminuctas.arabamisat.databinding.ActivityCarsDetailBinding
import kotlinx.android.synthetic.main.activity_cars_detail.*

class CarsDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityCarsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        initDataBinding()

        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@CarsDetailActivity)
        }

        retrieveData()

    }

    private fun initDataBinding() {
        binding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_cars_detail
            )
    }

    private fun retrieveData() {

        var database: DatabaseReference = FirebaseDatabase.getInstance().reference

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val posts = ArrayList<CarData>()

                dataSnapshot.children.forEach {
                    val post = it.getValue(CarData::class.java)
                    posts.add(post!!)
                }

                val adapter =
                    CarListAdapter(
                        posts
                    )
                recyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                //print error.message
            }
        })
    }

}