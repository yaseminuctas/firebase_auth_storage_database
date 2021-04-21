package com.yaseminuctas.arabamisat.ui.carsdetail

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.yaseminuctas.arabamisat.model.CarData
import com.yaseminuctas.arabamisat.R
import com.yaseminuctas.arabamisat.extension.loadImage

class CarListAdapter(private val carList: List<CarData>) :
    RecyclerView.Adapter<CarListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListViewHolder {
        return CarListViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun onBindViewHolder(holder: CarListViewHolder, position: Int) {
        holder.bindTo(carList[position])
    }
}

class CarListViewHolder(viewGroup: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(viewGroup.context)
        .inflate(R.layout.item_cars, viewGroup, false)
) {

    private val tvCarDesc by lazy { itemView.findViewById<TextView>(R.id.tvCarDesc) }
    private val imgCar by lazy { itemView.findViewById<ImageView>(R.id.imgItemCar) }

    fun bindTo(data: CarData) {
        tvCarDesc.text = data.desc
        downloadFile(imgCar, data.imgUri)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun downloadFile(imgCar: ImageView, imgUri: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef =
            storage.getReferenceFromUrl(imgUri)
        imgCar.loadImage(storageRef)

    }
}


