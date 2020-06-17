package app.sakai.tororoimo.tororoimo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ResultAdapter (
    private val context: Context,
    private var resultList: OrderedRealmCollection<Results>?,
    private var autoUpdate: Boolean
) :
        RealmRecyclerViewAdapter<Results, ResultAdapter.ResultViewHolder>(resultList, autoUpdate) {
    override fun getItemCount(): Int = resultList?.size ?: 0
    override fun onBindViewHolder(holder: ResultAdapter.ResultViewHolder, position: Int) {
        val result: Results = resultList?.get(position) ?: return
        holder.dateTextView.text = SimpleDateFormat("yyyy年MM月dd日", Locale.JAPANESE).format(result.date)
        holder.resultTextView.text = result.resultTextNumber.toString() + "とろろいも"
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResultAdapter.ResultViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return ResultViewHolder(v)
    }

    class ResultViewHolder(view: View) :RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.dateTextView
        val resultTextView: TextView = view.resultTextView
    }
}

