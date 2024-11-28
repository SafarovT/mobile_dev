package com.example.cookieclicker

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookieclicker.databinding.ProductBinding

class ProductViewHolder(view: View): RecyclerView.ViewHolder(view)

class ProductAdapter(
    private val onClick: (Product) -> Unit
): RecyclerView.Adapter<ProductViewHolder>() {
    var productsList = listOf<Product>()

    override fun getItemCount(): Int {
        return productsList.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ProductBinding.inflate(inflater, parent, false)

        return ProductViewHolder(view.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val itemBinding = ProductBinding.bind(holder.itemView)
        val product = productsList[position]

        itemBinding.title.setText(product.titleId)
        itemBinding.price.text = product.price.toString()
        itemBinding.image.setImageResource(product.resId)
        itemBinding.count.text = product.count.toString()

        itemBinding.container.setOnClickListener {
            onClick(product)
        }

        itemBinding.container.alpha = if (product.disabled) 0.5F else 1.0F
    }
}