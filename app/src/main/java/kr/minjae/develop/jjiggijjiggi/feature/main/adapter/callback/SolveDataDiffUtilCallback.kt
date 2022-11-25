package kr.minjae.develop.jjiggijjiggi.feature.main.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import kr.minjae.develop.jjiggijjiggi.feature.main.data.SolveData

object SolveDataDiffUtilCallback : DiffUtil.ItemCallback<SolveData>() {
    override fun areItemsTheSame(oldItem: SolveData, newItem: SolveData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: SolveData, newItem: SolveData): Boolean {
        return oldItem == newItem
    }
}