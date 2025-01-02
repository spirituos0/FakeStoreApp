package com.example.myapp

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapp.models.Product

class ProductAdapter(private var products: MutableList<Product>, private var fontSize: Float) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleEditText: EditText = itemView.findViewById(R.id.titleEditText)
        val bodyEditText: EditText = itemView.findViewById(R.id.bodyEditText)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val additionalInfoEditText: EditText = itemView.findViewById(R.id.additionalInfoEditText)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.titleEditText.setText(product.title)
        holder.bodyEditText.setText(product.description)

        val additionalInfo = """
        Price: $${product.price}
        Category: ${product.category}
        Rating: ${product.rating.rate} (${product.rating.count} reviews)
    """.trimIndent()

        holder.additionalInfoEditText.setText(additionalInfo)

        holder.titleEditText.textSize = fontSize
        holder.bodyEditText.textSize = fontSize
        holder.additionalInfoEditText.textSize = fontSize

        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.imageView)

        holder.titleEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                product.title = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.bodyEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                product.description = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        holder.additionalInfoEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val lines = s.toString().split("\n")
                if (lines.size >= 3) {
                    product.price = lines[0].substringAfter("Price: $").toDoubleOrNull() ?: product.price
                    product.category = lines[1].substringAfter("Category: ").trim()
                    val ratingInfo = lines[2].substringAfter("Rating: ").split(" ")
                    product.rating.rate = ratingInfo[0].toDoubleOrNull() ?: product.rating.rate
                    product.rating.count = ratingInfo.getOrNull(1)?.filter { it.isDigit() }?.toIntOrNull() ?: product.rating.count
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    override fun getItemCount() = products.size

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    fun updateFontSize(newFontSize: Float) {
        fontSize = newFontSize
        notifyDataSetChanged()
    }
}


