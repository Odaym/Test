package com.example.myapplication

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.functions.Consumer
import java.lang.IllegalArgumentException

interface DataProvider

interface Identifiable {
    val identity: Int
    val hash: Int
}

interface ViewHolderDataProvider : DataProvider, Identifiable {
    val identifier: Int
}

fun <T : Identifiable> createDiffCallback() = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.identity == newItem.identity
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem.hash == newItem.hash
}

abstract class DataViewHolder(layoutId: Int, parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
) {
    interface Factory {
        val identifier: Int

        fun create(parent: ViewGroup): DataViewHolder
    }

    abstract fun bind(data: DataProvider)
    open fun onAttached() {}
    open fun onDetached() {}
}

class RecyclerAdapter : RecyclerView.Adapter<DataViewHolder>() {
    private val factories = SparseArray<DataViewHolder.Factory>()

    private val differ = AsyncListDiffer(
        this,
        createDiffCallback<ViewHolderDataProvider>()
    )

    fun register(factory: DataViewHolder.Factory) {
        factories.put(factory.identifier, factory)
    }

    fun reload(dataProviders: List<ViewHolderDataProvider>) {
        differ.submitList(dataProviders)
    }

    fun dataProviders(): Consumer<List<ViewHolderDataProvider>> {
        return Consumer { reload(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val factory = factories[viewType]
            ?: throw IllegalArgumentException("No factory has been registered for this identifier $viewType")
        return factory.create(parent)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].identifier
    }

    override fun onViewAttachedToWindow(holder: DataViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttached()
    }

    override fun onViewDetachedFromWindow(holder: DataViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetached()
    }
}

