package  com.wajeez.sample.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wajeez.sample.databinding.UserItemBinding
import com.wajeez.sample.databinding.UserItemBindingImpl
import com.wajeez.sample.model.data.UserModel

class MainAdapter() :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var mUsersData: ArrayList<UserModel>? = null


    fun setUsersData(mUsersData: ArrayList<UserModel>) {
        this.mUsersData = mUsersData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            UserItemBindingImpl.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.onBind(mUsersData!![position])
    }

    override fun getItemCount(): Int {
        return mUsersData?.size ?: 0
    }


    class MainViewHolder(private val view: UserItemBinding) : RecyclerView.ViewHolder(view.root) {

        fun onBind(model: UserModel) {
            view.user = model
            view.textViewUserName.text = model.name
        }
    }
}