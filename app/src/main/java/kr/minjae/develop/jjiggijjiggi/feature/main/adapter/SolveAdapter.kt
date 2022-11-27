package kr.minjae.develop.jjiggijjiggi.feature.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.minjae.develop.jjiggijjiggi.databinding.ItemSolveBinding
import kr.minjae.develop.jjiggijjiggi.feature.main.adapter.callback.SolveDataDiffUtilCallback
import kr.minjae.develop.jjiggijjiggi.feature.main.data.SolveData

class SolveAdapter : ListAdapter<SolveData, SolveAdapter.SolveViewHolder>(SolveDataDiffUtilCallback) {

    inner class SolveViewHolder(private val binding: ItemSolveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SolveData) {
            binding.tvSolveTitle.text = data.title
            binding.tvSolveContent.text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolveViewHolder {
        return SolveViewHolder(
            ItemSolveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SolveViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }
}